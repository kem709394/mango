package com.mango.authorize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mango.common.DictConstants;
import com.mango.sys.entity.SysRole;
import com.mango.sys.entity.SysUser;
import com.mango.sys.entity.SysUserToken;
import com.mango.sys.service.SysRoleService;
import com.mango.sys.service.SysUserService;
import com.mango.sys.service.SysUserTokenService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基于数据库的用户授权域
 */
public class UserJdbcRealm extends UserAuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StringToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        SysUser user = (SysUser)principals.getPrimaryPrincipal();
        List<SysRole> roleList = sysUserService.getUserHasRoles(user.getId());
        Set<String> roleSet = new HashSet<>();
        Set<Permission> permSet = new HashSet<>();
        JSONArray menuCodes=new JSONArray();
        for(SysRole role:roleList){
            if(role.getState().equals(DictConstants.USE_STATE_ENABLED)){
                roleSet.add(role.getCode());
                List<Map<String, Object>> menuList = sysRoleService.getRoleHasResourcesWithFilter(role.getId());
                for(Map<String, Object> menu:menuList){
                    String code = menu.get("code").toString();
                    if(role.getState().equals(DictConstants.USE_STATE_ENABLED) && !menuCodes.contains(code)){
                        RequestPermission permission=new RequestPermission();
                        permission.setCode(code);
                        permission.setFilter(JSON.parseObject(menu.get("filter").toString()));
                        permSet.add(permission);
                        menuCodes.add(code);
                    }
                }
            }
        }
        info.setRoles(roleSet);
        info.setObjectPermissions(permSet);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        SysUserToken userToken = sysUserTokenService.queryByToken(accessToken);
        if(userToken == null || userToken.getExpireTime().isBefore(LocalDateTime.now())){
            throw new IncorrectCredentialsException("Token不存在或已失效，请重新登录");
        }
        if (userToken.getExpireTime().plus(5, ChronoUnit.MINUTES).isBefore(LocalDateTime.now())) {
            sysUserTokenService.extendToken(userToken.getSysUserId());
        }
        SysUser user = sysUserService.getById(userToken.getSysUserId());
        if(user.getState().equals(DictConstants.USE_STATE_DISABLED)){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        return new SimpleAuthenticationInfo(user, accessToken, getName());
    }


}