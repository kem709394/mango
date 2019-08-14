package com.mango.sys.service;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
@SuppressWarnings("ALL")
public interface SysStoreService {

    JSONObject createLocalStore(String uuid, String data, String type, JSONObject config);

    JSONObject createLocalStore(String uuid, byte[] bytes, String type, JSONObject config);

    boolean removeLocalStore(String filePath);

    JSONObject createAliyunStore(String uuid, String data, String type, JSONObject config);

    JSONObject createAliyunStore(String uuid, byte[] data, String type, JSONObject config);

    boolean removeAliyunStore(String bucketName, String objectName, JSONObject config);

    JSONObject createQcloudStore(String uuid, String data, String type, JSONObject config);

    JSONObject createQcloudStore(String uuid, byte[] data, String type, JSONObject config);

    boolean removeQcloudStore(String bucketName, String objectName, JSONObject config);

}
