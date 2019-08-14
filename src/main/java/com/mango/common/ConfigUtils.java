package com.mango.common;

import com.alibaba.fastjson.JSONObject;
import com.mango.sys.entity.SysConfig;
import com.mango.sys.service.SysConfigService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统运行参数
 */
public class ConfigUtils {

    private static Map<String, JSONObject> configMap;

    public static JSONObject get(String code) {
        if(configMap==null){
            update();
        }
        return configMap.get(code);
    }

    public static void update(){
        SysConfigService sysConfigService = SpringContextUtils.getBean(SysConfigService.class);
        configMap=new HashMap<>();
        List<SysConfig> list=sysConfigService.list();
        for(SysConfig temp:list){
            configMap.put(temp.getCode(),temp.getContent());
        }
    }

}
