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
import com.mango.sys.service.SysMenuFunctionService;
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
public class SysMenuFunctionController extends BaseController {

    private final SysMenuFunctionService sysMenuFunctionService;

    @Autowired
    public SysMenuFunctionController(SysMenuFunctionService sysMenuFunctionService) {
        this.sysMenuFunctionService = sysMenuFunctionService;
    }

    @ApiOperation(value="菜单分页查询", notes="菜单分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("menu_function")
    @RequiresPermissions("sys:menu_function:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysMenuFunction> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysMenuFunction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("name")){
            queryWrapper.like("name","%"+filterItem.getString("name")+"%");
        }
        if(filterItem.containsKey("code")){
            queryWrapper.like("code","%"+filterItem.getString("code")+"%");
        }
        if(filterItem.containsKey("type")){
            queryWrapper.eq("type",filterItem.getString("type"));
        }
        queryWrapper.orderByAsc("priority");
        IPage<SysMenuFunction> data = sysMenuFunctionService.page(page, queryWrapper);
        return JsonResult.page(data.getRecords(),data.getTotal());
    }

    @ApiOperation(value="获取菜单详细", notes="获取菜单详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "菜单ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("menu_function/{id}")
    @RequiresPermissions("sys:menu_function:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysMenuFunction menuFunction=sysMenuFunctionService.getById(id);
        JSONObject info = (JSONObject)JSON.toJSON(menuFunction);
        if(menuFunction.getPid().intValue()>0){
            info.put("parent", getObject(menuFunction.getPid()));
        }
        return JsonResult.info(info);
    }

    @ApiOperation(value="添加菜单实例", notes="添加菜单实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "code", value = "代码", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "parent", value = "上级", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "state", value = "状态", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "extFields", value = "扩展字段", required = true, dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "priority", value = "优先级", required = true, dataType = "Integer", paramType = "body")
    })
    @PostMapping("menu_function")
    @RequiresPermissions("sys:menu_function:create")
    public @ResponseBody
    JSONObject create(@RequestBody JSONObject params) {
        SysMenuFunction menuFunction=new SysMenuFunction();
        menuFunction.setName(params.getString("name"));
        menuFunction.setCode(params.getString("code"));
        menuFunction.setType(params.getString("type"));
        menuFunction.setState(params.getString("state"));
        menuFunction.setPid(params.getLong("parent"));
        menuFunction.setNote(params.getString("note"));
        menuFunction.setExtFields(params.getJSONObject("extFields"));
        menuFunction.setPriority(params.getIntValue("priority"));
        menuFunction.setCreatorId(getUserId());
        menuFunction.setCreateTime(LocalDateTime.now());
        menuFunction.setIsDeleted(false);
        sysMenuFunctionService.save(menuFunction);
        return JsonResult.ok();
    }

    @ApiOperation(value="修改菜单实例", notes="修改菜单实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "菜单ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "code", value = "代码", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "parent", value = "上级", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "state", value = "状态", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "extFields", value = "扩展字段", dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "priority", value = "优先级", dataType = "Integer", paramType = "body")
    })
    @PutMapping("menu_function/{id}")
    @RequiresPermissions("sys:menu_function:update")
    public @ResponseBody
    JSONObject update(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        SysMenuFunction menuFunction=new SysMenuFunction();
        menuFunction.setId(id);
        if(params.containsKey("name")){
            menuFunction.setName(params.getString("name"));
        }
        if(params.containsKey("code")){
            menuFunction.setCode(params.getString("code"));
        }
        if(params.containsKey("type")){
            menuFunction.setType(params.getString("type"));
        }
        if(params.containsKey("parent")){
            menuFunction.setPid(params.getLong("parent"));
        }
        if(params.containsKey("state")){
            menuFunction.setState(params.getString("state"));
        }
        if(params.containsKey("note")){
            menuFunction.setNote(params.getString("note"));
        }
        if(params.containsKey("priority")){
            menuFunction.setPriority(params.getIntValue("priority"));
        }
        if(params.containsKey("extFields")){
            menuFunction.setExtFields(params.getJSONObject("extFields"));
        }
        menuFunction.setModifierId(getUserId());
        menuFunction.setModifyTime(LocalDateTime.now());
        sysMenuFunctionService.updateById(menuFunction);
        return JsonResult.ok();
    }

    @ApiOperation(value="删除菜单实例", notes="删除菜单实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "菜单ID", required = true, dataType = "Long", paramType = "path"),
    })
    @DeleteMapping("menu_function/{id}")
    @RequiresPermissions("sys:menu_function:delete")
    public @ResponseBody
    JSONObject delete(@PathVariable("id") Long id) {
        SysMenuFunction menuFunction=new SysMenuFunction();
        menuFunction.setId(id);
        menuFunction.setIsDeleted(true);
        sysMenuFunctionService.updateById(menuFunction);
        return JsonResult.ok();
    }

    @SuppressWarnings("Duplicates")
    @ApiOperation(value="获取菜单信息列表", notes="获取菜单信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("menu_function/list")
    @RequiresPermissions("sys:menu_function:list")
    public @ResponseBody
    JSONObject list(@PathParam("filter")String filter) {
        QueryWrapper<SysMenuFunction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("code")){
            queryWrapper.eq("code",filterItem.getString("code"));
        }
        queryWrapper.orderByDesc("create_time");
        List<SysMenuFunction> list = sysMenuFunctionService.list(queryWrapper);
        JSONArray array=new JSONArray();
        for(SysMenuFunction temp:list) {
            JSONObject item=new JSONObject();
            item.put("id", temp.getId());
            item.put("code", temp.getCode());
            item.put("name", temp.getName());
            item.put("state", temp.getState());
            array.add(item);
        }
        return JsonResult.list(array);
    }

    @ApiOperation(value="获取菜单树形结构", notes="获取菜单树形结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("menu_function/tree")
    @RequiresPermissions("sys:menu_function:tree")
    public @ResponseBody
    JSONObject tree(@PathParam("filter")String filter) {
        JSONArray checkedIds = new JSONArray();
        QueryWrapper<SysMenuFunction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("checked")){
            checkedIds=filterItem.getJSONArray("checked");
        }
        if(filterItem.containsKey("type")){
            queryWrapper.eq("type",filterItem.getString("type"));
        }
        queryWrapper.orderByAsc("priority");
        List<SysMenuFunction> list = sysMenuFunctionService.list(queryWrapper);
        return JsonResult.ok("tree", getChildren(0L, list, checkedIds));
    }

    private JSONArray getChildren(Long id, List<SysMenuFunction> list, JSONArray checkedIds) {
        JSONArray array=new JSONArray();
        for(SysMenuFunction temp:list) {
            if(temp.getPid().intValue()==id.intValue()){
                JSONObject item=new JSONObject();
                item.put("id", temp.getId());
                item.put("code", temp.getCode());
                item.put("title", temp.getName());
                item.put("type", temp.getType());
                item.put("state", temp.getState());
                item.put("note", temp.getNote());
                item.put("children", getChildren(temp.getId(), list, checkedIds));
                if(checkedIds.contains(temp.getId().intValue())){
                    item.put("checked", true);
                }
                array.add(item);
            }
        }
        return array;
    }

    private JSONObject getObject(Long id) {
        SysMenuFunction menuFunction=sysMenuFunctionService.getById(id);
        return (JSONObject)JSON.toJSON(menuFunction);
    }
}

