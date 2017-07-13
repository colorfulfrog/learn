package com.elead.ppm.mqsender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elead.platform.common.api.message.SimpleTextMailDto;
import com.elead.ppm.mqsender.sender.MailSender;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(description="消息发送服务")
@RestController
@RequestMapping("/msg")
public class SendController {
	@Autowired
	private MailSender mailSender;
	
	@ApiOperation("发送文本邮件")
	@PostMapping("/mail")
	public void sendMail(@ApiParam(value="文本邮件对象")@RequestBody SimpleTextMailDto mail){
		mailSender.send(mail);
	}
}
