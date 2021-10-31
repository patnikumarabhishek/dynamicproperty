package com.dynamic.property.reader.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dynamicPropertyController")
public class DynamicPropertyController {

	@Value("${propertyValues}")
	String propertyValues;
	@Value("${spring.mail.username}")
	String userName;
	@Value("${spring.mail.password}")
	String password;
	@Value("${spring.mail.host}")
	String host;
	@Value("${spring.mail.port}")
	String port;
	@Value("${mail.transport.protocol}")
	String protocol;
	

	@GetMapping("/")
	public String SayHell() {
		return "propertyValues= " + propertyValues + ": userName= " + userName + ": password= " + password + ": host= " + host + ": port= " + port+ ": protocol= "+protocol;
	}

}
