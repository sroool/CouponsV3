package com.example.CouponsV3.facades;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.CouponsV3.beans.Category;
import com.example.CouponsV3.beans.Company;
import com.example.CouponsV3.beans.Coupon;
import com.example.CouponsV3.beans.Customer;
import com.example.CouponsV3.exceptions.CompanyDoesntExistException;
import com.example.CouponsV3.exceptions.CouponCompanyIdChangedException;
import com.example.CouponsV3.exceptions.CouponDoesntExistException;
import com.example.CouponsV3.exceptions.CouponTitleAlreadyExistsException;


/**
 * Client Facade child class 
 * This class extends the ClientFacade class and represent a client with Company privileges allowing for creation
 * updating and deleting coupons.
 */
@Service
@Scope("prototype")
public class CompanyFacade extends ClientFacade {
	private Company company; // The currently logged in company id
	
	/**
	 * CompanyFacade CTOR
	 * This constructor will be used to create a new CompanyFacade instance that will allow Company privileges
	 * 
	 */
	public CompanyFacade()  {
		super();
	}
	/**
	 * Returns true if the email and password given as arguments match the a company credentials in the database
	 * 
	 * @param email The The email credential for a Company login
	 * @param password The password credential for a Company login
	 * @return true if the credentials are correct, false otherwise
	 */
	@Override
	public boolean login(String email, String password){
		Company comp = compRepo.findByEmailAndPassword(email, password).orElse(null); // use the company repo to query a company with this email and password
		if(comp != null) { // check if the Optional object contains the company
			company = comp; // if so use the get method to get the company and then its id
			return true; // return true for correct login
		}
		return false; // return false in case no corresponding company was found
	}

	/**
	 * Accepts an instance of a coupon as an argument and adds it to the database
	 * 
	 * @param coupon The instance of the coupon to add to the database
	 * @return 
	 * @throws CouponTitleAlreadyExistsException if a coupon with this title already exists
	 */
	public Coupon addCoupon(Coupon coupon) throws CouponTitleAlreadyExistsException {
		for(Coupon coup : getCompanyCoupons()) { // Get all the company coupons and loop over them
			/**
			 * check if the coupon retrieved title the same as the coupon passed as an argument.
			 */
			if( coupon.getTitle().equals(coup.getTitle())) {
				//throw an exception if so
				throw new CouponTitleAlreadyExistsException("A coupon with this title already exists");
			}
		}
		return coupRepo.save(coupon);// otherwise add the coupon
	}
	
	/**
	 * Accepts an instance of a coupon as an argument and updates it in the database
	 * 
	 * @param coupon The instance of the coupon to update in the database
	 * @return 
	 * @throws CouponDoesntExistException if a coupon id with this id doesnt exist
	 * @throws CouponCompanyIdChangedException if a coupon company id change was attempted
	 * @throws CouponTitleAlreadyExistsException 
	 */
	public Coupon updateCoupon(Coupon coupon) throws  CouponCompanyIdChangedException, CouponDoesntExistException, CouponTitleAlreadyExistsException {
		
		Coupon coup = coupRepo.findById(coupon.getId()).orElse(null); // loads the coupon optional from the database by its id
		Coupon coupByTitle = coupRepo.findByTitle(coupon.getTitle()).orElse(null);
		if(coup == null) {
			throw new CouponDoesntExistException("Coupon doesnt Exist");
		}
		if(coup.getCompanyId() != coupon.getCompanyId()) {			
			throw new CouponCompanyIdChangedException("Cant change coupon company id from ["+coup.getCompanyId()+"] to ["+coupon.getCompanyId()+"]");
		}
		if(coupByTitle != null && coupByTitle.getId() != coupon.getId()) {
			throw new CouponTitleAlreadyExistsException("A coupon with this title already exists");
		}
		return coupRepo.save(coupon); // Update the coupon to the database if not
	}
	
	/**
	 * Accepts a couponId and deletes it from the database
	 * 
	 * @param companyId the id of the coupon to delete
	 * @throws CouponDoesntExistException 
	 */
	public void deleteCoupon(int couponId) throws CouponDoesntExistException  {
		Coupon coupon = coupRepo.findById(couponId).orElse(null);
		if(coupon == null) {
			throw new CouponDoesntExistException("Coupon doesnt exist");
		}
		coupRepo.deletePurchaseByCouponId(couponId);
		coupRepo.deleteById(couponId); // delete the coupon

	}
	/**
	 * Returns all the company coupons in the database
	 * @return all the company coupons in the database
	 */
	public List<Coupon> getCompanyCoupons() {
		Company company = compRepo.findById(this.company.getId()).orElse(null);
		if(company != null) {
			return company.getCoupons();
		}
		return new ArrayList<Coupon>();
	}
	/**
	 * Accepts a category as an argument and returns all the company coupons of that category
	 * 
	 * @param category the category to check
	 * @return all the company coupons of that category
	 */
	public List<Coupon> getCompanyCoupons(Category category) {
		return coupRepo.findByCompanyIdAndCategory(this.company.getId(), category);
	}
	
	/**
	 * Accepts a maxPrice as an argument and returns all the company coupons with a price up to that value.
	 * 
	 * @param maxPrice the max price to check by
	 * @return all the company coupons up to that price
	 */
	public List<Coupon> getCompanyCoupons(double maxPrice) {
		return coupRepo.findByCompanyIdAndPriceLessThan(this.company.getId(), maxPrice);
	}
	/**
	 * Returns the currently logged in company
	 * 
	 * @return the currently logged in company
	 * @throws CompanyDoesntExistException
	 */
	public Company getCompanyDetails() {
		Company company = compRepo.findById(this.company.getId()).orElse(null);
		if(company != null) {
			this.company = company;
		}
		return this.company;
	}
	
	public Set<Customer> getAllClients(){
		List<Customer> customers = custRepo.findAll();
		Set<Customer> clients = new HashSet<Customer>();
		for(Customer customer : customers) {
			for(Coupon coupon : customer.getCoupons()) {
				if(coupon.getCompanyId() == this.company.getId()) {
					clients.add(customer);
				}
			}
		}
		return clients;
	}
}
