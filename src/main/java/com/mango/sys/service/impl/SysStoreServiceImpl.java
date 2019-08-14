package com.mango.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.mango.sys.service.SysStoreService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

@SuppressWarnings("ALL")
@Service
public class SysStoreServiceImpl implements SysStoreService {

    @Override
    public JSONObject createLocalStore(String uuid, String data, String type, JSONObject config) {
        JSONObject result = new JSONObject();
        try {
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType mimeType = allTypes.forName(type);
            String fileName = uuid + mimeType.getExtension();
            String storeDirectory = config.getString("storeDirectory");
            String filePath = storeDirectory + "/" + fileName;
            String pathPrefix = config.getString("pathPrefix");
            if(StringUtils.isNotBlank(pathPrefix)){
                filePath=storeDirectory +  "/" + pathPrefix + "/" + fileName;
            }
            Files.write(Paths.get(filePath), Base64.getDecoder().decode(data), StandardOpenOption.CREATE);
            JSONObject info = new JSONObject();
            info.put("name", fileName);
            info.put("filePath", filePath);
            info.put("url", config.getString("domain") + "/open/file/" + uuid);
            result.put("err_code", 0);
            result.put("info", info);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("err_code", -1);
            result.put("err_msg", e.getMessage());
        }
        return result;
    }

    @Override
    public JSONObject createLocalStore(String uuid, byte[] bytes, String type, JSONObject config) {
        JSONObject result = new JSONObject();
        try {
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType mimeType = allTypes.forName(type);
            String fileName = uuid + mimeType.getExtension();
            String storeDirectory = config.getString("storeDirectory");
            String filePath = storeDirectory + "/" + fileName;
            String pathPrefix = config.getString("pathPrefix");
            if(StringUtils.isNotBlank(pathPrefix)){
                filePath=storeDirectory +  "/" + pathPrefix + "/" + fileName;
            }
            Files.write(Paths.get(filePath), bytes, StandardOpenOption.CREATE);
            JSONObject info = new JSONObject();
            info.put("name", fileName);
            info.put("filePath", filePath);
            info.put("url", config.getString("domain") + "/open/file/" + uuid);
            result.put("err_code", 0);
            result.put("info", info);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("err_code", -1);
            result.put("err_msg", e.getMessage());
        }
        return result;
    }

    @Override
    public boolean removeLocalStore(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.delete(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public JSONObject createAliyunStore(String uuid, String data, String type, JSONObject config) {
        JSONObject result = new JSONObject();
        try {
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType mimeType = allTypes.forName(type);
            String fileName = uuid + mimeType.getExtension();
            String objectName = fileName;
            String bucketName=config.getString("bucketName");
            String pathPrefix = config.getString("pathPrefix");
            if(StringUtils.isNotBlank(pathPrefix)){
                objectName=pathPrefix+"/"+objectName;
            }
            byte[] bytes=Base64.getDecoder().decode(data);
            OSS ossClient = new OSSClientBuilder().build(config.getString("endpoint"), config.getString("accessKeyId"),config.getString("accessKeySecret"));
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
            JSONObject info = new JSONObject();
            info.put("name", fileName);
            info.put("objectName", objectName);
            info.put("bucketName", bucketName);
            info.put("url", config.getString("domain") + "/" + objectName);
            result.put("err_code", 0);
            result.put("info", info);
            ossClient.shutdown();
        } catch (MimeTypeException e) {
            e.printStackTrace();
            result.put("err_code", -1);
            result.put("err_msg", e.getMessage());
        }
        return result;
    }

    @Override
    public JSONObject createAliyunStore(String uuid, byte[] data, String type, JSONObject config) {
        JSONObject result = new JSONObject();
        try {
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType mimeType = allTypes.forName(type);
            String fileName = uuid + mimeType.getExtension();
            String objectName = fileName;
            String bucketName=config.getString("bucketName");
            String pathPrefix = config.getString("pathPrefix");
            if(StringUtils.isNotBlank(pathPrefix)){
                objectName=pathPrefix+"/"+objectName;
            }
            OSS ossClient = new OSSClientBuilder().build(config.getString("endpoint"), config.getString("accessKeyId"),config.getString("accessKeySecret"));
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(data));
            JSONObject info = new JSONObject();
            info.put("name", fileName);
            info.put("objectName", objectName);
            info.put("bucketName", bucketName);
            info.put("url", config.getString("domain") + "/" + objectName);
            result.put("err_code", 0);
            result.put("info", info);
            ossClient.shutdown();
        } catch (MimeTypeException e) {
            e.printStackTrace();
            result.put("err_code", -1);
            result.put("err_msg", e.getMessage());
        }
        return result;
    }

    @Override
    public boolean removeAliyunStore(String bucketName, String objectName, JSONObject config) {
        try {
            OSS ossClient = new OSSClientBuilder().build(config.getString("endpoint"), config.getString("accessKeyId"),config.getString("accessKeySecret"));
            ossClient.deleteObject(bucketName, objectName);
            ossClient.shutdown();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public JSONObject createQcloudStore(String uuid, String data, String type, JSONObject config) {
        JSONObject result = new JSONObject();
        try {
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType mimeType = allTypes.forName(type);
            String fileName = uuid + mimeType.getExtension();
            String objectName = fileName;
            String bucketName=config.getString("bucketName") + "-" + config.getString("appId");
            String pathPrefix = config.getString("pathPrefix");
            if(StringUtils.isNotBlank(pathPrefix)){
                objectName=pathPrefix+"/"+objectName;
            }
            COSCredentials cred = new BasicCOSCredentials(config.getString("secretId"), config.getString("secretKey"));
            Region region = new Region(config.getString("region"));
            ClientConfig clientConfig = new ClientConfig(region);
            COSClient cosClient = new COSClient(cred, clientConfig);
            byte[] bytes=Base64.getDecoder().decode(data);
            InputStream inputStream=new ByteArrayInputStream(bytes);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setContentType(type);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName,  inputStream, objectMetadata);
            cosClient.putObject(putObjectRequest);
            JSONObject info = new JSONObject();
            info.put("name", fileName);
            info.put("objectName", objectName);
            info.put("bucketName", bucketName);
            info.put("url", config.getString("domain") + "/" + objectName);
            result.put("err_code", 0);
            result.put("info", info);
            cosClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            result.put("err_code", -1);
            result.put("err_msg", e.getMessage());
        }
        return result;
    }

    @Override
    public JSONObject createQcloudStore(String uuid, byte[] data, String type, JSONObject config) {
        JSONObject result = new JSONObject();
        try {
            MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
            MimeType mimeType = allTypes.forName(type);
            String fileName = uuid + mimeType.getExtension();
            String objectName = fileName;
            String bucketName=config.getString("bucketName") + "-" + config.getString("appId");
            String pathPrefix = config.getString("pathPrefix");
            if(StringUtils.isNotBlank(pathPrefix)){
                objectName=pathPrefix+"/"+objectName;
            }
            COSCredentials cred = new BasicCOSCredentials(config.getString("secretId"), config.getString("secretKey"));
            Region region = new Region(config.getString("region"));
            ClientConfig clientConfig = new ClientConfig(region);
            COSClient cosClient = new COSClient(cred, clientConfig);
            InputStream inputStream=new ByteArrayInputStream(data);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setContentType(type);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName,  inputStream, objectMetadata);
            cosClient.putObject(putObjectRequest);
            JSONObject info = new JSONObject();
            info.put("name", fileName);
            info.put("objectName", objectName);
            info.put("bucketName", bucketName);
            info.put("url", config.getString("domain") + "/" + objectName);
            result.put("err_code", 0);
            result.put("info", info);
            cosClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
            result.put("err_code", -1);
            result.put("err_msg", e.getMessage());
        }
        return result;
    }

    @Override
    public boolean removeQcloudStore(String bucketName, String objectName, JSONObject config) {
        try {
            COSCredentials cred = new BasicCOSCredentials(config.getString("secretId"), config.getString("secretKey"));
            Region region = new Region(config.getString("region"));
            ClientConfig clientConfig = new ClientConfig(region);
            COSClient cosClient = new COSClient(cred, clientConfig);
            cosClient.deleteObject(bucketName, objectName);
            cosClient.shutdown();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
