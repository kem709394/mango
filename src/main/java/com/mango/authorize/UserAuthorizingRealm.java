package com.mango.authorize;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 用户操作权限验证
 * 验证是否拥有相应的操作权限
 */
public abstract class UserAuthorizingRealm extends AuthorizingRealm {

    @Override
    protected boolean isPermitted(Permission permission, AuthorizationInfo info) {
        Collection<Permission> perms = this.getPermissions(info);
        if (perms != null && !perms.isEmpty()) {
            for(Permission perm:perms){
                if (perm.implies(permission)) {
                    RequestPermission reqPerm=(RequestPermission)perm;
                    if(RequestContextHolder.getRequestAttributes()!=null){
                        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
                        request.setAttribute("filter", reqPerm.getFilter());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
