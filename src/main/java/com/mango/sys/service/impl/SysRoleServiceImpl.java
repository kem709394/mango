package com.mango.sys.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.sys.entity.SysMenuFunction;
import com.mango.sys.entity.SysRole;
import com.mango.sys.entity.SysRoleHasSysMenuFunction;
import com.mango.sys.entity.SysRoleHasSysResource;
import com.mango.sys.mapper.SysMenuFunctionMapper;
import com.mango.sys.mapper.SysRoleHasSysMenuFunctionMapper;
import com.mango.sys.mapper.SysRoleHasSysResourceMapper;
import com.mango.sys.mapper.SysRoleMapper;
import com.mango.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysMenuFunctionMapper sysMenuFunctionMapper;

    private final SysRoleHasSysMenuFunctionMapper sysRoleHasSysMenuFunctionMapper;

    private final SysRoleHasSysResourceMapper sysRoleHasSysResourceMapper;

    @Autowired
    public SysRoleServiceImpl(SysMenuFunctionMapper sysMenuFunctionMapper, SysRoleHasSysMenuFunctionMapper sysRoleHasSysMenuFunctionMapper, SysRoleHasSysResourceMapper sysRoleHasSysResourceMapper) {
        this.sysMenuFunctionMapper = sysMenuFunctionMapper;
        this.sysRoleHasSysMenuFunctionMapper = sysRoleHasSysMenuFunctionMapper;
        this.sysRoleHasSysResourceMapper = sysRoleHasSysResourceMapper;
    }

    @Override
    public List<SysMenuFunction> getRoleHasMenuFunctions(Long roleId) {
        QueryWrapper<SysMenuFunction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", false);
        queryWrapper.inSql("id", "select sys_menu_function_id from sys_role_has_sys_menu_function where sys_role_id="+roleId);
        return sysMenuFunctionMapper.selectList(queryWrapper);
    }

    @Override
    public void updateRoleHasMenuFunctions(Long roleId, JSONArray menuFunctionIds) {
        QueryWrapper<SysRoleHasSysMenuFunction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sys_role_id", roleId);
        if(menuFunctionIds.size()>0){
            queryWrapper.notIn("sys_menu_function_id", menuFunctionIds);
        }
        sysRoleHasSysMenuFunctionMapper.delete(queryWrapper);
        for(int i=0;i<menuFunctionIds.size();i++) {
            Long menuFuncId = menuFunctionIds.getLong(i);
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sys_role_id", roleId);
            queryWrapper.eq("sys_menu_function_id", menuFuncId);
            if(sysRoleHasSysMenuFunctionMapper.selectCount(queryWrapper)==0){
                SysRoleHasSysMenuFunction item=new SysRoleHasSysMenuFunction();
                item.setSysRoleId(roleId);
                item.setSysMenuFunctionId(menuFuncId);
                sysRoleHasSysMenuFunctionMapper.insert(item);
            }
        }
    }

    @Override
    public JSONArray getRoleHasResources(Long roleId) {
        JSONArray data=new JSONArray();
        QueryWrapper<SysRoleHasSysResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sys_role_id", roleId);
        List<SysRoleHasSysResource> list=sysRoleHasSysResourceMapper.selectList(queryWrapper);
        for(SysRoleHasSysResource temp:list){
            JSONObject item=new JSONObject();
            item.put("id", temp.getSysResourceId());
            item.put("filter", temp.getFilter());
            data.add(item);
        }
        return data;
    }

    @Override
    public void updateRoleHasResources(Long roleId, JSONArray resources) {
        QueryWrapper<SysRoleHasSysResource> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("sys_role_id", roleId);
        JSONArray ids = new JSONArray();
        for(int i=0;i<resources.size();i++) {
            JSONObject obj=resources.getJSONObject(i);
            Long id = obj.getLong("id");
            QueryWrapper<SysRoleHasSysResource> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("sys_role_id", roleId);
            queryWrapper2.eq("sys_resource_id", id);
            SysRoleHasSysResource item=new SysRoleHasSysResource();
            item.setSysRoleId(roleId);
            item.setSysResourceId(id);
            item.setFilter(obj.getJSONObject("filter"));
            if(sysRoleHasSysResourceMapper.selectCount(queryWrapper2)==0){
                sysRoleHasSysResourceMapper.insert(item);
            }else{
                sysRoleHasSysResourceMapper.update(item,queryWrapper2);
            }
            ids.add(id);
        }
        if(ids.size()>0){
            queryWrapper1.notIn("sys_resource_id", ids);
        }
        sysRoleHasSysResourceMapper.delete(queryWrapper1);
    }

    @Override
    public List<Map<String, Object>> getRoleHasResourcesWithFilter(Long roleId) {
        return sysRoleHasSysResourceMapper.getResources(roleId);
    }

    @Override
    public List<Map<String, Object>> getRolesHasMenuFunctions(JSONArray roleIds) {
        StringBuilder idsStr = new StringBuilder();
        for(int i=0;i<roleIds.size(); i++){
            if(i==0){
                idsStr.append(roleIds.getString(i));
            }else{
                idsStr.append(",").append(roleIds.getString(i));
            }
        }
        return sysRoleHasSysMenuFunctionMapper.getMenuFunctions(idsStr.toString());
    }
}
