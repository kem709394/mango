package com.mango.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.sys.entity.SysTask;
import com.mango.sys.mapper.SysTaskMapper;
import com.mango.sys.service.SysTaskService;
import com.mango.task.SchedulerUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kem
 * @since 2019-07-31
 */
@Service
public class SysTaskServiceImpl extends ServiceImpl<SysTaskMapper, SysTask> implements SysTaskService {

    private final Scheduler scheduler;

    @Autowired
    public SysTaskServiceImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void init(){
        QueryWrapper<SysTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", false);
        SchedulerUtils.initQuartz(scheduler, this.list(queryWrapper));
    }
}
