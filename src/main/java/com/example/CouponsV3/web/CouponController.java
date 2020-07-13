package com.example.CouponsV3.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
@CrossOrigin(origins = "http://localhost:4200")
public class CouponController {
	@Autowired
	private Map<String, Session> sessions;
	

}
