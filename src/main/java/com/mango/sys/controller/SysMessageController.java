package com.mango.sys.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mango.common.DictConstants;
import com.mango.controller.BaseController;
import com.mango.common.JsonResult;
import com.mango.sys.entity.SysMessage;
import com.mango.sys.service.SysMessageService;
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
public class SysMessageController extends BaseController {

    private final SysMessageService sysMessageService;

    @Autowired
    public SysMessageController(SysMessageService sysMessageService) {
        this.sysMessageService = sysMessageService;
    }

    @ApiOperation(value="消息分页查询", notes="消息分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("message")
    @RequiresPermissions("sys:message:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysMessage> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("title")){
            queryWrapper.like("title","%"+filterItem.getString("title")+"%");
        }
        if(filterItem.containsKey("type")){
            queryWrapper.eq("type",filterItem.getString("type"));
        }
        if (filterItem.containsKey("dateRange")) {
            JSONArray dateRange=filterItem.getJSONArray("dateRange");
            queryWrapper.between("create_time" ,dateRange.getDate(0),dateRange.getDate(1));
        }
        queryWrapper.orderByAsc("create_time");
        IPage<SysMessage> data = sysMessageService.page(page, queryWrapper);
        JSONArray records = (JSONArray)JSON.toJSON(data.getRecords());
        for(Object obj: records){
            JSONObject item=(JSONObject)obj;
            item.put("user", getUserObj(item.getLong("sysUserId")));
        }
        return JsonResult.page(records,data.getTotal());
    }

    @ApiOperation(value="获取消息详细", notes="获取消息详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "消息ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("message/{id}")
    @RequiresPermissions("sys:message:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysMessage message=sysMessageService.getById(id);
        JSONObject info = (JSONObject)JSON.toJSON(message);
        info.put("user", getUserObj(message.getSysUserId()));
        return JsonResult.info(info);
    }

    @ApiOperation(value="添加消息实例", notes="添加消息实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "title", value = "标题", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "extFields", value = "扩展字段", required = true, dataType = "JSONArray", paramType = "body")
    })
    @PostMapping("message")
    @RequiresPermissions("sys:message:create")
    public @ResponseBody
    JSONObject create(@RequestBody JSONObject params) {
        JSONArray toUsers = params.getJSONArray("toUsers");
        for(int i=0;i<toUsers.size();i++){
            SysMessage message=new SysMessage();
            message.setType(DictConstants.MESSAGE_TYPE_SYS);
            message.setContent(params.getString("content"));
            message.setTitle(params.getString("title"));
            message.setExtFields(params.getJSONObject("extFields"));
            message.setNote(params.getString("note"));
            message.setCreatorId(getUserId());
            message.setCreateTime(LocalDateTime.now());
            message.setIsDeleted(false);
            message.setState(DictConstants.MESSAGE_STATE_NRD);
            message.setSysUserId(toUsers.getLong(i));
            sysMessageService.save(message);
        }
        return JsonResult.ok();
    }

    @ApiOperation(value="修改消息实例", notes="修改消息实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "消息ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "title", value = "标题", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "content", value = "内容", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "extFields", value = "扩展字段", dataType = "JSONArray", paramType = "body")
    })
    @PutMapping("message/{id}")
    @RequiresPermissions("sys:message:update")
    public @ResponseBody
    JSONObject update(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        SysMessage message=new SysMessage();
        message.setId(id);
        if(params.containsKey("content")){
            message.setContent(params.getString("content"));
        }
        if(params.containsKey("title")){
            message.setTitle(params.getString("title"));
        }
        if(params.containsKey("extFields")){
            message.setExtFields(params.getJSONObject("extFields"));
        }
        if(params.containsKey("note")){
            message.setNote(params.getString("note"));
        }
        message.setModifierId(getUserId());
        message.setModifyTime(LocalDateTime.now());
        sysMessageService.updateById(message);
        return JsonResult.ok();
    }

    @ApiOperation(value="删除消息实例", notes="删除消息实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "ids", value = "消息ID", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @DeleteMapping("message")
    @RequiresPermissions("sys:message:delete")
    public @ResponseBody
    JSONObject delete(@RequestBody JSONArray ids) {
        List<SysMessage> list=new ArrayList<>();
        for(int i=0;i<ids.size();i++){
            SysMessage item=new SysMessage();
            item.setId(ids.getLong(i));
            item.setIsDeleted(true);
            list.add(item);
        }
        sysMessageService.updateBatchById(list);
        return JsonResult.ok();
    }
}

