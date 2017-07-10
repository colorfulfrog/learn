package com.elead.ppm.mq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elead.ppm.mq.sender.TestSender;

@RestController
@RequestMapping("/hello")
public class TestController {
	@Autowired
	private TestSender testSender;
	
	@GetMapping("/test")
	public void test(){
		testSender.send();
	}
}
