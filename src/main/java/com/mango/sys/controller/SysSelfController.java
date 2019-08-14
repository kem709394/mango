package com.mango.sys.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mango.common.DictConstants;
import com.mango.controller.BaseController;
import com.mango.common.JsonResult;
import com.mango.sys.entity.SysMessage;
import com.mango.sys.entity.SysRole;
import com.mango.sys.entity.SysUser;
import com.mango.sys.service.SysMessageService;
import com.mango.sys.service.SysRoleService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
@Controller
@RequestMapping("/v1/self")
public class SysSelfController extends BaseController {

    private final SysRoleService sysRoleService;

    private final SysMessageService sysMessageService;

    @Autowired
    public SysSelfController(SysRoleService sysRoleService, SysMessageService sysMessageService) {
        this.sysRoleService = sysRoleService;
        this.sysMessageService = sysMessageService;
    }

    @ApiOperation(value="获取当前用户信息", notes="获取当前用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("info")
    @RequiresPermissions("sys:self:info:detail")
    public @ResponseBody
    JSONObject self() {
        SysUser user=sysUserService.getById(getUserId());
        JSONObject info = new JSONObject();
        info.put("id",user.getId());
        info.put("avatar",user.getAvatar());
        info.put("userName",user.getUserName());
        info.put("nickName",user.getNickName());
        JSONArray roles = new JSONArray();
        List<SysRole> list=sysUserService.getUserHasRoles(getUserId());
        for(SysRole temp:list){
            roles.add(temp.getName());
        }
        info.put("roles",roles);
        JSONArray access=new JSONArray();
        List<SysRole> roleList=sysUserService.getUserHasRoles(getUserId());
        JSONArray roleIds=new JSONArray();
        for(SysRole temp:roleList){
            if(temp.getState().equals(DictConstants.USE_STATE_ENABLED)){
                roleIds.add(temp.getId());
            }
        }
        if(roleIds.size()>0){
            List<Map<String, Object>> mapList=sysRoleService.getRolesHasMenuFunctions(roleIds);
            for(Map<String, Object> map:mapList){
                if(map.get("state").toString().equals(DictConstants.USE_STATE_ENABLED)){
                    access.add(map.get("code").toString());
                }
            }
        }
        info.put("access",access);
        return JsonResult.info(info);
    }

    @ApiOperation(value="修改当前用户信息", notes="修改当前用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "avatar", value = "头像图片地址", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "nickName", value = "昵称", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "password", value = "登录密码", dataType = "String", paramType = "body")
    })
    @PutMapping("info")
    @RequiresPermissions("sys:self:info:update")
    public @ResponseBody
    JSONObject self(@RequestBody JSONObject params) {
        SysUser user=sysUserService.getById(getUserId());
        if(params.containsKey("avatar")){
            user.setAvatar(params.getString("avatar"));
        }
        if(params.containsKey("nickName")){
            user.setNickName(params.getString("nickName"));
        }
        if(params.containsKey("oldPassword") && params.containsKey("newPassword")){
            String salt = user.getSalt();
            String oldPassword=new Sha256Hash(params.getString("oldPassword"), salt).toHex();
            if(user.getPassword().equals(oldPassword)){
                salt = RandomStringUtils.randomAlphanumeric(20);
                String newPassword=new Sha256Hash(params.getString("newPassword"), salt).toHex();
                user.setSalt(salt);
                user.setPassword(newPassword);
            }else{
                return JsonResult.fail("原密码错误");
            }
        }
        user.setModifierId(user.getId());
        user.setModifyTime(LocalDateTime.now());
        sysUserService.updateById(user);
        return JsonResult.ok();
    }

    @ApiOperation(value="获取当前用户消息数量", notes="获取当前用户消息数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("message/count")
    @RequiresPermissions("sys:self:message:count")
    public @ResponseBody
    JSONObject messageCount() {
        QueryWrapper<SysMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        queryWrapper.eq("sys_user_id",getUserId());
        queryWrapper.eq("state", DictConstants.MESSAGE_STATE_NRD);
        JSONObject info=new JSONObject();
        info.put("unread",sysMessageService.count(queryWrapper));
        return JsonResult.info(info);
    }

    @ApiOperation(value="获取当前用户消息列表", notes="获取当前用户消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("message/list")
    @RequiresPermissions("sys:self:message:list")
    public @ResponseBody
    JSONObject messageList() {
        QueryWrapper<SysMessage> queryWrapper0 = new QueryWrapper<>();
        queryWrapper0.eq("is_deleted",false);
        queryWrapper0.eq("sys_user_id",getUserId());
        queryWrapper0.eq("state",DictConstants.MESSAGE_STATE_NRD);
        queryWrapper0.orderByAsc("create_time");
        QueryWrapper<SysMessage> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("is_deleted",false);
        queryWrapper1.eq("sys_user_id",getUserId());
        queryWrapper1.eq("state",DictConstants.MESSAGE_STATE_RED);
        queryWrapper1.orderByAsc("create_time");
        QueryWrapper<SysMessage> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("is_deleted",false);
        queryWrapper2.eq("sys_user_id",getUserId());
        queryWrapper2.eq("state",DictConstants.MESSAGE_STATE_DEL);
        queryWrapper2.orderByAsc("create_time");
        JSONObject info=new JSONObject();
        info.put("unread",getMessages(sysMessageService.list(queryWrapper0)));
        info.put("read",getMessages(sysMessageService.list(queryWrapper1)));
        info.put("remove",getMessages(sysMessageService.list(queryWrapper2)));
        return JsonResult.info(info);
    }

    @ApiOperation(value="获取用户消息详细", notes="获取用户消息详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "消息ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("message/{id}")
    @RequiresPermissions("sys:self:message:detail")
    public @ResponseBody
    JSONObject messageDetail(@PathVariable("id") Long id) {
        SysMessage message =sysMessageService.getById(id);
        return JsonResult.info(message);
    }

    @ApiOperation(value="修改用户消息状态", notes="修改用户消息状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "消息ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "operate", value = "操作标志", dataType = "String", paramType = "body")
    })
    @PutMapping("message/{id}")
    @RequiresPermissions("sys:self:message:update")
    public @ResponseBody
    JSONObject messageOperate(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        SysMessage message = sysMessageService.getById(id);
        if(message.getSysUserId().intValue()==getUserId().intValue()){
            String operate = params.getString("operate");
            switch (operate) {
                case "read":
                    message.setState(DictConstants.MESSAGE_STATE_RED);
                    message.setReadTime(LocalDateTime.now());
                    sysMessageService.updateById(message);
                    break;
                case "remove":
                    message.setState(DictConstants.MESSAGE_STATE_DEL);
                    sysMessageService.updateById(message);
                    break;
                case "restore":
                    message.setState(DictConstants.MESSAGE_STATE_RED);
                    sysMessageService.updateById(message);
                    break;
                default:
                    return JsonResult.fail("操作失败");
            }
            return JsonResult.ok();
        }else{
            return JsonResult.fail("操作失败");
        }
    }

    private JSONArray getMessages(List<SysMessage> list){
        JSONArray data=new JSONArray();
        for(SysMessage temp:list){
            JSONObject item=new JSONObject();
            item.put("id", temp.getId());
            item.put("type", temp.getType());
            item.put("title", temp.getTitle());
            item.put("createTime", temp.getCreateTime());
            data.add(item);
        }
        return data;
    }
}

