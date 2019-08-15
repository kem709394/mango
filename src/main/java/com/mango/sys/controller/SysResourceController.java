package com.mango.sys.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mango.controller.BaseController;
import com.mango.common.JsonResult;
import com.mango.sys.entity.SysResource;
import com.mango.sys.service.SysResourceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
public class SysResourceController extends BaseController {

    private final SysResourceService sysResourceService;

    @Autowired
    public SysResourceController(SysResourceService sysResourceService) {
        this.sysResourceService = sysResourceService;
    }

    @ApiOperation(value="资源分页查询", notes="资源分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("resource")
    @RequiresPermissions("sys:resource:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysResource> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<>();
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
        IPage<SysResource> data = sysResourceService.page(page, queryWrapper);
        return JsonResult.page(data.getRecords(),data.getTotal());
    }

    @ApiOperation(value="获取资源详细", notes="获取资源详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "资源ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("resource/{id}")
    @RequiresPermissions("sys:resource:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysResource resource=sysResourceService.getById(id);
        return JsonResult.info(resource);
    }

    @ApiOperation(value="添加资源实例", notes="添加资源实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "code", value = "代码", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "path", value = "路径", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "method", value = "方法", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "mapClass", value = "类型", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "attributes", value = "属性", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "state", value = "状态", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "extFields", value = "扩展字段", required = true, dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "priority", value = "优先级", required = true, dataType = "Integer", paramType = "body")
    })
    @PostMapping("resource")
    @RequiresPermissions("sys:resource:create")
    public @ResponseBody
    JSONObject create(@RequestBody JSONObject params) {
        SysResource resource=new SysResource();
        resource.setCode(params.getString("code"));
        resource.setName(params.getString("name"));
        resource.setPath(params.getString("path"));
        resource.setMethod(params.getString("method"));
        resource.setMapClass(params.getString("mapClass"));
        resource.setAttributes(params.getJSONArray("attributes"));
        resource.setExtFields(params.getJSONObject("extFields"));
        resource.setState(params.getString("state"));
        resource.setNote(params.getString("note"));
        resource.setPriority(params.getIntValue("priority"));
        resource.setCreatorId(getUserId());
        resource.setCreateTime(LocalDateTime.now());
        resource.setIsDeleted(false);
        sysResourceService.save(resource);
        return JsonResult.ok();
    }

    @ApiOperation(value="添加资源实例", notes="添加资源实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "资源ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "code", value = "代码", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "path", value = "路径", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "method", value = "方法", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "mapClass", value = "类型", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "attributes", value = "属性", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "state", value = "状态", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "extFields", value = "扩展字段", dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "priority", value = "优先级", dataType = "Integer", paramType = "body")
    })
    @PutMapping("resource/{id}")
    @RequiresPermissions("sys:resource:update")
    public @ResponseBody
    JSONObject update(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        SysResource resource=new SysResource();
        resource.setId(id);
        if(params.containsKey("code")){
            resource.setCode(params.getString("code"));
        }
        if(params.containsKey("name")){
            resource.setName(params.getString("name"));
        }
        if(params.containsKey("path")){
            resource.setPath(params.getString("path"));
        }
        if(params.containsKey("method")){
            resource.setMethod(params.getString("method"));
        }
        if(params.containsKey("mapClass")){
            resource.setMapClass(params.getString("mapClass"));
        }
        if(params.containsKey("attributes")){
            resource.setAttributes(params.getJSONArray("attributes"));
        }
        if(params.containsKey("state")){
            resource.setState(params.getString("state"));
        }
        if(params.containsKey("note")){
            resource.setNote(params.getString("note"));
        }
        if(params.containsKey("priority")){
            resource.setPriority(params.getIntValue("priority"));
        }
        if(params.containsKey("extFields")){
            resource.setExtFields(params.getJSONObject("extFields"));
        }
        resource.setModifierId(getUserId());
        resource.setModifyTime(LocalDateTime.now());
        sysResourceService.updateById(resource);
        return JsonResult.ok();
    }

    @ApiOperation(value="删除资源实例", notes="删除资源实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "ids", value = "资源ID", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @DeleteMapping("resource")
    @RequiresPermissions("sys:resource:delete")
    public @ResponseBody
    JSONObject delete(@RequestBody JSONArray ids) {
        List<SysResource> list=new ArrayList<>();
        for(int i=0;i<ids.size();i++){
            SysResource item=new SysResource();
            item.setId(ids.getLong(i));
            item.setIsDeleted(true);
            list.add(item);
        }
        sysResourceService.updateBatchById(list);
        return JsonResult.ok();
    }

    @ApiOperation(value="获取资源信息列表", notes="获取资源信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("resource/list")
    @RequiresPermissions("sys:resource:list")
    public @ResponseBody
    JSONObject list(@PathParam("filter")String filter) throws Exception {
        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("code")){
            queryWrapper.eq("code",filterItem.getString("code"));
        }
        queryWrapper.orderByDesc("priority");
        List<SysResource> list = sysResourceService.list(queryWrapper);
        JSONArray array=new JSONArray();
        for(SysResource temp:list) {
            JSONObject item=new JSONObject();
            item.put("id", temp.getId());
            item.put("code", temp.getCode());
            item.put("name", temp.getName());
            item.put("path", temp.getPath());
            item.put("method", temp.getMethod());
            JSONArray attributes = temp.getAttributes();
            if(StringUtils.isNotBlank(temp.getMapClass())){
                Class type=Class.forName(temp.getMapClass());
                Field[] fields = type.getDeclaredFields();
                Method[] methods = type.getMethods();
                for(Field field:fields){
                    String fieldName=field.getName();
                    for(Method method:methods){
                        String methodName=method.getName();
                        if(PropertyNamer.isProperty(methodName)){
                            if(PropertyNamer.methodToProperty(methodName).equals(fieldName)){
                                if(!fieldName.equals("isDeleted")&&!attributes.contains(fieldName)){
                                    attributes.add(fieldName);
                                }
                                break;
                            }
                        }
                    }
                }
            }
            item.put("attributes", attributes);
            array.add(item);
        }
        return JsonResult.list(array);
    }
}

