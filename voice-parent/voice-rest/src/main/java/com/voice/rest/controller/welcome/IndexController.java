package com.voice.rest.controller.welcome;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voice.rest.service.UserService;

@RestController
public class IndexController {

	private Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping("/index")
	public String index() {
		Map params =  userService.getUser();
		logger.info("测试 {}",params);
		return "hello world";
	}
}
