package com.example.CouponsV3.facades;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.CouponsV3.beans.Company;
import com.example.CouponsV3.beans.Coupon;
import com.example.CouponsV3.beans.Customer;
import com.example.CouponsV3.exceptions.CompanyDoesntExistException;
import com.example.CouponsV3.exceptions.CompanyEmailAlreadyExistsException;
import com.example.CouponsV3.exceptions.CompanyNameAlreadyExistsException;
import com.example.CouponsV3.exceptions.CompanyNameChangedException;
import com.example.CouponsV3.exceptions.CustomerDoesntExistException;
import com.example.CouponsV3.exceptions.CustomerEmailAlreadyExistsException;

/**
 * Client Facade child class 
 * This class extends the ClientFacade class and represent a client with administrative privileges allowing for creation
 * updating and deleting companies and customers
 */
@Service
@Scope("prototype")
public class AdminFacade extends ClientFacade {
	private static final String EMAIL = "admin@admin.com"; // A constant holding email credential for an admin login
	private static final String PASSWORD = "admin"; // A constant holding the password credential for an admin login
	/**
	 * AdminFacade CTOR
	 * This constructor will be used to create a new AdminFacade instance that will allow administrative privileges
	 */
	public AdminFacade()  {}
	/**
	 * Returns true if the email and password given as arguments match the admin credentials 
	 * 
	 * @param email The The email credential for an admin login
	 * @param password The password credential for an admin login
	 * @return true if the credentials are correct, false otherwise
	 */
	@Override
	public boolean login(String email, String password) {
		
		return email.equals(EMAIL) && password.equals(PASSWORD);

	}
	/**
	 * Accepts an instance of a company as an argument and adds it to the database
	 * 
	 * @param company The instance of the company to add to the database
	 * @return The newly added company with updated id
	 * @throws CompanyNameAlreadyExistsException if a company with this name already exists
	 * @throws CompanyEmailAlreadyExistsException if a company with this email already exists
	 */
	public Company addCompany(Company company) throws  CompanyNameAlreadyExistsException, CompanyEmailAlreadyExistsException {
		
		for(Company comp : compRepo.findAll()) { // Get all the companies from the database and loop over them
			// check if company accepted as an argument has the same name as any of them, throw an exception if so
			if(company.getName().equals(comp.getName())) { 
				throw new CompanyNameAlreadyExistsException("A company with this name already exists in the database " + company.getName());
			}
			if(company.getEmail().equals(comp.getEmail())) {
				// check if company accepted as an argument has the same email as any of them, throw an exception if so
				throw new CompanyEmailAlreadyExistsException("A company with this email already exists in the database " + company.getEmail());
			}
		}
		return compRepo.save(company); // otherwise add to the database
	}
	/**
	 * Accepts an instance of a company as an argument and updates it in the database
	 * 
	 * @param company The instance of the company to update in the database
	 * @return The updated company with updated data
	 * @throws CompanyDoesntExistException if a company with this id doesnt exist
	 * @throws CompanyNameChangedException if a company name change was attempted
	 * @throws CompanyEmailAlreadyExistsException 
	 */
	public Company updateCompany(Company company) throws  CompanyDoesntExistException, CompanyNameChangedException, CompanyEmailAlreadyExistsException{
		Company comp = compRepo.findById(company.getId()).orElse(null); // loads the company from the database by its id
		Company comp2 = compRepo.findByEmail(company.getEmail()).orElse(null);
		if(comp == null) {
			throw new CompanyDoesntExistException("Error: no company with id " + company.getId());
		}
		if(!(company.getName().equals(comp.getName()))) { // check if the company name has been altered 
			throw new CompanyNameChangedException("Cant change company name from ["+comp.getName()+"] to ["+company.getName()+"]");
		}
		if(comp2 != null && comp2.getId() != company.getId()) {
			throw new CompanyEmailAlreadyExistsException("A company with that email already exists");
		}
		return compRepo.save(company); // Update the company to the database if not
	
	}
	/**
	 * Accepts a companyId and deletes it from the database
	 * 
	 * @param companyId the id of the company to delete
	 * @throws CompanyDoesntExistException 
	 */
	public void deleteCompany(int companyId) throws CompanyDoesntExistException {
		Company company = compRepo.findById(companyId).orElse(null);
		if(company == null) {
			throw new CompanyDoesntExistException("Error: no company with id " + companyId);
		}
		coupRepo.deletePurchasesByCompanyId(companyId);
		coupRepo.deleteByCompanyId(companyId);
		
		compRepo.deleteById(companyId);
	}

	/**
	 * Returns all the companies in the database
	 * @return all the companies in the database
	 */
	public List<Company> getAllCompanies(){
		return compRepo.findAll();
	}

	/**
	 * Accepts a company id and returns it from the database
	 *  
	 * @param companyId the id of the company to return 
	 * @return The company with the given id saved in the database
	 * @throws SQLException
	 * @throws CompanyDoesntExistException if a company with the provided id could not be found
	 */
	public Company getOneCompany(int companyId) throws CompanyDoesntExistException {
		Company company = compRepo.findById(companyId).orElse(null); // get the company by id
		if(company == null) {
			throw new CompanyDoesntExistException("Error: Company with id " + companyId + " doesnt exist");
		}
		return company;
	}
	/**
	 * Accepts an instance of a customer as an argument and adds it to the database 
	 *  
	 * @param customer The instance of the customer to add to the database
	 * @return The newly added customer with updated id
	 * @throws CustomerEmailAlreadyExistsException if customer in the database already has this email
	 */
	public Customer addCustomer(Customer customer) throws  CustomerEmailAlreadyExistsException {
		Customer cust = custRepo.findByEmail(customer.getEmail()).orElse(null);
		if(cust != null) {
			
			throw new CustomerEmailAlreadyExistsException("this customer email already exists in the database " + customer.getEmail());
		}
		return custRepo.save(customer); // otherwise add to the database
	}
	/**
	 * Accepts an instance of a customer as an argument and updates it in the database
	 * 
	 * @param customer The instance of the customer to update in the database
	 * @return The updated customer with updated data
	 * @throws CustomerDoesntExistException if a customer with this id doesnt exist
	 * @throws CustomerEmailAlreadyExistsException 
	 */
	public Customer updateCustomer(Customer customer) throws CustomerDoesntExistException, CustomerEmailAlreadyExistsException {
		Customer cust = custRepo.findById(customer.getId()).orElse(null); // loads the company from the database by its id
		Customer custByEmail = custRepo.findByEmail(customer.getEmail()).orElse(null);
		if(cust == null) { // check that the cust reference isnt null
			throw new CustomerDoesntExistException("Error: No customer with id " + customer.getId());
		}
		if(custByEmail != null && custByEmail.getId() != customer.getId()) {
			throw new CustomerEmailAlreadyExistsException("this customer email already exists " + customer.getEmail());
		}
		return custRepo.save(customer); // update the customer in the database
	}
	/**
	 * Accepts a customerId and deletes it from the database
	 * 
	 * @param customerId the id of the customer to delete
	 * @throws CustomerDoesntExistException 
	 */
	public void deleteCustomer(int customerId) throws CustomerDoesntExistException {
		Customer customer = custRepo.findById(customerId).orElse(null);
		if(customer == null) {
			throw new CustomerDoesntExistException("Error: Customer with id " + customerId + "doesnt exist");
		}
		for(Coupon coupon : customer.getCoupons()) {
			coupon.increaseCurrentAmount();
			coupRepo.save(coupon);
		}
		coupRepo.deletePurchaseByCustomerId(customerId);
		custRepo.deleteById(customerId); // delete the customer
	}
	/**
	 * Returns all the customers in the database
	 * @return all the customers in the database
	 */
	public List<Customer> getAllCustomers(){
		return custRepo.findAll();
	}
	/**
	 * Accepts a customer id and returns it from the database
	 *  
	 * @param customerId the id of the customer to return 
	 * @return The customer with the given id saved in the database
	 * @throws CustomerDoesntExistException if a customer with the provided id could not be found
	 */
	public Customer getOneCustomer(int customerId) throws  CustomerDoesntExistException {
		Customer customer = custRepo.findById(customerId).orElse(null); // Get the customer by id
		if(customer == null) {
			throw new CustomerDoesntExistException("Error: customer with id " + customerId + " doesnt exist");
		}
		return customer;
	}
	/**
	 * Helper method to reset the auto increment of all the tables in the db.
	 * for testing.
	 * @param start the value to reset to
	 * @throws SQLException
	 */
	public void resetAutoIncrement(int start) {
		compRepo.resetAutoIncrement(start);
		coupRepo.resetAutoIncrement(start);
		custRepo.resetAutoIncrement(start);
	}

}