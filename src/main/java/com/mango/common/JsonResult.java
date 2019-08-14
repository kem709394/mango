package com.mango.common;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

@SuppressWarnings("ALL")
public class JsonResult {

    private static final String CODE = "err_code";

    private static final String MESSAGE = "err_msg";

    public static JSONObject ok() {
        JSONObject result = new JSONObject();
        result.put(CODE, 0);
        return result;
    }

    public static JSONObject ok(String objName,Object objData) {
        JSONObject result = new JSONObject();
        result.put(CODE, 0);
        result.put(objName, objData);
        return result;
    }

    public static JSONObject info(Object info) {
        JSONObject result = new JSONObject();
        result.put(CODE, 0);
        result.put("info", info);
        return result;
    }

    public static JSONObject list(List list) {
        JSONObject result = new JSONObject();
        result.put(CODE, 0);
        result.put("list", list);
        return result;
    }

    public static JSONObject page(List list, long total) {
        JSONObject result = new JSONObject();
        result.put(CODE, 0);
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    public static JSONObject fail(String message) {
        JSONObject result = new JSONObject();
        result.put(CODE, -1);
        result.put(MESSAGE, message);
        return result;
    }

    public static JSONObject fail(int code, String message) {
        JSONObject result = new JSONObject();
        result.put(CODE, code);
        result.put(MESSAGE, message);
        return result;
    }

    public static JSONObject other(int code) {
        JSONObject result = new JSONObject();
        result.put(CODE, code);
        return result;
    }

    public static JSONObject other(int code, String message) {
        JSONObject result = new JSONObject();
        result.put(CODE, code);
        result.put(MESSAGE, message);
        return result;
    }


}
