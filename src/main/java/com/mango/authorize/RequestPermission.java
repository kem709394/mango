package com.mango.authorize;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.shiro.authz.Permission;

/**
 * 自定义权限类
 * code:   权限代码
 * filter: 内容过滤器
 */
@Data
class RequestPermission implements Permission {

    private String code;

    private JSONObject filter;

    @Override
    public boolean implies(Permission permission) {
        return this.code.equals(permission.toString());
    }
}
