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
import com.mango.sys.entity.SysMail;
import com.mango.sys.service.SysMailService;
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
 * @since 2019-08-01
 */
@Controller
@RequestMapping("/v1/sys")
public class SysMailController extends BaseController {
    private final SysMailService sysMailService;

    @Autowired
    public SysMailController(SysMailService sysMailService) {
        this.sysMailService = sysMailService;
    }

    @ApiOperation(value="邮件分页查询", notes="邮件分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("mail")
    @RequiresPermissions("sys:mail:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysMail> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysMail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("subject")){
            queryWrapper.like("subject","%"+filterItem.getString("subject")+"%");
        }
        if(filterItem.containsKey("state")){
            queryWrapper.eq("state",filterItem.getString("state"));
        }
        if (filterItem.containsKey("dateRange")) {
            JSONArray dateRange=filterItem.getJSONArray("dateRange");
            queryWrapper.between("create_time" ,dateRange.getDate(0),dateRange.getDate(1));
        }
        queryWrapper.orderByDesc("create_time");
        IPage<SysMail> data = sysMailService.page(page, queryWrapper);
        return JsonResult.page(data.getRecords(),data.getTotal());
    }

    @ApiOperation(value="获取邮件详细", notes="获取邮件详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "邮件ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("mail/{id}")
    @RequiresPermissions("sys:mail:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysMail mail=sysMailService.getById(id);
        return JsonResult.info(mail);
    }

    @ApiOperation(value="添加邮件实例", notes="添加邮件实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "subject", value = "主题", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "attachment", value = "附件", dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "inline", value = "插图", dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "toSite", value = "接收地址", required = true, dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "ccSite", value = "抄送地址", dataType = "JSONArray", paramType = "body")
    })
    @PostMapping("mail")
    @RequiresPermissions("sys:mail:create")
    public @ResponseBody
    JSONObject create(@RequestBody JSONObject params) {
        SysMail mail=new SysMail();
        mail.setSubject(params.getString("subject"));
        mail.setContent(params.getString("content"));
        mail.setAttachment(params.getJSONArray("attachment"));
        mail.setInline(params.getJSONArray("inline"));
        mail.setToSite(params.getJSONArray("toSite"));
        mail.setCcSite(params.getJSONArray("ccSite"));
        mail.setCreatorId(getUserId());
        mail.setCreateTime(LocalDateTime.now());
        mail.setState(DictConstants.MAIL_STATE_NONE);
        mail.setCounter(0);
        mail.setIsDeleted(false);
        sysMailService.save(mail);
        return JsonResult.ok();
    }

    @ApiOperation(value="修改邮件实例", notes="修改邮件实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "邮件ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "cancel", value = "撤销操作", dataType = "boolean", paramType = "body"),
            @ApiImplicitParam(name = "retry", value = "重发操作", dataType = "boolean", paramType = "body")
    })
    @PutMapping("mail/{id}")
    @RequiresPermissions("sys:mail:update")
    public @ResponseBody
    JSONObject update(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        SysMail mail=sysMailService.getById(id);
        if(params.containsKey("cancel")){
            if(mail.getState().equals(DictConstants.MAIL_STATE_NONE)||mail.getState().equals(DictConstants.MAIL_STATE_FAIL)){
                mail.setState(DictConstants.MAIL_STATE_CANCEL);
                sysMailService.updateById(mail);
                return JsonResult.ok();
            }else{
                return JsonResult.fail("操作失败");
            }
        } else if(params.containsKey("retry")) {
            if(mail.getState().equals(DictConstants.MAIL_STATE_CANCEL)||mail.getState().equals(DictConstants.MAIL_STATE_FAIL)){
                mail.setState(DictConstants.MAIL_STATE_NONE);
                mail.setCounter(0);
                sysMailService.updateById(mail);
                return JsonResult.ok();
            }else{
                return JsonResult.fail("操作失败");
            }
        } else {
            return JsonResult.fail("非法操作");
        }
    }

    @ApiOperation(value="删除邮件实例", notes="删除邮件实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "ids", value = "邮件ID", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @DeleteMapping("mail")
    @RequiresPermissions("sys:mail:delete")
    public @ResponseBody
    JSONObject delete(@RequestBody JSONArray ids) {
        List<SysMail> list=new ArrayList<>();
        for(int i=0;i<ids.size();i++){
            SysMail item=new SysMail();
            item.setId(ids.getLong(i));
            item.setIsDeleted(true);
            list.add(item);
        }
        sysMailService.updateBatchById(list);
        return JsonResult.ok();
    }
}

