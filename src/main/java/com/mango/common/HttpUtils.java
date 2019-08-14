package com.mango.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;

public class HttpUtils {

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if(ip.contains(",")){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static JSONObject getDevice(HttpServletRequest request) {
        JSONObject data=new JSONObject();
        String ua = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        Browser browser = userAgent.getBrowser();
        OperatingSystem os = userAgent.getOperatingSystem();
        data.put("system",os.getName());
        data.put("browser",browser.getName());
        return data;
    }

    public static JSONObject getParameters(HttpServletRequest request){
        JSONObject data=new JSONObject();
        if(request.getMethod().equals("GET")){
            for(String key:request.getParameterMap().keySet()){
                data.put(key,request.getParameter(key));
            }
        }else{
            if(request instanceof ShiroHttpServletRequest){
                ShiroHttpServletRequest request1 = (ShiroHttpServletRequest) request;
                if(request1.getRequest() instanceof ContentCachingRequestWrapper){
                    ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request1.getRequest();
                    String body= StringUtils.toEncodedString(wrapper.getContentAsByteArray(),
                            Charset.forName(wrapper.getCharacterEncoding()));
                    data= JSON.parseObject(body);
                }
            }
        }
        return data;
    }
}
