package com.mango.task.job;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mango.common.ConfigUtils;
import com.mango.common.JsonUtils;
import com.mango.sys.entity.SysMail;
import com.mango.sys.service.SysMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@Component
public class MailJob {

    private final SysMailService sysMailService;

    @Autowired
    public MailJob(SysMailService sysMailService) {
        this.sysMailService = sysMailService;
    }

    @SuppressWarnings("ALL")
    public void execute(JSONObject params) {
        JSONObject config= ConfigUtils.get("MAIL_CONFIG");
        JavaMailSenderImpl jms = new JavaMailSenderImpl();
        jms.setHost(config.getString("host"));
        jms.setPort(config.getIntValue("port"));
        jms.setUsername(config.getString("username"));
        jms.setPassword(config.getString("password"));
        jms.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.starttls.enable", "true");
        p.setProperty("mail.smtp.starttls.required", "true");
        jms.setJavaMailProperties(p);
        QueryWrapper<SysMail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false);
        queryWrapper.in("state", "0","-1");
        queryWrapper.lt("counter",3);
        List<SysMail> list=sysMailService.list(queryWrapper);
        for(SysMail temp:list){
            temp.setSendTime(LocalDateTime.now());
            try {
                MimeMessage mimeMessage = jms.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setSubject(temp.getSubject());
                helper.setText(temp.getContent());
                helper.setFrom(config.getString("sender"));
                helper.setTo(JsonUtils.toString(temp.getToSite()));
                if(temp.getCcSite().size()>0){
                    helper.setCc(JsonUtils.toString(temp.getCcSite()));
                }
                jms.send(mimeMessage);
                temp.setState("1");
            } catch (MessagingException e) {
                e.printStackTrace();
                temp.setState("-1");
                temp.setCounter(temp.getCounter()+1);
                temp.setMessage(e.getMessage());
            }
            sysMailService.updateById(temp);
        }
    }
}
