/**
 * 
 */
package com.example.CouponsV3.web;

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
import com.example.CouponsV3.beans.Customer;
import com.example.CouponsV3.exceptions.CompanyDoesntExistException;
import com.example.CouponsV3.exceptions.CompanyEmailAlreadyExistsException;
import com.example.CouponsV3.exceptions.CompanyNameAlreadyExistsException;
import com.example.CouponsV3.exceptions.CompanyNameChangedException;
import com.example.CouponsV3.exceptions.CustomerDoesntExistException;
import com.example.CouponsV3.exceptions.CustomerEmailAlreadyExistsException;
import com.example.CouponsV3.facades.AdminFacade;

/**
 * @author israe
 *
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
	@Autowired
	private AdminFacade facade;

	@PostMapping("/add-company/{token}")
	public ResponseEntity<?> addCompany(@PathVariable String token, @RequestBody Company company) {
		System.out.println(company);
		try {
			return ResponseEntity.ok(facade.addCompany(company));
		} catch (CompanyNameAlreadyExistsException | CompanyEmailAlreadyExistsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/update-company/{token}")
	public ResponseEntity<?> updateCompany(@PathVariable String token, @RequestBody Company company) {
		System.out.println(company);
		try {
			return ResponseEntity.ok(facade.updateCompany(company));
		} catch (CompanyEmailAlreadyExistsException | CompanyDoesntExistException | CompanyNameChangedException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/delete-company/{token}/{id}")
	public ResponseEntity<?> deleteCompany(@PathVariable String token, @PathVariable int id) {
		try {
			facade.deleteCompany(id);
			return ResponseEntity.ok("Deleted");
		} catch (CompanyDoesntExistException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/get-all-companies/{token}")
	public ResponseEntity<?> getAllCompanies(@PathVariable String token) {
		return ResponseEntity.ok(facade.getAllCompanies());
	}

	@PostMapping("/add-customer/{token}")
	public ResponseEntity<?> addCustomer(@PathVariable String token, @RequestBody Customer customer) {
		System.out.println(customer);
		try {
			return ResponseEntity.ok(facade.addCustomer(customer));
		} catch (CustomerEmailAlreadyExistsException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PostMapping("/update-customer/{token}")
	public ResponseEntity<?> updateCustomer(@PathVariable String token, @RequestBody Customer customer) {
		System.out.println(customer);
		try {
			return ResponseEntity.ok(facade.updateCustomer(customer));
		} catch (CustomerDoesntExistException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/delete-customer/{token}/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable String token, @PathVariable int id) {
		try {
			facade.deleteCustomer(id);
			return ResponseEntity.ok("Deleted");
		} catch (CustomerDoesntExistException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/get-all-customers/{token}")
	public ResponseEntity<?> getAllCustomers(@PathVariable String token) {
		return ResponseEntity.ok(facade.getAllCustomers());
	}
}
