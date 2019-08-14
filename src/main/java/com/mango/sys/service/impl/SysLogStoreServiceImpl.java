package com.mango.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mango.common.ConfigUtils;
import com.mango.sys.entity.SysErrorLog;
import com.mango.sys.entity.SysLoginLog;
import com.mango.sys.entity.SysOperateLog;
import com.mango.sys.entity.SysTaskLog;
import com.mango.sys.mapper.SysErrorLogMapper;
import com.mango.sys.mapper.SysLoginLogMapper;
import com.mango.sys.mapper.SysOperateLogMapper;
import com.mango.sys.mapper.SysTaskLogMapper;
import com.mango.sys.service.SysLogStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
@Service
public class SysLogStoreServiceImpl implements SysLogStoreService {

    private final SysErrorLogMapper syErrorLogMapper;

    private final SysLoginLogMapper syLoginLogMapper;

    private final SysOperateLogMapper syOperateLogMapper;

    private final SysTaskLogMapper syTaskLogMapper;

    @Autowired
    public SysLogStoreServiceImpl(SysErrorLogMapper syErrorLogMapper, SysLoginLogMapper syLoginLogMapper, SysOperateLogMapper syOperateLogMapper, SysTaskLogMapper syTaskLogMapper) {
        this.syErrorLogMapper = syErrorLogMapper;
        this.syLoginLogMapper = syLoginLogMapper;
        this.syOperateLogMapper = syOperateLogMapper;
        this.syTaskLogMapper = syTaskLogMapper;
    }

    @Override
    public void error(SysErrorLog log) {
        JSONObject config= ConfigUtils.get("LOG_CONFIG");
        if(config.getBoolean("error")) {
            log.setCreateTime(LocalDateTime.now());
            log.setIsDeleted(false);
            syErrorLogMapper.insert(log);
        }
    }

    @Override
    public void login(SysLoginLog log) {
        JSONObject config= ConfigUtils.get("LOG_CONFIG");
        if(config.getBoolean("login")) {
            log.setIsDeleted(false);
            syLoginLogMapper.insert(log);
        }
    }

    @Override
    public void operate(SysOperateLog log) {
        JSONObject config= ConfigUtils.get("LOG_CONFIG");
        if(config.getBoolean("operate")) {
            log.setCreateTime(LocalDateTime.now());
            log.setIsDeleted(false);
            syOperateLogMapper.insert(log);
        }
    }

    @Override
    public void task(SysTaskLog log) {
        JSONObject config= ConfigUtils.get("LOG_CONFIG");
        if(config.getBoolean("task")) {
            log.setCreateTime(LocalDateTime.now());
            log.setIsDeleted(false);
            syTaskLogMapper.insert(log);
        }
    }
}
