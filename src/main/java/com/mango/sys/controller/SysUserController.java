package com.mango.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mango.common.JsonResult;
import com.mango.controller.BaseController;
import com.mango.sys.entity.SysRole;
import com.mango.sys.entity.SysUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
@Controller
@RequestMapping("/v1/sys")
public class SysUserController extends BaseController {

    @ApiOperation(value="用户分页查询", notes="用户分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("user")
    @RequiresPermissions("sys:user:find")
    public @ResponseBody
    JSONObject find(@PathParam("current")int current, @PathParam("size")int size, @PathParam("filter")String filter) {
        Page<SysUser> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("userName")){
            queryWrapper.like("user_name","%"+filterItem.getString("userName")+"%");
        }
        if(filterItem.containsKey("nickName")){
            queryWrapper.like("nick_name","%"+filterItem.getString("nickName")+"%");
        }
        if(filterItem.containsKey("state")){
            queryWrapper.eq("state",filterItem.getString("state"));
        }
        queryWrapper.orderByDesc("create_time");
        IPage<SysUser> data = sysUserService.page(page, queryWrapper);
        JSONArray records = (JSONArray)JSON.toJSON(data.getRecords());
        for(Object obj: records){
            JSONObject item=(JSONObject)obj;
            item.put("roles", getRoles(item.getLong("id")));
        }
        return JsonResult.page(records,data.getTotal());
    }

    @ApiOperation(value="获取用户详细", notes="获取用户详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("user/{id}")
    @RequiresPermissions("sys:user:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysUser user=sysUserService.getById(id);
        JSONObject info = (JSONObject)JSON.toJSON(user);
        info.put("roles", getRoles(user.getId()));
        return JsonResult.info(info);
    }

    @ApiOperation(value="添加用户实例", notes="添加用户实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "avatar", value = "头像图片地址", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "nickName", value = "昵称", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "userName", value = "登录账号", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "password", value = "登录密码", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "state", value = "状态", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "privilege", value = "特权", required = true, dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "roles", value = "角色", required = true, dataType = "JSONArray", paramType = "body")
    })
    @PostMapping("user")
    @RequiresPermissions("sys:user:create")
    public @ResponseBody
    JSONObject create(@RequestBody JSONObject params) {
        SysUser user=new SysUser();
        user.setAvatar(params.getString("avatar"));
        user.setUserName(params.getString("userName"));
        user.setNickName(params.getString("nickName"));
        user.setState(params.getString("state"));
        user.setNote(params.getString("note"));
        user.setPrivilege(params.getJSONObject("privilege"));
        String salt = RandomStringUtils.randomAlphanumeric(20);
        String password=new Sha256Hash(params.getString("password"), salt).toHex();
        user.setSalt(salt);
        user.setPassword(password);
        user.setCreatorId(getUserId());
        user.setCreateTime(LocalDateTime.now());
        user.setIsDeleted(false);
        sysUserService.save(user);
        if(params.containsKey("roles")){
            sysUserService.updateUserHasRoles(user.getId(), params.getJSONArray("roles"));
        }
        return JsonResult.ok();
    }

    @ApiOperation(value="添加用户实例", notes="添加用户实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "avatar", value = "头像图片地址", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "nickName", value = "昵称", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "userName", value = "登录账号", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "password", value = "登录密码", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "state", value = "状态", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "note", value = "说明", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "privilege", value = "特权", dataType = "JSONArray", paramType = "body"),
            @ApiImplicitParam(name = "roles", value = "角色", dataType = "JSONArray", paramType = "body")
    })
    @PutMapping("user/{id}")
    @RequiresPermissions("sys:user:update")
    public @ResponseBody
    JSONObject update(@PathVariable("id") Long id, @RequestBody JSONObject params) {
        SysUser user=new SysUser();
        user.setId(id);
        if(params.containsKey("avatar")){
            user.setAvatar(params.getString("avatar"));
        }
        if(params.containsKey("userName")){
            user.setUserName(params.getString("userName"));
        }
        if(params.containsKey("nickName")){
            user.setNickName(params.getString("nickName"));
        }
        if(params.containsKey("password") && StringUtils.isNotBlank(params.getString("password"))){
            String salt = RandomStringUtils.randomAlphanumeric(20);
            String password=new Sha256Hash(params.getString("password"), salt).toHex();
            user.setSalt(salt);
            user.setPassword(password);
        }
        if(params.containsKey("note")){
            user.setNote(params.getString("note"));
        }
        if(params.containsKey("state")){
            user.setState(params.getString("state"));
        }
        user.setModifierId(getUserId());
        user.setModifyTime(LocalDateTime.now());
        sysUserService.updateById(user);
        if(params.containsKey("roles")){
            sysUserService.updateUserHasRoles(user.getId(), params.getJSONArray("roles"));
        }
        return JsonResult.ok();
    }

    @ApiOperation(value="删除用户实例", notes="删除用户实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "ids", value = "用户ID", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @DeleteMapping("user")
    @RequiresPermissions("sys:user:delete")
    public @ResponseBody
    JSONObject delete(@RequestBody JSONArray ids) {
        List<SysUser> list=new ArrayList<>();
        for(int i=0;i<ids.size();i++){
            SysUser item=new SysUser();
            item.setId(ids.getLong(i));
            item.setIsDeleted(true);
            list.add(item);
        }
        sysUserService.updateBatchById(list);
        return JsonResult.ok();
    }

    @ApiOperation(value="获取用户信息列表", notes="获取用户信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("user/list")
    @RequiresPermissions("sys:user:list")
    public @ResponseBody
    JSONObject list(@PathParam("filter")String filter) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem= JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if(filterItem.containsKey("userName")){
            queryWrapper.eq("user_name",filterItem.getString("userName"));
        }
        queryWrapper.orderByDesc("create_time");
        List<SysUser> list = sysUserService.list(queryWrapper);
        JSONArray array=new JSONArray();
        for(SysUser temp:list) {
            JSONObject item=new JSONObject();
            item.put("id", temp.getId());
            item.put("avatar", temp.getAvatar());
            item.put("nickName", temp.getNickName());
            item.put("userName", temp.getUserName());
            array.add(item);
        }
        return JsonResult.list(array);
    }

    @SuppressWarnings("Duplicates")
    private JSONArray getRoles(Long id){
        JSONArray data=new JSONArray();
        List<SysRole> list=sysUserService.getUserHasRoles(id);
        for(SysRole temp: list){
            JSONObject item=new  JSONObject();
            item.put("id",temp.getId());
            item.put("code",temp.getCode());
            item.put("name",temp.getName());
            item.put("state",temp.getState());
            data.add(item);
        }
        return data;
    }
}

