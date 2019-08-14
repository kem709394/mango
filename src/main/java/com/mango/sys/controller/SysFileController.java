package com.mango.sys.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mango.common.ConfigUtils;
import com.mango.common.DictConstants;
import com.mango.controller.BaseController;
import com.mango.common.JsonResult;
import com.mango.sys.entity.SysFile;
import com.mango.sys.service.SysFileService;
import com.mango.sys.service.SysStoreService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
@Controller
@RequestMapping("/v1/sys")
public class SysFileController extends BaseController {

    private final SysFileService sysFileService;

    private final SysStoreService sysStoreService;

    @Autowired
    public SysFileController(SysFileService sysFileService, SysStoreService sysStoreService) {
        this.sysFileService = sysFileService;
        this.sysStoreService = sysStoreService;
    }

    @ApiOperation(value="文件分页查询", notes="文件分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "current", value = "分页的页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页的行数", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "filter", value = "查询条件", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("file")
    @RequiresPermissions("sys:file:find")
    public @ResponseBody
    JSONObject find(@PathParam("current") int current, @PathParam("size") int size, @PathParam("filter") String filter) {
        Page<SysFile> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", false);
        byte[] bytes = Base64.getDecoder().decode(filter);
        JSONObject filterItem = JSON.parseObject(new String(bytes, StandardCharsets.UTF_8));
        if (filterItem.containsKey("name")) {
            queryWrapper.like("name", "%" + filterItem.getString("name") + "%");
        }
        if (filterItem.containsKey("dateRange")) {
            JSONArray dateRange=filterItem.getJSONArray("dateRange");
            queryWrapper.between("create_time" ,dateRange.getDate(0),dateRange.getDate(1));
        }
        queryWrapper.orderByDesc("create_time");
        IPage<SysFile> data = sysFileService.page(page, queryWrapper);
        return JsonResult.page(data.getRecords(),data.getTotal());
    }

    @ApiOperation(value="获取文件详细", notes="获取文件详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "文件ID", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("file/{id}")
    @RequiresPermissions("sys:file:detail")
    public @ResponseBody
    JSONObject detail(@PathVariable("id") Long id) {
        SysFile file = sysFileService.getById(id);
        return JsonResult.info(file);
    }

    @ApiOperation(value="上传文件实例", notes="上传文件实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "file", value = "文件对象", dataType = "File", paramType = "body"),
            @ApiImplicitParam(name = "name", value = "文件名称", dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "size", value = "文件大小", dataType = "Long", paramType = "body"),
            @ApiImplicitParam(name = "type", value = "文件类型", dataType = "String", paramType = "body")
    })
    @PostMapping("file")
    @RequiresPermissions("sys:file:upload")
    public @ResponseBody
    JSONObject upload(@RequestBody(required = false) JSONObject params,
                      @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        JSONObject config= ConfigUtils.get("STORE_CONFIG");
        SysFile sysFile = new SysFile();
        String uuid = UUID.randomUUID().toString();
        sysFile.setUuid(uuid);
        JSONObject upload;
        if (params == null || params.isEmpty()) {
            if (file.isEmpty()) {
                return JsonResult.fail("没有文件");
            } else {
                sysFile.setType(file.getContentType());
                sysFile.setSize(file.getSize());
                switch (config.getString("mode")) {
                    case DictConstants.STORE_MODE_LOCAL:
                        config = config.getJSONObject(DictConstants.STORE_MODE_LOCAL);
                        upload = sysStoreService.createLocalStore(uuid, file.getBytes(), file.getContentType(), config);
                        break;
                    case DictConstants.STORE_MODE_ALIYUN:
                        config = config.getJSONObject(DictConstants.STORE_MODE_ALIYUN);
                        upload = sysStoreService.createAliyunStore(uuid, file.getBytes(), file.getContentType(), config);
                        break;
                    case DictConstants.STORE_MODE_QCLOUD:
                        config = config.getJSONObject(DictConstants.STORE_MODE_QCLOUD);
                        upload = sysStoreService.createQcloudStore(uuid, file.getBytes(), file.getContentType(), config);
                        break;
                    default:
                        return JsonResult.fail("没有设置存储方式");
                }
            }
        } else {
            sysFile.setType(params.getString("type"));
            sysFile.setSize(params.getLong("size"));
            String data = params.getString("data");
            data = data.substring(data.indexOf(",") + 1);
            switch (config.getString("mode")) {
                case DictConstants.STORE_MODE_LOCAL:
                    config=config.getJSONObject(DictConstants.STORE_MODE_LOCAL);
                    upload = sysStoreService.createLocalStore(uuid, data, params.getString("type"), config);
                    break;
                case DictConstants.STORE_MODE_ALIYUN:
                    config = config.getJSONObject(DictConstants.STORE_MODE_ALIYUN);
                    upload = sysStoreService.createAliyunStore(uuid, data, params.getString("type"), config);
                    break;
                case DictConstants.STORE_MODE_QCLOUD:
                    config = config.getJSONObject(DictConstants.STORE_MODE_QCLOUD);
                    upload = sysStoreService.createQcloudStore(uuid, data, params.getString("type"), config);
                    break;
                default:
                    return JsonResult.fail("没有设置存储方式");
            }
        }
        if (upload.getIntValue("err_code") == 0) {
            sysFile.setStore(DictConstants.STORE_MODE_LOCAL);
            JSONObject info = upload.getJSONObject("info");
            sysFile.setUrl(info.getString("url"));
            sysFile.setName(info.getString("name"));
            sysFile.setExtFields(info);
            sysFile.setPermission(new JSONObject());
            sysFile.setIsDeleted(false);
            sysFile.setIsSynced(true);
            sysFile.setCreatorId(getUserId());
            sysFile.setCreateTime(LocalDateTime.now());
            sysFileService.save(sysFile);
            return JsonResult.info(sysFile);
        }else{
            return JsonResult.fail("上传失败");
        }
    }

    @ApiOperation(value="删除文件实例", notes="删除文件实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Token", value = "授权证书", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "ids", value = "文件ID", required = true, dataType = "JSONArray", paramType = "body"),
    })
    @DeleteMapping("file")
    @RequiresPermissions("sys:file:delete")
    public @ResponseBody
    JSONObject delete(@RequestBody JSONArray ids) {
        List<SysFile> list=new ArrayList<>();
        for(int i=0;i<ids.size();i++){
            SysFile item=new SysFile();
            item.setId(ids.getLong(i));
            item.setIsDeleted(true);
            item.setIsSynced(false);
            list.add(item);
        }
        sysFileService.updateBatchById(list);
        return JsonResult.ok();
    }
}

