package com.mango.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.sys.entity.SysUser;
import com.mango.sys.entity.SysUserToken;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
public interface SysUserTokenService extends IService<SysUserToken> {

    String createToken(SysUser user);

    SysUserToken queryByToken(String token);

    void extendToken(Long userId);

    void destroyToken(String token);
}
