package com.mango.authorize;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mango.common.DictConstants;
import com.mango.common.HttpUtils;
import com.mango.common.JsonResult;
import com.mango.sys.entity.SysLoginLog;
import com.mango.sys.entity.SysUser;
import com.mango.sys.service.SysCaptchaService;
import com.mango.sys.service.SysLogStoreService;
import com.mango.sys.service.SysUserService;
import com.mango.sys.service.SysUserTokenService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 用户授权接口
 */
@Controller
@RequestMapping("/auth")
public class UserLoginController {

    private final SysUserService sysUserService;

    private final SysCaptchaService sysCaptchaService;

    private final SysUserTokenService sysUserTokenService;

    private final SysLogStoreService sysLogStoreService;

    @Autowired
    public UserLoginController(SysUserService sysUserService, SysCaptchaService sysCaptchaService, SysUserTokenService sysUserTokenService, SysLogStoreService sysLogStoreService) {
        this.sysUserService = sysUserService;
        this.sysCaptchaService = sysCaptchaService;
        this.sysUserTokenService = sysUserTokenService;
        this.sysLogStoreService = sysLogStoreService;
    }

    @ApiOperation(value="验证码图片", notes="系统登录验证码图片")
    @ApiImplicitParam(name = "session", value = "sessionID", required = true, dataType = "String", paramType = "query")
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, String session) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        BufferedImage image = sysCaptchaService.getCaptcha(session);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    @ApiOperation(value="用户登录", notes="系统用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登录账号", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "password", value = "登录密码", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "captcha", value = "验证码", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "session", value = "sessionID", required = true, dataType = "String", paramType = "body")
    })
    @PostMapping("/user/login")
    public @ResponseBody
    JSONObject login(HttpServletRequest request, @RequestBody JSONObject params) {
        JSONObject result=new JSONObject();
        SysLoginLog log=new SysLoginLog();
        log.setUserName(params.getString("username"));
        log.setDevice(HttpUtils.getDevice(request));
        log.setIpAddress(HttpUtils.getIpAddress(request));
        boolean isOk = sysCaptchaService.validate(params.getString("session"), params.getString("captcha"));
        if(isOk){
            SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().eq("user_name", params.getString("username")).eq("is_deleted", false));
            if(user != null && user.getPassword().equals(new Sha256Hash(params.getString("password"), user.getSalt()).toHex())){
                if(user.getState().equals(DictConstants.USE_STATE_ENABLED)){
                    String token=sysUserTokenService.createToken(user);
                    if(StringUtils.isNotBlank(token)){
                        result.put("err_code",0);
                        result.put("token",token);
                        log.setIsSucceed(true);
                    }else{
                        result.put("err_code",104);
                        result.put("err_msg","Token授权错误");
                        log.setIsSucceed(false);
                        log.setNote("Token授权错误");
                    }
                }else{
                    result.put("err_code",103);
                    result.put("err_msg","账号已被锁定,请联系管理员");
                    log.setIsSucceed(false);
                    log.setNote("账号已被锁定,请联系管理员");
                }
            }else{
                result.put("err_code",102);
                result.put("err_msg","账号或密码不正确");
                log.setIsSucceed(false);
                log.setNote("账号或密码不正确");
            }
        }else{
            result.put("err_code",101);
            result.put("err_msg","验证码不正确");
            log.setIsSucceed(false);
            log.setNote("验证码不正确");
        }
        log.setIsDeleted(false);
        log.setLoginTime(LocalDateTime.now());
        sysLogStoreService.login(log);
        return result;
    }

    @ApiOperation(value="注销登录", notes="用户注销登录")
    @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header")
    @PostMapping("/user/logout")
    public @ResponseBody
    JSONObject logout(HttpServletRequest request) {
        String token = request.getHeader("Token");
        sysUserTokenService.destroyToken(token);
        return JsonResult.ok();
    }
}
