package com.mango.sys.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.sys.entity.SysRole;
import com.mango.sys.entity.SysUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
public interface SysUserService extends IService<SysUser> {

    List<SysRole> getUserHasRoles(Long userId);

    void updateUserHasRoles(Long userId, JSONArray roleIds);

}
