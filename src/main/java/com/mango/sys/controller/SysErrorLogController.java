package com.mango.sys.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mango.controller.BaseController;
import com.mango.common.JsonResult;
import com.mango.sys.entity.SysErrorLog;
import com.mango.sys.service.SysErrorLogService;
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
 * @since 2019-07-06
 */
@Controller
@RequestMapping("/v1/sys")
public class SysErrorLogController extends BaseController {

    private final SysErrorLogService sysErrorLogService;

    @Autowired
    public SysErrorLogController(SysErrorLogService sysErrorLogService) {
        this.sysErrorLogService = sysErrorLogService;
    }

    @ApiOperation(value="异常日志分页查询", notes="异常日志分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("error_log")
    @RequiresPermissions("sys:error_log:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysErrorLog> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysErrorLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("path")){
            queryWrapper.like("path","%"+filterItem.getString("path")+"%");
        }
        if(filterItem.containsKey("ipAddress")){
            queryWrapper.like("ip_address","%"+filterItem.getString("ipAddress")+"%");
        }
        if (filterItem.containsKey("dateRange")) {
            JSONArray dateRange=filterItem.getJSONArray("dateRange");
            queryWrapper.between("create_time" ,dateRange.getDate(0),dateRange.getDate(1));
        }
        queryWrapper.orderByDesc("create_time");
        IPage<SysErrorLog> data = sysErrorLogService.page(page, queryWrapper);
        return JsonResult.page(data.getRecords(),data.getTotal());
    }

    @ApiOperation(value="获取异常日志详细", notes="获取异常日志详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "日志ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("error_log/{id}")
    @RequiresPermissions("sys:error_log:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysErrorLog errorLog=sysErrorLogService.getById(id);
        return JsonResult.info(errorLog);
    }

    @ApiOperation(value="删除异常日志实例", notes="删除异常日志实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "ids", value = "日志ID", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @DeleteMapping("error_log")
    @RequiresPermissions("sys:error_log:delete")
    public @ResponseBody
    JSONObject delete(@RequestBody JSONArray ids) {
        List<SysErrorLog> list=new ArrayList<>();
        for(int i=0;i<ids.size();i++){
            SysErrorLog item=new SysErrorLog();
            item.setId(ids.getLong(i));
            item.setIsDeleted(true);
            list.add(item);
        }
        sysErrorLogService.updateBatchById(list);
        return JsonResult.ok();
    }
}

