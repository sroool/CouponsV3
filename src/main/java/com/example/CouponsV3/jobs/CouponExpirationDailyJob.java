package com.example.CouponsV3.jobs;

import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CouponsV3.dao.CouponRepo;

@Service
public class CouponExpirationDailyJob implements Runnable {
	@Autowired
	private CouponRepo coupRepo;
	private boolean quit; // a switch used to 'kill' the expiration job

	/**
	 * CouponExpirationDailyJob CTOR
	 * 
	 */
	public CouponExpirationDailyJob() {

	}

	/**
	 * uses custom queries to delete all the expired purchases along with the coupons themselves
	 */
	@Override
	public void run() {
		while (!quit) {

			coupRepo.deletePurchaseByEnddate(new Date(System.currentTimeMillis()));
			coupRepo.deleteByEnddate(new Date(System.currentTimeMillis()));

			try {
				Thread.sleep(1000 * 60 * 60 * 24);
			} catch (InterruptedException e) {
			}
		}

	}

	/**
	 * 
	 */
	public void stop() {
		quit = true;
	}

}
