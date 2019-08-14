package com.mango.authorize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mango.sys.entity.SysUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的用户授权域
 */
public class UserRedisRealm extends UserAuthorizingRealm {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StringToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        SysUser user = (SysUser)principals.getPrimaryPrincipal();
        String token=(String)redisTemplate.opsForHash().get("user_token",user.getId().toString());
        Object obj = redisTemplate.opsForValue().get("user_token-" + token);
        if(obj!=null){
            JSONObject userToken = (JSONObject)obj;
            if(userToken.containsKey("roles")){
                JSONArray roles = userToken.getJSONArray("roles");
                Set<String> roleSet = new HashSet<>();
                for(int i=0;i<roles.size();i++){
                    roleSet.add(roles.getString(i));
                }
                info.setRoles(roleSet);
            }
            if(userToken.containsKey("perms")) {
                JSONArray perms = userToken.getJSONArray("perms");
                Set<Permission> permSet = new HashSet<>();
                for (int i = 0; i < perms.size(); i++) {
                    JSONObject perm = perms.getJSONObject(i);
                    RequestPermission permission = new RequestPermission();
                    permission.setCode(perm.getString("code"));
                    permission.setFilter(perm.getJSONObject("filter"));
                    permSet.add(permission);
                }

                info.setObjectPermissions(permSet);
            }
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        Object obj=redisTemplate.opsForValue().get("user_token-"+ accessToken);
        if(obj==null){
            throw new IncorrectCredentialsException("Token不存在或已失效，请重新登录");
        }else{
            Long minutes = redisTemplate.getExpire("user_token-"+ accessToken, TimeUnit.MINUTES);
            if(minutes!=null && minutes.intValue() < 15){
                redisTemplate.expire("user_token-"+ accessToken, 1, TimeUnit.HOURS);
            }
            JSONObject userToken = (JSONObject)obj;
            SysUser user = JSON.parseObject(userToken.getString("user"), SysUser.class);
            return new SimpleAuthenticationInfo(user, accessToken, getName());
        }
    }
}