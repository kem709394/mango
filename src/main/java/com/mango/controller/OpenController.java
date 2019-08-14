package com.mango.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mango.common.DictConstants;
import com.mango.common.JsonResult;
import com.mango.common.exception.RtException;
import com.mango.sys.entity.SysDictionary;
import com.mango.sys.entity.SysFile;
import com.mango.sys.service.SysDictionaryService;
import com.mango.sys.service.SysFileService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/open")
public class OpenController {

    private final SysFileService sysFileService;

    private final SysDictionaryService sysDictionaryService;

    @Autowired
    public OpenController(SysFileService sysFileService, SysDictionaryService sysDictionaryService) {
        this.sysFileService = sysFileService;
        this.sysDictionaryService = sysDictionaryService;
    }

    @ApiOperation(value="获取字典数据", notes="获取字典数据")
    @GetMapping("dictionary")
    public @ResponseBody
    JSONObject dictionary() {
        QueryWrapper<SysDictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", false);
        queryWrapper.orderByDesc("priority");
        List<SysDictionary> list = sysDictionaryService.list(queryWrapper);
        JSONArray data = new JSONArray();
        for (SysDictionary temp : list) {
            JSONObject item = new JSONObject();
            item.put("id", temp.getId());
            item.put("code", temp.getCode());
            item.put("name", temp.getName());
            item.put("options", temp.getOptions());
            data.add(item);
        }
        return JsonResult.list(data);
    }

    @ApiOperation(value="下载本地文件", notes="下载本地文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "文件UUID", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("file/{uuid}")
    public void file(@PathVariable("uuid") String uuid, HttpServletResponse response) {
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", false);
        queryWrapper.eq("uuid", uuid);
        SysFile file = sysFileService.getOne(queryWrapper);
        if (file == null) {
            throw new RtException("找不到文件");
        } else {
            if(!file.getStore().equals(DictConstants.STORE_MODE_LOCAL)){
                throw new RtException("找不到文件");
            }
            try {
                response.reset();
                response.setContentType(file.getType());
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
                DataInputStream in = new DataInputStream(
                        new FileInputStream(new File(file.getExtFields().getString("filePath"))));
                OutputStream out = response.getOutputStream();
                int bytes;
                byte[] buffer = new byte[1024];
                while ((bytes = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes);
                }
                out.close();
                in.close();
            } catch (IOException e) {
                throw new RtException("文件读取失败");
            }
        }
    }
}
