package com.mango.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mango.sys.entity.SysRoleHasSysResource;
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
public interface SysRoleHasSysResourceMapper extends BaseMapper<SysRoleHasSysResource> {

    @Select("SELECT a.id,a.name,a.code,a.state,b.filter FROM sys_resource a INNER JOIN sys_role_has_sys_resource b ON a.id = b.sys_resource_id WHERE a.is_deleted=false AND b.sys_role_id = (#{roleId})")
    List<Map<String, Object>> getResources(Long roleId);

}
