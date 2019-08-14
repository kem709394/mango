package com.mango.common;

import com.alibaba.fastjson.JSONArray;

public class JsonUtils {

    public static String[] toString(JSONArray list){
        String[] array = new String[list.size()];
        for(int i=0;i<list.size();i++){
            array[i] = list.getString(i);
        }
        return array;
    }

}
