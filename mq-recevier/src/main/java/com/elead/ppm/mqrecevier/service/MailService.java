package com.elead.ppm.mqrecevier.service;

import java.io.File;
import java.io.StringWriter;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.elead.platform.common.api.message.SimpleTextMailDto;

@Component
public class MailService {
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String defaultFrom;

	/**
	 * 发送普通文本邮件
	 * @param mail 邮件对象
	 */
    public void sendSimpleTextMail(SimpleTextMailDto mail){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(StringUtils.isNotEmpty(mail.getFrom())?mail.getFrom():defaultFrom);// 发送者，可选的
        mailMessage.setTo(mail.getTo());//接受者
        mailMessage.setCc(mail.getCc());
        mailMessage.setSubject(mail.getSubject());//主题
        mailMessage.setText(mail.getTextContent());//邮件内容
        javaMailSender.send(mailMessage);
    }

    /**
     * 发送简单Html邮件
     * @param mail
     * @throws Exception
     */
    public void sendMimeMail(SimpleTextMailDto mail) throws Exception {   //发送HTML格式的邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(StringUtils.isNotEmpty(mail.getFrom())?mail.getFrom():defaultFrom);
        helper.setTo(mail.getTo());
        helper.setCc(mail.getCc());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getTextContent(), true);
        javaMailSender.send(mimeMessage);
    }

    
    public void testMimeInlineMail() throws Exception { //发送HTML格式含图片的邮件

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("eleadrd@aliyun.com");
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
        helper.setFrom("eleadrd@aliyun.com");
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
        helper.setFrom("eleadrd@aliyun.com");
        helper.setTo("671579557@qq.com");
        helper.setSubject("主题：模板邮件");

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        
        VelocityContext context = new VelocityContext();
        context.put("username", "Velocity");
        context.put("activation_url", "http://www.baidu.com");
        Template template = ve.getTemplate("templates/mail.vm","UTF-8");
        StringWriter sw = new StringWriter();
        template.merge(context, sw);

        String text = sw.toString();
        helper.setText(text, true);
        javaMailSender.send(mimeMessage);
    }
}
