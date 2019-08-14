package com.mango.controller;

import com.alibaba.fastjson.JSONObject;
import com.mango.common.ConfigUtils;
import com.mango.sys.entity.SysUserToken;
import com.mango.sys.service.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/other")
public class OtherController extends BaseController {

    private final SysUserTokenService sysUserTokenService;

    @Autowired
    public OtherController(SysUserTokenService sysUserTokenService) {
        this.sysUserTokenService = sysUserTokenService;
    }

    @RequestMapping("druid/login")
    public ModelAndView druidLogin(@PathParam("token") String token) {
        ModelAndView mv = new ModelAndView();
        SysUserToken userToken = sysUserTokenService.queryByToken(token);
        if(userToken != null && userToken.getExpireTime().isAfter(LocalDateTime.now())){
            JSONObject config= ConfigUtils.get("DRUID_CONFIG");
            mv.addObject("username", config.getString("username"));
            mv.addObject("password", config.getString("password"));
            mv.addObject("index_url", config.getString("index_url"));
            mv.setViewName("druid/login");
        } else {
            mv.setViewName("druid/error");
        }
        return mv;
    }

}
