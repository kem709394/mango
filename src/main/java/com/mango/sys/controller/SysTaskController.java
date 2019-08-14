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
import com.mango.sys.entity.SysTask;
import com.mango.sys.service.SysTaskService;
import com.mango.task.SchedulerUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronExpression;
import org.quartz.Scheduler;
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
 * @since 2019-07-31
 */
@Controller
@RequestMapping("/v1/sys")
public class SysTaskController extends BaseController {

    private final SysTaskService sysTaskService;

    private final Scheduler scheduler;

    @Autowired
    public SysTaskController(SysTaskService sysTaskService, Scheduler scheduler) {
        this.sysTaskService = sysTaskService;
        this.scheduler = scheduler;
    }

    @ApiOperation(value="任务分页查询", notes="任务分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("task")
    @RequiresPermissions("sys:task:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysTask> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("name")){
            queryWrapper.like("name","%"+filterItem.getString("name")+"%");
        }
        if(filterItem.containsKey("bean")){
            queryWrapper.like("bean","%"+filterItem.getString("bean")+"%");
        }
        if(filterItem.containsKey("state")){
            queryWrapper.eq("state",filterItem.getString("state"));
        }
        queryWrapper.orderByDesc("create_time");
        IPage<SysTask> data = sysTaskService.page(page, queryWrapper);
        return JsonResult.page(data.getRecords(),data.getTotal());
    }

    @ApiOperation(value="获取任务详细", notes="获取任务详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "任务ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("task/{id}")
    @RequiresPermissions("sys:task:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysTask task=sysTaskService.getById(id);
        return JsonResult.info(task);
    }

    @ApiOperation(value="添加任务实例", notes="添加任务实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "bean", value = "Bean", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "params", value = "参数", required = true, dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "cron", value = "Cron", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", required = true, dataType = "String", paramType = "body")
    })
    @PostMapping("task")
    @RequiresPermissions("sys:task:create")
    public @ResponseBody
    JSONObject create(@RequestBody JSONObject params) {
        if(CronExpression.isValidExpression(params.getString("cron"))){
            SysTask task=new SysTask();
            task.setName(params.getString("name"));
            task.setBean(params.getString("bean"));
            task.setCron(params.getString("cron"));
            task.setNote(params.getString("note"));
            task.setParams(params.getJSONObject("params"));
            task.setNote(params.getString("note"));
            task.setCreatorId(getUserId());
            task.setCreateTime(LocalDateTime.now());
            task.setState(DictConstants.TASK_STATE_STP);
            task.setIsDeleted(false);
            sysTaskService.save(task);
            if(SchedulerUtils.createJob(scheduler, task)){
                return JsonResult.ok();
            }else{
                sysTaskService.removeById(task.getId());
                return JsonResult.fail("创建任务失败");
            }
        }else{
            return JsonResult.fail("Cron表达式错误");
        }
    }

    @ApiOperation(value="修改任务实例", notes="修改任务实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "任务ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "bean", value = "Bean", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "params", value = "参数", dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "cron", value = "Cron", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", dataType = "String", paramType = "body")
    })
    @PutMapping("task/{id}")
    @RequiresPermissions("sys:task:update")
    public @ResponseBody
    JSONObject update(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        SysTask task=sysTaskService.getById(id);
        if(params.containsKey("name")){
            task.setName(params.getString("name"));
        }
        if(params.containsKey("bean")){
            task.setBean(params.getString("bean"));
        }
        if(params.containsKey("cron")){
            if(CronExpression.isValidExpression(params.getString("cron"))){
                task.setCron(params.getString("cron"));
            }else{
                return JsonResult.fail("Cron表达式错误");
            }
        }
        if(params.containsKey("params")){
            task.setParams(params.getJSONObject("params"));
        }
        if(params.containsKey("note")){
            task.setNote(params.getString("note"));
        }
        task.setModifierId(getUserId());
        task.setModifyTime(LocalDateTime.now());
        if(SchedulerUtils.updateJob(scheduler, task)){
            sysTaskService.updateById(task);
            return JsonResult.ok();
        }else{
            return JsonResult.fail("修改任务失败");
        }
    }

    @ApiOperation(value="删除任务实例", notes="删除任务实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "任务ID", required = true, dataType = "Long", paramType = "path"),
    })
    @DeleteMapping("task/{id}")
    @RequiresPermissions("sys:task:delete")
    public @ResponseBody
    JSONObject delete(@PathVariable("id") Long id) {
        SysTask task=sysTaskService.getById(id);
        if(task==null){
            return JsonResult.fail("任务不存在");
        }else{
            if(SchedulerUtils.remove(scheduler, task)){
                task.setIsDeleted(true);
                sysTaskService.updateById(task);
                return JsonResult.ok();
            }else{
                return JsonResult.fail("操作失败");
            }
        }
    }

    @ApiOperation(value="获取任务信息列表", notes="获取任务信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("task/list")
    @RequiresPermissions("sys:task:list")
    public @ResponseBody
    JSONObject list(@PathParam("filter")String filter) {
        QueryWrapper<SysTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("bean")){
            queryWrapper.eq("bean",filterItem.getString("bean"));
        }
        queryWrapper.orderByDesc("create_time");
        List<SysTask> list = sysTaskService.list(queryWrapper);
        JSONArray array=new JSONArray();
        for(SysTask temp:list) {
            JSONObject item=new JSONObject();
            item.put("id", temp.getId());
            item.put("bean", temp.getBean());
            item.put("name", temp.getName());
            array.add(item);
        }
        return JsonResult.list(array);
    }

    @ApiOperation(value="操作任务实例", notes="操作任务实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "任务ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "command", value = "标题", dataType = "String", paramType = "body")
    })
    @PostMapping("task/{id}")
    @RequiresPermissions("sys:task:operate")
    public @ResponseBody
    JSONObject operate(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        SysTask task=sysTaskService.getById(id);
        if(task==null){
            return JsonResult.fail("任务不存在");
        }else{
            switch (params.getString("command")){
                case "0":
                    task.setState(DictConstants.TASK_STATE_STP);
                    if(!SchedulerUtils.pause(scheduler, task)){
                        return JsonResult.fail("操作失败");
                    }
                    break;
                case "1":
                    task.setState(DictConstants.TASK_STATE_RUN);
                    if(!SchedulerUtils.resume(scheduler, task)){
                        return JsonResult.fail("操作失败");
                    }
                    break;
                case "2":
                    if(!SchedulerUtils.execute(scheduler, task)){
                        return JsonResult.fail("操作失败");
                    }
                    return JsonResult.ok();
            }
            task.setModifierId(getUserId());
            task.setModifyTime(LocalDateTime.now());
            sysTaskService.updateById(task);
            return JsonResult.ok();
        }
    }

}

