package com.mango.task;

import com.mango.sys.entity.SysTask;
import org.quartz.*;

import java.util.List;

public class SchedulerUtils {

    private final static String PREFIX = "TASK_";

    public static void initQuartz(Scheduler scheduler, List<SysTask> list) {
        for(SysTask task : list){
            try {
                if(scheduler.checkExists(JobKey.jobKey(PREFIX + task.getId().toString()))){
                    updateJob(scheduler, task);
                } else {
                    createJob(scheduler, task);
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean createJob(Scheduler scheduler, SysTask task){
        boolean isOk = true;
        try {
            JobKey jobKey = JobKey.jobKey(PREFIX + task.getId().toString());
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(jobKey).build();
            jobDetail.getJobDataMap().put("TASK", task);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron())
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey(PREFIX + task.getId().toString())).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
            if(task.getState().equals("0")){
                scheduler.pauseJob(jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            isOk = false;
        }
        return isOk;
    }

    public static boolean updateJob(Scheduler scheduler, SysTask task){
        boolean isOk = true;
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(PREFIX + task.getId().toString());
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron())
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            trigger.getJobDataMap().put("TASK", task);
            scheduler.rescheduleJob(triggerKey, trigger);
            if(task.getState().equals("0")){
                scheduler.pauseJob(JobKey.jobKey(PREFIX + task.getId().toString()));
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            isOk = false;
        }
        return isOk;
    }

    public static boolean execute(Scheduler scheduler, SysTask task) {
        boolean isOk = true;
        try {
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("TASK", task);
            scheduler.triggerJob(JobKey.jobKey(PREFIX + task.getId().toString()), dataMap);
        } catch (SchedulerException e) {
            e.printStackTrace();
            isOk = false;
        }
        return isOk;
    }

    public static boolean remove(Scheduler scheduler, SysTask task) {
        boolean isOk = true;
        try {
            scheduler.deleteJob(JobKey.jobKey(PREFIX + task.getId().toString()));
        } catch (SchedulerException e) {
            e.printStackTrace();
            isOk = false;
        }
        return isOk;
    }

    public static boolean pause(Scheduler scheduler, SysTask task) {
        boolean isOk = true;
        try {
            scheduler.pauseJob(JobKey.jobKey(PREFIX + task.getId().toString()));
        } catch (SchedulerException e) {
            e.printStackTrace();
            isOk = false;
        }
        return isOk;
    }

    public static boolean resume(Scheduler scheduler, SysTask task) {
        boolean isOk = true;
        try {
            scheduler.resumeJob(JobKey.jobKey(PREFIX + task.getId().toString()));
        } catch (SchedulerException e) {
            e.printStackTrace();
            isOk = false;
        }
        return isOk;
    }
}
