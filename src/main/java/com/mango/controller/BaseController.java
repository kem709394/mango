package com.mango.controller;

import com.alibaba.fastjson.JSONObject;
import com.mango.sys.entity.SysUser;
import com.mango.sys.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    protected SysUserService sysUserService;

    private SysUser getUser() {
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }

    protected Long getUserId() {
        return getUser().getId();
    }

    protected JSONObject getUserObj(Long userId){
        JSONObject data=new JSONObject();
        SysUser user=sysUserService.getById(userId);
        data.put("id",user.getId());
        data.put("userName",user.getUserName());
        data.put("nickName",user.getNickName());
        data.put("avatar",user.getAvatar());
        data.put("state",user.getState());
        return data;
    }

}
