package com.example.CouponsV3.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CouponsV3.beans.Coupon;
import com.example.CouponsV3.exceptions.CouponAlreadyPurchasedException;
import com.example.CouponsV3.exceptions.CouponDoesntExistException;
import com.example.CouponsV3.exceptions.CouponExpiredException;
import com.example.CouponsV3.exceptions.CouponOutOfStockException;
import com.example.CouponsV3.facades.CustomerFacade;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {
	@Autowired
	private Map<String, Session> sessions;

	@GetMapping("/customer-details/{token}")
	public ResponseEntity<?> getCustomerDetails(@PathVariable String token) {
		Session session = sessions.get(token);
		if (session != null) {
			CustomerFacade facade = (CustomerFacade) sessions.get(token).getClientFacade();
			return ResponseEntity.ok(facade.getCustomerDetails());
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED!");
	}

	@GetMapping("/coupon-by-id/{token}/{id}")
	public ResponseEntity<?> getCouponById(@PathVariable String token, @PathVariable int id) {
		Session session = sessions.get(token);
		if (session == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED!");
		}
		CustomerFacade facade = (CustomerFacade) sessions.get(token).getClientFacade();
		try {
			return ResponseEntity.ok(facade.getCouponById(id));
		} catch (CouponDoesntExistException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/customers-by-coupon/{token}/{id}")
	public ResponseEntity<?> getCustomersByCoupon(@PathVariable String token, @PathVariable int id) {
		CustomerFacade facade = (CustomerFacade) sessions.get(token).getClientFacade();
		try {
			return ResponseEntity.ok(facade.getCustomersByCoupon(id));
		} catch (CouponDoesntExistException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@GetMapping("/all-coupons/{token}")
	public ResponseEntity<?> getAllCoupons(@PathVariable String token){
		Session session = sessions.get(token);
		if(session == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED!");
		}
		CustomerFacade facade = (CustomerFacade) session.getClientFacade();
		return ResponseEntity.ok(facade.getAllCoupons());
	}
	@PostMapping("/purchase-coupon/{token}")
	public ResponseEntity<?> purchaseCoupon(@PathVariable String token,@RequestBody Coupon coupon){
		Session session = sessions.get(token);
		if(session == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED!");
		}
		CustomerFacade facade = (CustomerFacade) session.getClientFacade();
		try {
			facade.purchaseCoupon(coupon);
			return ResponseEntity.ok("Purchased!");
		} catch (CouponAlreadyPurchasedException | CouponOutOfStockException | CouponExpiredException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
