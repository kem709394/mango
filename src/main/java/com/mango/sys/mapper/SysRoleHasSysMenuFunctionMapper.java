package com.mango.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mango.sys.entity.SysRoleHasSysMenuFunction;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
public interface SysRoleHasSysMenuFunctionMapper extends BaseMapper<SysRoleHasSysMenuFunction> {

    @Select("SELECT a.id,a.name,a.code,a.state FROM sys_menu_function a INNER JOIN sys_role_has_sys_menu_function b ON a.id = b.sys_menu_function_id WHERE a.is_deleted=false AND b.sys_role_id IN (#{idsStr})")
    List<Map<String, Object>> getMenuFunctions(String idsStr);

}
