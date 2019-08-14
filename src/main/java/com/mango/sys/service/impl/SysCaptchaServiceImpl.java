package com.mango.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mango.common.exception.RtException;
import com.mango.config.RedisConfig;
import com.mango.sys.entity.SysCaptcha;
import com.mango.sys.mapper.SysCaptchaMapper;
import com.mango.sys.service.SysCaptchaService;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kem
 * @since 2019-07-06
 */
@Service
public class SysCaptchaServiceImpl extends ServiceImpl<SysCaptchaMapper, SysCaptcha> implements SysCaptchaService {

    private final Producer producer;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public SysCaptchaServiceImpl(Producer producer, RedisTemplate<String, Object> redisTemplate) {
        this.producer = producer;
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings("UnusedAssignment")
    @Override
    public BufferedImage getCaptcha(String session) {
        if(StringUtils.isBlank(session)){
            throw new RtException("session不能为空");
        }
        String code = producer.createText();
        if(RedisConfig.enabled){
            redisTemplate.opsForValue().set("captcha-" + session, code,5, TimeUnit.MINUTES);
        }else{
            SysCaptcha captcha = new SysCaptcha();
            captcha.setSession(session);
            captcha.setCode(code);
            captcha.setExpireTime(LocalDateTime.now().plus(5, ChronoUnit.MINUTES));
            this.save(captcha);
        }
        return producer.createImage(code);
    }

    @SuppressWarnings("UnusedAssignment")
    @Override
    public boolean validate(String session, String code) {
        if(RedisConfig.enabled){
            Object obj=redisTemplate.opsForValue().get("captcha-" + session);
            if(obj!=null){
                redisTemplate.delete("captcha-" + session);
                return obj.equals(code);
            }
        }else{
            QueryWrapper<SysCaptcha> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("session", session);
            SysCaptcha captcha = this.getOne(queryWrapper);
            if(captcha == null){
                return false;
            }
            this.removeById(captcha.getId());
            return captcha.getCode().equalsIgnoreCase(code) && captcha.getExpireTime().isAfter(LocalDateTime.now());
        }
        return false;
    }

}
