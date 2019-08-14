package com.mango.task.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mango.common.ConfigUtils;
import com.mango.common.DictConstants;
import com.mango.sys.entity.SysFile;
import com.mango.sys.service.SysFileService;
import com.mango.sys.service.SysStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileJob {

    private final SysFileService sysFileService;

    private final SysStoreService sysStoreService;

    @Autowired
    public FileJob(SysFileService sysFileService, SysStoreService sysStoreService) {
        this.sysFileService = sysFileService;
        this.sysStoreService = sysStoreService;
    }

    @SuppressWarnings("ALL")
    public void execute(JSONObject params) {
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",true);
        queryWrapper.eq("is_synced",false);
        List<SysFile> list=sysFileService.list(queryWrapper);
        for(SysFile temp:list){
            boolean flag = false;
            switch (temp.getStore()) {
                case DictConstants.STORE_MODE_LOCAL:
                    flag = sysStoreService.removeLocalStore(temp.getExtFields().getString("filePath"));
                    break;
                case DictConstants.STORE_MODE_ALIYUN:
                    flag = sysStoreService.removeAliyunStore(temp.getExtFields().getString("bucketName"), temp.getExtFields().getString("objectName"), ConfigUtils.get("STORE_CONFIG"));
                    break;
                case DictConstants.STORE_MODE_QCLOUD:
                    flag = sysStoreService.removeQcloudStore(temp.getExtFields().getString("bucketName"), temp.getExtFields().getString("objectName"), ConfigUtils.get("STORE_CONFIG"));
                    break;
            }
            if(flag){
                temp.setIsSynced(true);
                sysFileService.updateById(temp);
            }
        }
    }
}
