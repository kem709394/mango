package com.mango.sys.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.sys.entity.SysMenuFunction;
import com.mango.sys.entity.SysRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
public interface SysRoleService extends IService<SysRole> {

    List<SysMenuFunction> getRoleHasMenuFunctions(Long roleId);

    void updateRoleHasMenuFunctions(Long roleId, JSONArray menuFunctionIds);

    JSONArray getRoleHasResources(Long roleId);

    void updateRoleHasResources(Long roleId, JSONArray resources);

    List<Map<String, Object>> getRoleHasResourcesWithFilter(Long roleId);

    List<Map<String, Object>> getRolesHasMenuFunctions(JSONArray roleIds);
}
