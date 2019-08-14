package com.mango.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.common.DictConstants;
import com.mango.config.RedisConfig;
import com.mango.sys.entity.SysRole;
import com.mango.sys.entity.SysUser;
import com.mango.sys.entity.SysUserToken;
import com.mango.sys.mapper.SysUserTokenMapper;
import com.mango.sys.service.SysRoleService;
import com.mango.sys.service.SysUserService;
import com.mango.sys.service.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
@Service
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenMapper, SysUserToken> implements SysUserTokenService {

    private final SysUserService sysUserService;

    private final SysRoleService sysRoleService;

    private final RedisTemplate<String, Object> redisTemplate;



    @Autowired
    public SysUserTokenServiceImpl(SysUserService sysUserService, SysRoleService sysRoleService, RedisTemplate<String, Object> redisTemplate) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings("UnusedAssignment")
    @Override
    public String createToken(SysUser user) {
        String token = UUID.randomUUID().toString();
        if(RedisConfig.enabled){
            JSONObject userToken=new JSONObject();
            userToken.put("user", JSON.toJSON(user));
            List<SysRole> roleList = sysUserService.getUserHasRoles(user.getId());
            JSONArray roles = new JSONArray();
            JSONArray perms = new JSONArray();
            JSONArray menuCodes=new JSONArray();
            for(SysRole role:roleList){
                if(role.getState().equals(DictConstants.USE_STATE_ENABLED)){
                    roles.add(role.getCode());
                    List<Map<String, Object>> menuList = sysRoleService.getRoleHasResourcesWithFilter(role.getId());
                    for(Map<String, Object> menu:menuList){
                        String code = menu.get("code").toString();
                        if(role.getState().equals(DictConstants.USE_STATE_ENABLED) && !menuCodes.contains(code)){
                            menuCodes.add(code);
                            JSONObject perm=new JSONObject();
                            perm.put("code", code);
                            perm.put("filter", JSON.parseObject(menu.get("filter").toString()));
                            perms.add(perm);
                        }
                    }
                }
            }
            userToken.put("roles", roles);
            userToken.put("perms", perms);
            if(redisTemplate.opsForHash().hasKey("user_token", user.getId().toString())){
                String curToken = (String)redisTemplate.opsForHash().get("user_token", user.getId().toString());
                redisTemplate.delete("user_token-" + curToken);
            }
            redisTemplate.opsForValue().set("user_token-" + token, userToken, 3, TimeUnit.HOURS);
            redisTemplate.opsForHash().put("user_token", user.getId().toString(), token);
        }else {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expireTime = now.plus(3, ChronoUnit.HOURS);
            SysUserToken userToken = this.getById(user.getId());
            if(userToken == null){
                userToken = new SysUserToken();
                userToken.setSysUserId(user.getId());
                userToken.setToken(token);
                userToken.setUpdateTime(now);
                userToken.setExpireTime(expireTime);
                this.save(userToken);
            }else{
                userToken.setToken(token);
                userToken.setUpdateTime(now);
                userToken.setExpireTime(expireTime);
                this.saveOrUpdate(userToken);
            }
        }
        return token;
    }

    @Override
    public SysUserToken queryByToken(String token) {
        return this.getOne(new QueryWrapper<SysUserToken>().eq("token", token));
    }

    @Override
    public void extendToken(Long userId) {
        SysUserToken userToken = new SysUserToken();
        userToken.setSysUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plus(1, ChronoUnit.HOURS);
        userToken.setUpdateTime(now);
        userToken.setExpireTime(expireTime);
        this.updateById(userToken);
    }

    @SuppressWarnings("UnusedAssignment")
    @Override
    public void destroyToken(String token) {
        if(RedisConfig.enabled){
            redisTemplate.delete("user_token-" + token);
        }else{
            SysUserToken userToken = queryByToken(token);
            if (userToken!=null) {
                userToken.setExpireTime(LocalDateTime.now());
                this.saveOrUpdate(userToken);
            }
        }
    }
}
