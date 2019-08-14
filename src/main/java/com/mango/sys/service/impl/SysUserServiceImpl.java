package com.mango.sys.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.sys.entity.SysRole;
import com.mango.sys.entity.SysUser;
import com.mango.sys.entity.SysUserHasSysRole;
import com.mango.sys.mapper.SysRoleMapper;
import com.mango.sys.mapper.SysUserHasSysRoleMapper;
import com.mango.sys.mapper.SysUserMapper;
import com.mango.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysRoleMapper sysRoleMapper;

    private final SysUserHasSysRoleMapper sysUserHasSysRoleMapper;

    @Autowired
    public SysUserServiceImpl(SysRoleMapper sysRoleMapper, SysUserHasSysRoleMapper sysUserHasSysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
        this.sysUserHasSysRoleMapper = sysUserHasSysRoleMapper;
    }

    @Override
    public List<SysRole> getUserHasRoles(Long userId) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", false);
        queryWrapper.inSql("id", "select sys_role_id from sys_user_has_sys_role where sys_user_id="+userId);
        queryWrapper.orderByAsc("priority");
        return sysRoleMapper.selectList(queryWrapper);
    }

    @Override
    public void updateUserHasRoles(Long userId, JSONArray roleIds) {
        QueryWrapper<SysUserHasSysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sys_user_id", userId);
        if(roleIds.size()>0){
            queryWrapper.notIn("sys_role_id", roleIds);
        }
        sysUserHasSysRoleMapper.delete(queryWrapper);
        for(int i=0;i<roleIds.size();i++) {
            Long roleId = roleIds.getLong(i);
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sys_user_id", userId);
            queryWrapper.eq("sys_role_id", roleId);
            if(sysUserHasSysRoleMapper.selectCount(queryWrapper)==0){
                SysUserHasSysRole item=new SysUserHasSysRole();
                item.setSysUserId(userId);
                item.setSysRoleId(roleId);
                sysUserHasSysRoleMapper.insert(item);
            }
        }
    }
}
