package com.example.CouponsV3.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.CouponsV3.beans.Company;
import com.example.CouponsV3.beans.Coupon;
import com.example.CouponsV3.exceptions.CouponCompanyIdChangedException;
import com.example.CouponsV3.exceptions.CouponDoesntExistException;
import com.example.CouponsV3.exceptions.CouponTitleAlreadyExistsException;
import com.example.CouponsV3.facades.ClientFacade;
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
		if (session != null) {
			CompanyFacade facade = (CompanyFacade) session.getClientFacade();
			Company company = facade.getCompanyDetails();
			return ResponseEntity.ok(company);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED!");
	}

	@PostMapping("/add-coupon/{token}")
	public ResponseEntity<?> addCoupon(@PathVariable String token, @RequestBody Coupon coupon) {
		try {
			CompanyFacade facade = (CompanyFacade) sessions.get(token).getClientFacade();
			return ResponseEntity.ok(facade.addCoupon(coupon));
		} catch (CouponTitleAlreadyExistsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PostMapping("/add-coupon-image/{token}")
	public String uploadImage(@PathVariable String token, @RequestParam("imageFile") MultipartFile imageFile) {
		String returnValue = "";
		String folder = "src\\main\\resources\\static\\assets\\images\\";
		try {
			byte[] bytes = imageFile.getBytes();
			Path path = Paths.get(folder + imageFile.getOriginalFilename());
			Files.write(path, bytes);
			returnValue = "image saved";
		} catch (IOException e) {
			returnValue = "failure : " + e.getMessage();
		}
		System.out.println(returnValue);
		return returnValue;

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
