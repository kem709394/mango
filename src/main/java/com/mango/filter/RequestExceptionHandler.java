package com.mango.filter;

import com.alibaba.fastjson.JSONObject;
import com.mango.common.HttpUtils;
import com.mango.sys.entity.SysErrorLog;
import com.mango.sys.entity.SysOperateLog;
import com.mango.sys.entity.SysUser;
import com.mango.sys.service.SysLogStoreService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求异常统一处理
 */
@ControllerAdvice
public class RequestExceptionHandler {

    private final SysLogStoreService sysLogStoreService;

    @Autowired
    public RequestExceptionHandler(SysLogStoreService sysLogStoreService) {
        this.sysLogStoreService = sysLogStoreService;
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    public JSONObject handleException1(Exception e, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        try {
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            SysOperateLog log = new SysOperateLog();
            log.setDevice(HttpUtils.getDevice(request));
            log.setIpAddress(HttpUtils.getIpAddress(request));
            log.setMethod(request.getMethod());
            log.setPath(request.getRequestURI());
            log.setIsAgreed(false);
            log.setSysUserId(user.getId());
            log.setParameters(HttpUtils.getParameters(request));
            log.setResponse(result);
            sysLogStoreService.operate(log);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        result.put("err_code", 200);
        result.put("err_msg", e.getMessage());
        return result;
    }

    @ExceptionHandler
    @ResponseBody
    public JSONObject handleException2(Exception e, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        try {
            SysErrorLog log = new SysErrorLog();
            log.setContent(ExceptionUtils.getStackTrace(e));
            log.setDevice(HttpUtils.getDevice(request));
            log.setIpAddress(HttpUtils.getIpAddress(request));
            log.setMethod(request.getMethod());
            log.setPath(request.getRequestURI());
            log.setParameters(HttpUtils.getParameters(request));
            sysLogStoreService.error(log);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        result.put("err_code", 300);
        result.put("err_msg", e.getMessage());
        return result;
    }

}
