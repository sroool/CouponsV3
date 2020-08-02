package com.example.CouponsV3.web;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.CouponsV3.beans.Company;
import com.example.CouponsV3.beans.Coupon;
import com.example.CouponsV3.exceptions.CouponCompanyIdChangedException;
import com.example.CouponsV3.exceptions.CouponDoesntExistException;
import com.example.CouponsV3.exceptions.CouponTitleAlreadyExistsException;
import com.example.CouponsV3.facades.CompanyFacade;

@RestController
@RequestMapping("/company")
@CrossOrigin(origins = "http://localhost:4200")
public class CompanyController {
	
	@Autowired
	private Map<String, Session> sessions;

	@GetMapping("/company-details/{token}")
	public ResponseEntity<?> getCompanyDetails(@PathVariable String token) {
		Session session = sessions.get(token);
		CompanyFacade facade = (CompanyFacade) session.getClientFacade();
		Company company = facade.getCompanyDetails();
		return ResponseEntity.ok(company);
		
	}

	@PostMapping("/add-coupon/{token}")
	public ResponseEntity<?> addCoupon(@PathVariable String token, @RequestBody Coupon coupon) {
	
			CompanyFacade facade = (CompanyFacade) sessions.get(token).getClientFacade();
			try {
				return ResponseEntity.ok(facade.addCoupon(coupon));
			} catch (CouponTitleAlreadyExistsException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		
	}
	@PostMapping("/update-coupon/{token}")
	public ResponseEntity<?> updateCoupon(@PathVariable String token, @RequestBody Coupon coupon) {
		try {
			CompanyFacade facade = (CompanyFacade) sessions.get(token).getClientFacade();
			return ResponseEntity.ok(facade.updateCoupon(coupon));
		} catch (CouponCompanyIdChangedException | CouponDoesntExistException | CouponTitleAlreadyExistsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/delete-coupon/{token}/{id}")
	public ResponseEntity<?> deleteCoupon(@PathVariable String token, @PathVariable int id) {
		try {
			CompanyFacade facade = (CompanyFacade) sessions.get(token).getClientFacade();
			facade.deleteCoupon(id);
			return ResponseEntity.ok("Deleted!");
		} catch (CouponDoesntExistException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/all-clients/{token}")
	public ResponseEntity<?> getAllClients(@PathVariable String token) {

		CompanyFacade facade = (CompanyFacade) sessions.get(token).getClientFacade();
		return ResponseEntity.ok(facade.getAllClients());

	}
}
