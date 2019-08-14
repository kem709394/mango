package com.mango.sys.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mango.common.JsonResult;
import com.mango.controller.BaseController;
import com.mango.sys.entity.SysLoginLog;
import com.mango.sys.service.SysLoginLogService;
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
public class SysLoginLogController extends BaseController {

    private final SysLoginLogService sysLoginLogService;

    @Autowired
    public SysLoginLogController(SysLoginLogService sysLoginLogService) {
        this.sysLoginLogService = sysLoginLogService;
    }

    @ApiOperation(value="登录日志分页查询", notes="登录日志分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("login_log")
    @RequiresPermissions("sys:login_log:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysLoginLog> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("userName")){
            queryWrapper.like("user_name","%"+filterItem.getString("userName")+"%");
        }
        if(filterItem.containsKey("ipAddress")){
            queryWrapper.like("ip_address","%"+filterItem.getString("ipAddress")+"%");
        }
        if (filterItem.containsKey("dateRange")) {
            JSONArray dateRange=filterItem.getJSONArray("dateRange");
            queryWrapper.between("login_time" ,dateRange.getDate(0),dateRange.getDate(1));
        }
        queryWrapper.orderByDesc("login_time");
        IPage<SysLoginLog> data = sysLoginLogService.page(page, queryWrapper);
        return JsonResult.page(data.getRecords(),data.getTotal());
    }

    @ApiOperation(value="获取登录日志详细", notes="获取登录日志详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "日志ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("login_log/{id}")
    @RequiresPermissions("sys:login_log:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysLoginLog loginLog=sysLoginLogService.getById(id);
        return JsonResult.info(loginLog);
    }

    @ApiOperation(value="删除登录日志实例", notes="删除登录日志实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "ids", value = "日志ID", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @DeleteMapping("login_log")
    @RequiresPermissions("sys:login_log:delete")
    public @ResponseBody
    JSONObject delete(@RequestBody JSONArray ids) {
        List<SysLoginLog> list=new ArrayList<>();
        for(int i=0;i<ids.size();i++){
            SysLoginLog item=new SysLoginLog();
            item.setId(ids.getLong(i));
            item.setIsDeleted(true);
            list.add(item);
        }
        sysLoginLogService.updateBatchById(list);
        return JsonResult.ok();
    }
}

