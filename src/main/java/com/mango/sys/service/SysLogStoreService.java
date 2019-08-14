package com.mango.sys.service;

import com.mango.sys.entity.SysErrorLog;
import com.mango.sys.entity.SysLoginLog;
import com.mango.sys.entity.SysOperateLog;
import com.mango.sys.entity.SysTaskLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
public interface SysLogStoreService {

    void error(SysErrorLog log);

    void login(SysLoginLog log);

    void operate(SysOperateLog log);

    void task(SysTaskLog log);

}
