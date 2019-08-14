package com.mango.sys.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mango.common.ConfigUtils;
import com.mango.controller.BaseController;
import com.mango.common.JsonResult;
import com.mango.sys.entity.SysConfig;
import com.mango.sys.service.SysConfigService;
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
public class SysConfigController extends BaseController {

    private final SysConfigService sysConfigService;

    @Autowired
    public SysConfigController(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    @ApiOperation(value="参数分页查询", notes="参数分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("config")
    @RequiresPermissions("sys:config:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysConfig> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("name")){
            queryWrapper.like("name","%"+filterItem.getString("name")+"%");
        }
        if(filterItem.containsKey("code")){
            queryWrapper.like("code","%"+filterItem.getString("code")+"%");
        }
        queryWrapper.orderByAsc("priority");
        IPage<SysConfig> data = sysConfigService.page(page, queryWrapper);
        return JsonResult.page(data.getRecords(),data.getTotal());
    }

    @ApiOperation(value="获取参数详细", notes="获取参数详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "参数ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("config/{id}")
    @RequiresPermissions("sys:config:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysConfig config=sysConfigService.getById(id);
        return JsonResult.info(config);
    }

    @ApiOperation(value="修改参数实例", notes="修改参数实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "参数ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "content", value = "内容", dataType = "JSONArray", paramType = "body")
    })
    @PutMapping("config/{id}")
    @RequiresPermissions("sys:config:update")
    public @ResponseBody
    JSONObject update(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        SysConfig config=new SysConfig();
        config.setId(id);
        if(params.containsKey("content")){
            config.setContent(params.getJSONObject("content"));
        }
        config.setModifierId(getUserId());
        config.setModifyTime(LocalDateTime.now());
        sysConfigService.updateById(config);
        ConfigUtils.update();
        return JsonResult.ok();
    }

    @ApiOperation(value="获取参数内容", notes="获取参数内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "code", value = "参数代码", required = true, dataType = "Long", paramType = "query")
    })
    @GetMapping("config/content")
    @RequiresPermissions("sys:config:content:detail")
    public @ResponseBody
    JSONObject content(@PathParam("code") String code) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",code);
        SysConfig config=sysConfigService.getOne(queryWrapper);
        if(config==null){
            return JsonResult.fail("参数不存在");
        }
        return JsonResult.info(config.getContent());
    }

    @ApiOperation(value="修改参数内容", notes="修改参数内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "code", value = "参数代码", dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "content", value = "内容", dataType = "JSONArray", paramType = "body")
    })
    @PutMapping("config/content")
    @RequiresPermissions("sys:config:content:update")
    public @ResponseBody
    JSONObject content(@RequestBody JSONObject params) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",params.getString("code"));
        SysConfig config=sysConfigService.getOne(queryWrapper);
        if(config==null){
            return JsonResult.fail("参数不存在");
        }
        config.setContent(params.getJSONObject("content"));
        config.setModifierId(getUserId());
        config.setModifyTime(LocalDateTime.now());
        sysConfigService.updateById(config);
        ConfigUtils.update();
        return JsonResult.ok();
    }
}

