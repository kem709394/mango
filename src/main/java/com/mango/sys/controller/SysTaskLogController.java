package com.mango.sys.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mango.common.JsonResult;
import com.mango.sys.entity.SysTaskLog;
import com.mango.sys.service.SysTaskLogService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kem
 * @since 2019-07-31
 */
@Controller
@RequestMapping("/v1/sys")
public class SysTaskLogController {

    private final SysTaskLogService sysTaskLogService;

    @Autowired
    public SysTaskLogController(SysTaskLogService sysTaskLogService) {
        this.sysTaskLogService = sysTaskLogService;
    }

    @ApiOperation(value="任务日志分页查询", notes="任务日志分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("task_log")
    @RequiresPermissions("sys:task_log:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysTaskLog> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysTaskLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("bean")){
            queryWrapper.eq("bean",filterItem.getString("bean"));
        }
        if (filterItem.containsKey("dateRange")) {
            JSONArray dateRange=filterItem.getJSONArray("dateRange");
            queryWrapper.between("create_time" ,dateRange.getDate(0),dateRange.getDate(1));
        }
        queryWrapper.orderByDesc("create_time");
        IPage<SysTaskLog> data = sysTaskLogService.page(page, queryWrapper);
        return JsonResult.page(data.getRecords(),data.getTotal());
    }

    @ApiOperation(value="获取任务日志详细", notes="获取任务日志详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "日志ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("task_log/{id}")
    @RequiresPermissions("sys:task_log:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysTaskLog taskLog=sysTaskLogService.getById(id);
        return JsonResult.info(taskLog);
    }

    @ApiOperation(value="删除任务日志实例", notes="删除任务日志实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "ids", value = "日志ID", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @DeleteMapping("task_log")
    @RequiresPermissions("sys:task_log:delete")
    public @ResponseBody
    JSONObject delete(@RequestBody JSONArray ids) {
        List<SysTaskLog> list=new ArrayList<>();
        for(int i=0;i<ids.size();i++){
            SysTaskLog item=new SysTaskLog();
            item.setId(ids.getLong(i));
            item.setIsDeleted(true);
            list.add(item);
        }
        sysTaskLogService.updateBatchById(list);
        return JsonResult.ok();
    }
}

