package com.mango.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mango.sys.entity.SysCaptcha;

import java.awt.image.BufferedImage;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
public interface SysCaptchaService extends IService<SysCaptcha> {

    BufferedImage getCaptcha(String uuid);

    boolean validate(String uuid, String code);

}
