package com.example.CouponsV3.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
	
	@Bean
	public Map<String, Session> sessions(){
		return new HashMap<String, Session>();
	}
}
