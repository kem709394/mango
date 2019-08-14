package com.mango.task.job;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class TestJob {

    @SuppressWarnings("ALL")
    public void execute(JSONObject params) {
        System.out.println("This is test job, params: " + params.toJSONString());
    }
}
