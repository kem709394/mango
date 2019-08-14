package com.mango.task;

import com.alibaba.fastjson.JSONObject;
import com.mango.common.ConfigUtils;
import com.mango.common.SpringContextUtils;
import com.mango.sys.entity.SysTask;
import com.mango.sys.entity.SysTaskLog;
import com.mango.sys.service.SysLogStoreService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class ScheduleJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        SysTask task = (SysTask) jobExecutionContext.getMergedJobDataMap().get("TASK");
        SysLogStoreService sysLogStoreService = SpringContextUtils.getBean(SysLogStoreService.class);
        SysTaskLog taskLog = new SysTaskLog();
        taskLog.setSysTaskId(task.getId());
        taskLog.setBean(task.getBean());
        taskLog.setParams(task.getParams());
        taskLog.setNote(task.getNote());
        try {
            Object target = SpringContextUtils.getBean(task.getBean());
            Method method = target.getClass().getDeclaredMethod("execute", JSONObject.class);
            method.invoke(target, task.getParams());
            taskLog.setIsSucceed(true);
        } catch (Exception e) {
            taskLog.setIsSucceed(false);
            taskLog.setMessage(ExceptionUtils.getStackTrace(e));
        }finally {
            JSONObject config= ConfigUtils.get("LOG_CONFIG");
            if(config.getBoolean("task")){
                sysLogStoreService.task(taskLog);
            }
        }
    }
}
