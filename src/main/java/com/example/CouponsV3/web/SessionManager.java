package com.example.CouponsV3.web;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SessionManager {
	@Autowired
	private Map<String, Session> sessions;
	
	@Pointcut("execution(* com.example.CouponsV3.web.AdminController.*(..))")
	public void adminControllerMethods() {}
	
	@Pointcut("execution(* com.example.CouponsV3.web.CustomerController.*(..))")
	public void customerControllerMethods() {
		
	}
	
	@Pointcut("execution(* com.example.CouponsV3.web.CompanyController.*(..))")
	public void companyControllerMethods() {}
	
	@Around("adminControllerMethods() || customerControllerMethods() || companyControllerMethods()")
	public Object validateToken(ProceedingJoinPoint pjp) {
		Object[] args = pjp.getArgs();
		String token = (String) args[0];
		Session session = sessions.get(token);
		if(session==null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
		}
		try {
			Object result = pjp.proceed();
			session.setLastActionTime(System.currentTimeMillis());
			return result;
		} catch (Throwable e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
}
