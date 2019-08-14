package com.mango.sys.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mango.controller.BaseController;
import com.mango.common.JsonResult;
import com.mango.sys.entity.SysMenuFunction;
import com.mango.sys.entity.SysRole;
import com.mango.sys.service.SysRoleService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
@Controller
@RequestMapping("/v1/sys")
public class SysRoleController extends BaseController {

    private final SysRoleService sysRoleService;

    @Autowired
    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @ApiOperation(value="角色分页查询", notes="角色分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("role")
    @RequiresPermissions("sys:role:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysRole> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("name")){
            queryWrapper.like("name","%"+filterItem.getString("name")+"%");
        }
        if(filterItem.containsKey("code")){
            queryWrapper.like("code","%"+filterItem.getString("code")+"%");
        }
        if(filterItem.containsKey("state")){
            queryWrapper.eq("state",filterItem.getString("state"));
        }
        queryWrapper.orderByAsc("priority");
        IPage<SysRole> data = sysRoleService.page(page, queryWrapper);
        return JsonResult.page(data.getRecords(),data.getTotal());
    }

    @ApiOperation(value="获取角色详细", notes="获取角色详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("role/{id}")
    @RequiresPermissions("sys:role:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysRole role=sysRoleService.getById(id);
        return JsonResult.info(role);
    }

    @ApiOperation(value="添加角色实例", notes="添加角色实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "code", value = "代码", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "state", value = "状态", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "extFields", value = "扩展字段", required = true, dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "priority", value = "优先级", required = true, dataType = "Integer", paramType = "body")
    })
    @PostMapping("role")
    @RequiresPermissions("sys:role:create")
    public @ResponseBody
    JSONObject create(@RequestBody JSONObject params) {
        SysRole role=new SysRole();
        role.setName(params.getString("name"));
        role.setCode(params.getString("code"));
        role.setState(params.getString("state"));
        role.setNote(params.getString("note"));
        role.setExtFields(params.getJSONObject("extFields"));
        role.setPriority(params.getIntValue("priority"));
        role.setCreatorId(getUserId());
        role.setCreateTime(LocalDateTime.now());
        role.setIsDeleted(false);
        sysRoleService.save(role);
        return JsonResult.ok();
    }

    @ApiOperation(value="修改角色实例", notes="修改角色实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "code", value = "代码", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "state", value = "状态", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "extFields", value = "扩展字段", dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "priority", value = "优先级", dataType = "Integer", paramType = "body")
    })
    @PutMapping("role/{id}")
    @RequiresPermissions("sys:role:update")
    public @ResponseBody
    JSONObject update(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        SysRole role=new SysRole();
        role.setId(id);
        if(params.containsKey("name")){
            role.setName(params.getString("name"));
        }
        if(params.containsKey("code")){
            role.setCode(params.getString("code"));
        }
        if(params.containsKey("state")){
            role.setState(params.getString("state"));
        }
        if(params.containsKey("note")){
            role.setNote(params.getString("note"));
        }
        if(params.containsKey("priority")){
            role.setPriority(params.getIntValue("priority"));
        }
        if(params.containsKey("extFields")){
            role.setExtFields(params.getJSONObject("extFields"));
        }
        role.setModifierId(getUserId());
        role.setModifyTime(LocalDateTime.now());
        sysRoleService.updateById(role);
        return JsonResult.ok();
    }

    @ApiOperation(value="删除角色实例", notes="删除角色实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "ids", value = "角色ID", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @DeleteMapping("role")
    @RequiresPermissions("sys:role:delete")
    public @ResponseBody
    JSONObject delete(@RequestBody JSONArray ids) {
        List<SysRole> list=new ArrayList<>();
        for(int i=0;i<ids.size();i++){
            SysRole item=new SysRole();
            item.setId(ids.getLong(i));
            item.setIsDeleted(true);
            list.add(item);
        }
        sysRoleService.updateBatchById(list);
        return JsonResult.ok();
    }

    @SuppressWarnings("Duplicates")
    @ApiOperation(value="获取角色信息列表", notes="获取角色信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("role/list")
    @RequiresPermissions("sys:role:list")
    public @ResponseBody
    JSONObject list(@PathParam("filter")String filter) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("code")){
            queryWrapper.eq("code",filterItem.getString("code"));
        }
        queryWrapper.orderByDesc("create_time");
        List<SysRole> list = sysRoleService.list(queryWrapper);
        JSONArray array=new JSONArray();
        for(SysRole temp:list) {
            JSONObject item=new JSONObject();
            item.put("id", temp.getId());
            item.put("code", temp.getCode());
            item.put("name", temp.getName());
            array.add(item);
        }
        return JsonResult.list(array);
    }

    @ApiOperation(value="获取角色拥有的菜单", notes="获取角色拥有的菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "JSONArray", paramType = "path")
    })
    @GetMapping("role/{id}/menu_function")
    @RequiresPermissions("sys:role:menu_function:detail")
    public @ResponseBody
    JSONObject menu_function(@PathVariable("id") Long id) {
        List<SysMenuFunction> list=sysRoleService.getRoleHasMenuFunctions(id);
        return JsonResult.list(list);
    }

    @ApiOperation(value="更新角色拥有的菜单", notes="更新角色拥有的菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "JSONArray", paramType = "path"),
            @ApiImplicitParam(name = "menuFuncIds", value = "菜单ID", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @PutMapping("role/{id}/menu_function")
    @RequiresPermissions("sys:role:menu_function:update")
    public @ResponseBody
    JSONObject menu_function(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        sysRoleService.updateRoleHasMenuFunctions(id, params.getJSONArray("menuFuncIds"));
        return JsonResult.ok();
    }

    @ApiOperation(value="获取角色拥有的资源", notes="获取角色拥有的资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "JSONArray", paramType = "path")
    })
    @GetMapping("role/{id}/resource")
    @RequiresPermissions("sys:role:resource:detail")
    public @ResponseBody
    JSONObject resource(@PathVariable("id") Long id) {
        JSONArray list=sysRoleService.getRoleHasResources(id);
        return JsonResult.list(list);
    }

    @ApiOperation(value="更新角色拥有的资源", notes="更新角色拥有的资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "JSONArray", paramType = "path"),
            @ApiImplicitParam(name = "resources", value = "资源", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @PutMapping("role/{id}/resource")
    @RequiresPermissions("sys:role:resource:update")
    public @ResponseBody
    JSONObject resource(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        sysRoleService.updateRoleHasResources(id, params.getJSONArray("resources"));
        return JsonResult.ok();
    }
}

