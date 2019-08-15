package com.mango.authorize;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mango.common.HttpUtils;
import com.mango.sys.entity.SysOperateLog;
import com.mango.sys.entity.SysUser;
import com.mango.sys.service.SysLogStoreService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 对返回的内容进行过滤处理
 * 同时写入操作日志
 */
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    private final SysLogStoreService sysLogStoreService;

    @Autowired
    public ResponseResultHandler(SysLogStoreService sysLogStoreService) {
        this.sysLogStoreService = sysLogStoreService;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        try {
            if(object instanceof JSONObject){
                JSONObject jsonObject=(JSONObject)object;
                if(SecurityUtils.getSubject().getPrincipal()!=null){
                    if(RequestContextHolder.getRequestAttributes()!=null) {
                        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
                        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                        Object filterObj = request.getAttribute("filter");
                        if (filterObj != null) {
                            JSONObject filterJson = (JSONObject) filterObj;
                            if (filterJson.containsKey("fields")) {
                                JSONArray filterFields = filterJson.getJSONArray("fields");
                                if (jsonObject.containsKey("info")) {
                                    JSONObject info = jsonObject.getJSONObject("info");
                                    info.remove("isDeleted");
                                    for (int i = 0; i < filterFields.size(); i++) {
                                        info.remove(filterFields.getString(i));
                                    }
                                } else if (jsonObject.containsKey("list")) {
                                    JSONArray list = jsonObject.getJSONArray("list");
                                    for (int i = 0; i < list.size(); i++) {
                                        list.getJSONObject(i).remove("isDeleted");
                                        for (int j = 0; j < filterFields.size(); j++) {
                                            list.getJSONObject(i).remove(filterFields.getString(j));
                                        }
                                    }
                                }
                            }
                        }
                        SysOperateLog log = new SysOperateLog();
                        log.setDevice(HttpUtils.getDevice(request));
                        log.setIpAddress(HttpUtils.getIpAddress(request));
                        log.setMethod(request.getMethod());
                        log.setPath(request.getRequestURI());
                        log.setIsAgreed(true);
                        log.setSysUserId(user.getId());
                        log.setParameters(HttpUtils.getParameters(request));
                        log.setResponse(jsonObject);
                        sysLogStoreService.operate(log);
                    }
                }
                return jsonObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
