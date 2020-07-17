package com.ztu.cloud.cloud.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Jager
 * @description 邮箱工具
 * @date 2020/06/23-18:18
 **/
@Component
public class EmailUtil {
    JavaMailSender mailSender;
    //TODO 暂时关闭 javaee-web-api会导致打包冲突
    public EmailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendPasswordToEmail(String email, String password) {
        // MimeMessage message = this.mailSender.createMimeMessage();
        // try {
        //     MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //     helper.setFrom("校园云盘<814878826@qq.com>");
        //     helper.setTo(email);
        //     helper.setSubject("校园云盘:您的新密码");
        //     helper.setText("新密码:" + password + "<br>请第一时间修改密码", true);
        //     this.mailSender.send(message);
        // } catch (MessagingException e) {
        //     e.printStackTrace();
        // }
    }

    @Async
    public void sendRegisterSuccess(String email, String account) {
        // MimeMessage message = this.mailSender.createMimeMessage();
        // try {
        //     MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //     helper.setFrom("校园云盘<814878826@qq.com>");
        //     helper.setTo(email);
        //     helper.setSubject("校园云盘:您已成功注册校园云盘");
        //     helper.setText("账号:" + account, true);
        //     this.mailSender.send(message);
        // } catch (MessagingException e) {
        //     e.printStackTrace();
        // }
    }
}
