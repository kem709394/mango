package com.mango.sys.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mango.controller.BaseController;
import com.mango.common.JsonResult;
import com.mango.sys.entity.SysDictionary;
import com.mango.sys.service.SysDictionaryService;
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
public class SysDictionaryController extends BaseController {

    private final SysDictionaryService sysDictionaryService;

    @Autowired
    public SysDictionaryController(SysDictionaryService sysDictionaryService) {
        this.sysDictionaryService = sysDictionaryService;
    }

    @ApiOperation(value="字典分页查询", notes="字典分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("dictionary")
    @RequiresPermissions("sys:dictionary:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysDictionary> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysDictionary> queryWrapper = new QueryWrapper<>();
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
        IPage<SysDictionary> data = sysDictionaryService.page(page, queryWrapper);
        return JsonResult.page(data.getRecords(), data.getTotal());
    }

    @ApiOperation(value="获取字典详细", notes="获取字典详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "字典ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("dictionary/{id}")
    @RequiresPermissions("sys:dictionary:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysDictionary dictionary=sysDictionaryService.getById(id);
        return JsonResult.info(dictionary);
    }

    @ApiOperation(value="添加字典实例", notes="添加字典实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "code", value = "代码", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "options", value = "选项", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "priority", value = "优先级", required = true, dataType = "Integer", paramType = "body")
    })
    @PostMapping("dictionary")
    @RequiresPermissions("sys:dictionary:create")
    public @ResponseBody
    JSONObject create(@RequestBody JSONObject params) {
        SysDictionary dictionary=new SysDictionary();
        dictionary.setName(params.getString("name"));
        dictionary.setCode(params.getString("code"));
        dictionary.setType(params.getString("type"));
        dictionary.setOptions(params.getJSONArray("options"));
        dictionary.setPriority(params.getIntValue("priority"));
        dictionary.setNote(params.getString("note"));
        dictionary.setCreatorId(getUserId());
        dictionary.setCreateTime(LocalDateTime.now());
        dictionary.setIsDeleted(false);
        sysDictionaryService.save(dictionary);
        return JsonResult.ok();
    }

    @ApiOperation(value="修改字典实例", notes="修改字典实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "字典ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "code", value = "代码", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "options", value = "选项", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "priority", value = "优先级", dataType = "Integer", paramType = "body")
    })
    @PutMapping("dictionary/{id}")
    @RequiresPermissions("sys:dictionary:update")
    public @ResponseBody
    JSONObject update(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        SysDictionary dictionary=new SysDictionary();
        dictionary.setId(id);
        if(params.containsKey("name")){
            dictionary.setName(params.getString("name"));
        }
        if(params.containsKey("code")){
            dictionary.setCode(params.getString("code"));
        }
        if(params.containsKey("type")){
            dictionary.setType(params.getString("type"));
        }
        if(params.containsKey("options")){
            dictionary.setOptions(params.getJSONArray("options"));
        }
        if(params.containsKey("priority")){
            dictionary.setPriority(params.getIntValue("priority"));
        }
        if(params.containsKey("note")){
            dictionary.setNote(params.getString("note"));
        }
        dictionary.setModifierId(getUserId());
        dictionary.setModifyTime(LocalDateTime.now());
        sysDictionaryService.updateById(dictionary);
        return JsonResult.ok();
    }

    @ApiOperation(value="删除字典实例", notes="删除字典实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "ids", value = "字典ID", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @DeleteMapping("dictionary")
    @RequiresPermissions("sys:dictionary:delete")
    public @ResponseBody
    JSONObject delete(@RequestBody JSONArray ids) {
        List<SysDictionary> list=new ArrayList<>();
        for(int i=0;i<ids.size();i++){
            SysDictionary item=new SysDictionary();
            item.setId(ids.getLong(i));
            item.setIsDeleted(true);
            list.add(item);
        }
        sysDictionaryService.updateBatchById(list);
        return JsonResult.ok();
    }

    @SuppressWarnings("Duplicates")
    @ApiOperation(value="获取字典信息列表", notes="获取字典信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("dictionary/list")
    @RequiresPermissions("sys:dictionary:list")
    public @ResponseBody
    JSONObject list(@PathParam("filter")String filter) {
        QueryWrapper<SysDictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("code")){
            queryWrapper.eq("code",filterItem.getString("code"));
        }
        if(filterItem.containsKey("type")){
            queryWrapper.eq("type",filterItem.getString("type"));
        }
        queryWrapper.orderByDesc("priority");
        List<SysDictionary> list = sysDictionaryService.list(queryWrapper);
        JSONArray array=new JSONArray();
        for(SysDictionary temp:list) {
            JSONObject item=new JSONObject();
            item.put("id", temp.getId());
            item.put("code", temp.getCode());
            item.put("name", temp.getName());
            array.add(item);
        }
        return JsonResult.list(array);
    }

}

