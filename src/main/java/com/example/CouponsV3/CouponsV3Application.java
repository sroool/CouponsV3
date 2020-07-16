package com.example.CouponsV3;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.CouponsV3.jobs.CustomerLastActionJob;

@SpringBootApplication
public class CouponsV3Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(CouponsV3Application.class, args);
		CustomerLastActionJob job = ctx.getBean(CustomerLastActionJob.class);
		Thread lastActionThread = new Thread(job);
		lastActionThread.start();
//		(ctx.getBean(Test.class)).testAll();
		(ctx.getBean(DBMockUp.class)).generateMockUp();
	}

}
