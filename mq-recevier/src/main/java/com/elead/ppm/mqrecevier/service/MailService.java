package com.elead.ppm.mqrecevier.service;

import java.io.File;
import java.io.StringWriter;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailService {
	@Autowired
    private JavaMailSender javaMailSender;

    public void testSimpleTextMail(){   //发送普通文本邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("liwei@e-lead.cn");// 发送者，可选的
        mailMessage.setTo("671579557@qq.com");//接受者
        mailMessage.setSubject("测试邮件");//主题
        mailMessage.setText("Test Email send by javaMailSender!");//邮件内容

        javaMailSender.send(mailMessage);
    }

    public void testMimeMail() throws Exception {   //发送HTML格式的邮件

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("liwei@e-lead.cn");
        helper.setTo("671579557@qq.com");
        helper.setSubject("主题：嵌入静态资源");
        helper.setText("<html><body><a href=\"http://www.baidu.com\" ></body></html>", true);

        javaMailSender.send(mimeMessage);
    }

    
    public void testMimeInlineMail() throws Exception { //发送HTML格式含图片的邮件

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("liwei@e-lead.cn");
        helper.setTo("671579557@qq.com");
        helper.setSubject("主题：嵌入静态资源");
        helper.setText("<html><body><img src=\"cid:logo\" ></body></html>", true);
        FileSystemResource logoFile = new FileSystemResource(new File("logo.jpg"));
        helper.addInline("logo", logoFile);

        javaMailSender.send(mimeMessage);
    }

    
    public void testAttachmentMail() throws Exception { //发送含附件的邮件

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);// 第二个参数设置为true，表示允许添加附件
        helper.setFrom("liwei@e-lead.cn");
        helper.setTo("671579557@qq.com");
        helper.setSubject("发送含图片附件的邮件");
        helper.setText("含有附件的邮件");

        helper.addAttachment(MimeUtility.encodeText("附件-1.jpg"), new File("E:/attachment1.jpg"));
        helper.addAttachment(MimeUtility.encodeText("附件-2.jpg"), new File("E:/attachment2.jpg"));

        javaMailSender.send(mimeMessage);
    }
    
    
    public void testSendTemplateMail() throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("liwei@e-lead.cn");
        helper.setTo("671579557@qq.com");
        helper.setSubject("主题：模板邮件");

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        
        VelocityContext context = new VelocityContext();
        context.put("username", "Velocity");
        context.put("activation_url", "http://www.baidu.com");
        Template template = ve.getTemplate("mail.vm","UTF-8");
        StringWriter sw = new StringWriter();
        template.merge(context, sw);

        String text = sw.toString();
        helper.setText(text, true);
        javaMailSender.send(mimeMessage);
    }
}
