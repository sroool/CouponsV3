package com.example.CouponsV3.facades;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.CouponsV3.beans.Category;
import com.example.CouponsV3.beans.Coupon;
import com.example.CouponsV3.beans.Customer;
import com.example.CouponsV3.exceptions.CouponAlreadyPurchasedException;
import com.example.CouponsV3.exceptions.CouponDoesntExistException;
import com.example.CouponsV3.exceptions.CouponExpiredException;
import com.example.CouponsV3.exceptions.CouponOutOfStockException;
import com.example.CouponsV3.exceptions.CustomerDoesntExistException;
import com.example.CouponsV3.facades.ClientFacade;

/**
 * Client Facade child class 
 * This class extends the ClientFacade class and represent a client with Customer privileges.
 */
@Service
@Scope("prototype")
public class CustomerFacade extends ClientFacade {
	private int customerId;
	
	/**
	 * CustomerFacade CTOR
	 * This constructor will be used to create a new CustomerFacade instance that will allow Customer privileges
	 * 
	 * @throws  
	 */
	public CustomerFacade()  {
	
	}
	/**
	 * Returns true if the email and password given as arguments match the a customer credentials in the database
	 * 
	 * @param email The The email credential for a customer login
	 * @param password The password credential for a customer login
	 * @return true if the credentials are correct, false otherwise
	 */
	@Override
	public boolean login(String email, String password)  {
		Customer customer = custRepo.findByEmailAndPassword(email, password).orElse(null);
		if(customer != null) {
			this.customerId = customer.getId();
			return true;
		}
		return false;
	}
	/**
	 * Accepts a coupon as an argument and adds it as a purchase made by the customer
	 * 
	 * @param coupon the coupon to purchase
	 * 
	 * @throws CouponAlreadyPurchasedException if the customer already has a coupon with the same id
	 *  
	 * @throws CouponOutOfStockException if the coupon amount is 0
	 * @throws CouponExpiredException if the coupon end date passed
	 */
	public void purchaseCoupon(Coupon coupon) throws CouponAlreadyPurchasedException,  CouponOutOfStockException, CouponExpiredException {
		/**
		 * cant purchase coupon with amount 0 so perform a check and throw an exception if so.
		 * **check concurrency issues**
		 */
		if(coupon.getAmount() == 0) { 
			throw new CouponOutOfStockException("This coupon ran out :" + coupon.getId());
		}
		/**
		 * cant purchase an expired coupon 
		 * compare the current time to the coupon end date and throw an exepction
		 * if the date passed(is in the past)
		 */
		Calendar now = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(coupon.getEndDate());
		if(endDate.before(now) && endDate.get(Calendar.DATE) != now.get(Calendar.DATE)) {
			throw new CouponExpiredException("This coupon already expired on " + coupon.getEndDate());
		}
		/**
		 * cant purchase a coupon already purchased before,
		 * get all the customer coupons and compare the id's
		 * if a match is found throw an exception.
		 */
		
		for(Coupon coup : getCustomerCoupons()) {
			if(coupon.getId() == coup.getId()) {
				throw new CouponAlreadyPurchasedException("You already bought this coupon!");
			}
		}
		Customer customer = custRepo.findById(this.customerId).orElse(null);
		if(customer != null) {
			customer.getCoupons().add(coupon);
			custRepo.save(customer);
			coupon.setAmount(coupon.getAmount()-1);
			coupRepo.save(coupon);
		}
		
	}
	/**
	 * Returns all the coupons of the customer
	 * @return all the coupons of the customer
	 *  
	 */
	public Set<Coupon> getCustomerCoupons()  {
		Customer customer = custRepo.findById(this.customerId).orElse(null);
		return customer.getCoupons();
		
	}
	/**
	 * Accepts a category as an argument and returns all the customer coupons of that category
	 * 
	 * @param category the category to check
	 * @return all the customer coupons of that category
	 *  
	 */
	public Set<Coupon> getCustomerCoupons(Category category)  {
		return coupRepo.findByCustomerIdAndCategory(this.customerId, category.ordinal());
	}
	/**
	 * Accepts a maxPrice as an argument and returns all the customer coupons with a price up to that value.
	 * 
	 * @param maxPrice the max price to check by
	 * @return all the customer coupons up to that price
	 * @throws 
	 */
	public Set<Coupon> getCustomerCoupons(double maxPrice)  {
		return coupRepo.findByCustomerIdAndMaxprice(this.customerId, maxPrice);
	}
	/**
	 * Returns the currently logged in customer
	 * 
	 * @return the currently logged in customer 
	 * @throws CustomerDoesntExistException 
	 */
	public Customer getCustomerDetails() {
		Customer customer = custRepo.findById(this.customerId).orElse(null);
		return customer;
	}
	
	public Coupon getCouponById(int id) throws CouponDoesntExistException {
		Coupon coupon = coupRepo.findById(id).orElse(null);
		if(coupon == null) {
			throw new CouponDoesntExistException("Coupon doesnt exist");
		}
		return coupon;
	}
	
	public List<Customer> getCustomersByCoupon(int couponId) throws CouponDoesntExistException{
		Coupon coupon = coupRepo.findById(couponId).orElse(null);
		if(coupon == null) {
			throw new CouponDoesntExistException("Coupon doesnt exist");
		}
		List<Customer> customers = custRepo.findAll();
		List<Customer> customersByCoupon = new ArrayList<Customer>();
		for(Customer c : customers) {
			for(Coupon coup : c.getCoupons()) {
				if(coup.getId() == coupon.getId()) {
					customersByCoupon.add(c);
				}
			}
		}
		return customersByCoupon;
	}
	public List<Coupon> getAllCoupons(){
		return coupRepo.findAll();
	}
	public List<Coupon> getAllCouponsByCategory(Category category){
		List<Coupon> allCoupons = coupRepo.findAll();
		List<Coupon> categoryCoupons = new ArrayList<Coupon>();
		for(Coupon coup : allCoupons) {
			if(coup.getCategory().ordinal() == category.ordinal()) {
				categoryCoupons.add(coup);
			}
		}
		return categoryCoupons;
	}
}
