package com.elead.ppm.mqsender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elead.platform.common.api.message.SimpleTextMailDto;
import com.elead.ppm.mqsender.sender.MailSender;

@RestController
@RequestMapping("/msg")
public class SendController {
	@Autowired
	private MailSender mailSender;
	
	
	@PostMapping("/mail")
	public void sendMail(@RequestBody SimpleTextMailDto mail){
		mailSender.send(mail);
	}
}
