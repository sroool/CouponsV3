
package com.example.CouponsV3;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.example.CouponsV3.beans.Category;
import com.example.CouponsV3.beans.Company;
import com.example.CouponsV3.beans.Coupon;
import com.example.CouponsV3.beans.Customer;
import com.example.CouponsV3.exceptions.CompanyDoesntExistException;
import com.example.CouponsV3.exceptions.CompanyEmailAlreadyExistsException;
import com.example.CouponsV3.exceptions.CompanyNameAlreadyExistsException;
import com.example.CouponsV3.exceptions.CompanyNameChangedException;
import com.example.CouponsV3.exceptions.CouponAlreadyPurchasedException;
import com.example.CouponsV3.exceptions.CouponCompanyIdChangedException;
import com.example.CouponsV3.exceptions.CouponDoesntExistException;
import com.example.CouponsV3.exceptions.CouponExpiredException;
import com.example.CouponsV3.exceptions.CouponOutOfStockException;
import com.example.CouponsV3.exceptions.CouponTitleAlreadyExistsException;
import com.example.CouponsV3.exceptions.CustomerDoesntExistException;
import com.example.CouponsV3.exceptions.CustomerEmailAlreadyExistsException;
import com.example.CouponsV3.exceptions.IncorrectCredentialsException;
import com.example.CouponsV3.facades.AdminFacade;
import com.example.CouponsV3.facades.ClientType;
import com.example.CouponsV3.facades.CompanyFacade;
import com.example.CouponsV3.facades.CustomerFacade;
import com.example.CouponsV3.facades.LoginManager;
import com.example.CouponsV3.jobs.CouponExpirationDailyJob;
@Service
public class Test {
	@Autowired
	private ConfigurableApplicationContext ctx;
	
	public void testAll() {
		
		CouponExpirationDailyJob job = ctx.getBean(CouponExpirationDailyJob.class);
		Thread thread = new Thread(job);
		thread.setDaemon(true);
		thread.start();
		try {
			System.out.println("\n\n===CLEARING DB=========================================\n\n");
			ClearDB();
			System.out.println("\n\n===TESTING ADMIN=======================================\n\n");
			testAdmin();
			System.out.println("\n\n==TESTING COMPANY======================================\n\n");
			testCompany();
			System.out.println("\n\n==TESTING CUSTOMER=====================================\n\n");
			testCustomer();
			System.out.println("\n\n==TESTING COMBINATIONS=================================\n\n");
			testCombined();
			System.out.println("\n\n\n =========DONE TESTS========= \n\n\n");

		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());

		} finally {
			job.stop();

			thread.interrupt();
			try {

				thread.join();
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Helper method to clear the db before each test and reset the auto increment
	 * of every table to 1. to remove also delete the methods from the DAO's,
	 * DBDAO's and admin facade.
	 * 
	 * @throws SQLException
	 * @throws IncorrectCredentialsException 
	 */
	public  void ClearDB() throws  SQLException, IncorrectCredentialsException {
		LoginManager manager = ctx.getBean(LoginManager.class);
		AdminFacade admin = (AdminFacade) manager.login("admin@admin.com", "admin", ClientType.Administrator);
		
		try {
			for (Company company : admin.getAllCompanies()) {
				admin.deleteCompany(company.getId());
			}
			for (Customer customer : admin.getAllCustomers()) {
				admin.deleteCustomer(customer.getId());
			}
		
			admin.resetAutoIncrement(1);
		}catch(Exception e) {}
	}

	public void testCombined() throws SQLException, CompanyDoesntExistException, CouponTitleAlreadyExistsException, InterruptedException, IncorrectCredentialsException {
		LoginManager manager = ctx.getBean(LoginManager.class);
		System.out.println("Trying to login as admin with correct credentials, should return AdminFacade instance:");
		AdminFacade admin = (AdminFacade) manager.login("admin@admin.com", "admin", ClientType.Administrator);
		if(!(admin == null)) {
			System.out.println("SUCCESS!!");
		}else {
			System.out.println("FAILURE!!");
		}
		System.out.println("trying to delete a company who has coupons and customer bought its coupons:");
		ArrayList<Integer> companyIDS = new ArrayList<Integer>();
		for(Company company : admin.getAllCompanies()) {
			companyIDS.add(company.getId());
		}
		int randomCompanyId = companyIDS.get((int)(Math.random() * companyIDS.size()));
		Company company = admin.getOneCompany(randomCompanyId);
		System.out.println("\n");
		System.out.println(company);
		System.out.println("\n");
		ArrayList<Integer> companyCouponsIndexes = new ArrayList<Integer>();
		for(Coupon coupon : company.getCoupons()) {
			companyCouponsIndexes.add( coupon.getId());
		}
		admin.deleteCompany(company.getId());
		boolean companyCouponsStilExists = false;
		for(Company company2 : admin.getAllCompanies()) {
			for(Coupon coupon : company2.getCoupons()) {
				if(companyCouponsIndexes.contains(coupon.getId())) {
					companyCouponsStilExists = true;
				}
			}
		}
		for(Customer customer : admin.getAllCustomers()) {
			for(Coupon coupon : customer.getCoupons()) {
				if(companyCouponsIndexes.contains(coupon.getId())) {
					companyCouponsStilExists = true;
				}
			}
		}
		if(!companyCouponsStilExists) {
			System.out.println("SUCCESS!!");
		}else {
			System.out.println("FAILURE!!");
		}
		
//		  SET THE JOB SLEEP TO SMALL INTERVAL LIKE 1000* 10 TO SEE THIS EFFECT BETTER
//		 
//		  System.out.println("--testing the job thread--");
//		Company company2 = admin.getAllCompanies().get(0);
//		System.out.println("trying to login as company :" + company2.getId());
//		CompanyFacade compF = (CompanyFacade)manager.login(company2.getEmail(), company2.getPassword(), ClientType.Company);
//		if(!(compF == null)) {
//			System.out.println("SUCCESS!!");
//		}else {
//			System.out.println("FAILURE!!");
//		}
//		Coupon coupon1 = randomCoupon(company2.getId());
//		Coupon coupon2 = randomCoupon(company2.getId());
//		Coupon coupon3 = randomCoupon(company2.getId());
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.DATE, 1);
//		coupon1.setEndDate(new Date(cal.getTimeInMillis()));
//		coupon2.setEndDate(new Date(cal.getTimeInMillis()));
//		coupon3.setEndDate(new Date(cal.getTimeInMillis()));
//		compF.addCoupon(coupon1);
//		compF.addCoupon(coupon2);
//		compF.addCoupon(coupon3);
//		for(Coupon coupon : compF.getCompanyCoupons()) {
//			if(coupon.getTitle().equals(coupon1.getTitle()) ||coupon.getTitle().equals(coupon2.getTitle()) || coupon.getTitle().equals(coupon3.getTitle())) {
//				System.out.println(coupon);
//			}
//		}
//		Thread.sleep(1000 * 20);
//		boolean expiredCouponsExists = false;
//		for(Coupon coupon : compF.getCompanyCoupons()) {
//			if(coupon.getTitle().equals(coupon1.getTitle()) ||coupon.getTitle().equals(coupon2.getTitle()) || coupon.getTitle().equals(coupon3.getTitle())) {
//				expiredCouponsExists = true;
//			}
//		}
//		if(!(expiredCouponsExists)) {
//			System.out.println("SUCCESS!!");
//		}else {
//			System.out.println("FAILURE!!");
//		}
		
		
		
	}

	public void testCustomer() throws SQLException, CouponTitleAlreadyExistsException,
			CompanyDoesntExistException, CouponAlreadyPurchasedException, CouponOutOfStockException,
			CouponExpiredException, CustomerDoesntExistException, IncorrectCredentialsException {
		LoginManager manager = ctx.getBean(LoginManager.class);
		System.out.println("Trying to login as admin with correct credentials, should return AdminFacade instance:");
		AdminFacade admin = (AdminFacade) manager.login("admin@admin.com", "admin", ClientType.Administrator);
		if(!(admin == null)) {
			System.out.println("SUCCESS!!");
		}else {
			System.out.println("FAILURE!!");
		}
		for(Customer customer : admin.getAllCustomers()) {
			System.out.println("Trying to login as customer :" + customer.getId());
			
			CustomerFacade facade = (CustomerFacade) manager.login(customer.getEmail(), customer.getPassword(),
					ClientType.Customer);
			if(!(facade == null)) {
				System.out.println("SUCCESS!!");
			}else {
				System.out.println("FAILURE!!");
			}
			Company company = admin.getAllCompanies().get((int)(Math.random() * admin.getAllCompanies().size()));
			
			System.out.println("trying to login as company :" + company.getId());
			CompanyFacade companyFacade = (CompanyFacade) manager.login(company.getEmail(), company.getPassword(),
					ClientType.Company);
			if (!(companyFacade == null)) {
				System.out.println("SUCCESS!!");
			} else {
				System.out.println("FAILURE!!");
			}
			List<Coupon> coupons = companyFacade.getCompanyCoupons();
//			if (coupons.size() == 0) {
//				for (int i = 0; i < 5; i++) {
//					companyFacade.addCoupon(randomCoupon(companyFacade.getCompanyDetails().getId()));
//				}
//				coupons = companyFacade.getCompanyCoupons();
//			}
			for(Coupon coupon : coupons) {
				
				facade.purchaseCoupon(coupon);
				//System.out.println("coupons : " + facade.getCustomerCoupons());
			}
			System.out.println("trying to get coupons by category[Furniture]:");
			int furnitureCoupons = 0;
			for(Coupon coupon : facade.getCustomerCoupons()) {
				if(coupon.getCategory().ordinal() == Category.Furniture.ordinal()) {
					furnitureCoupons++;
				}
			}
			if(facade.getCustomerCoupons(Category.Furniture).size() == furnitureCoupons) {
				System.out.println("SUCCESS!!");
			}else {
				System.out.println("FAILURE!!");
			}
			System.out.println("trying to get coupons by max price[200]:");
			int maxPrice200 = 0;
			for (Coupon coupon : coupons) {
				if(coupon.getPrice() <= 200) {
					maxPrice200++;
				}
			}
			if(facade.getCustomerCoupons(200).size() == maxPrice200) {
				System.out.println("SUCCESS!!");
			}else {
				System.out.println("FAILURE!!");
			}
			System.out.println(facade.getCustomerDetails());
		}

	}

	public void testCompany()
			throws SQLException, CouponTitleAlreadyExistsException,
			CouponCompanyIdChangedException, CouponDoesntExistException, CompanyDoesntExistException, IncorrectCredentialsException {
		LoginManager manager = ctx.getBean(LoginManager.class);
		System.out.println("Trying to login as admin with correct credentials, should return AdminFacade instance:");
		AdminFacade admin = (AdminFacade) manager.login("admin@admin.com", "admin", ClientType.Administrator);
		if(!(admin == null)) {
			System.out.println("SUCCESS!!");
		}else {
			System.out.println("FAILURE!!");
		}
		for (Company company : admin.getAllCompanies()) {
			System.out.println("trying to login as company :" + company.getId());
			CompanyFacade facade = (CompanyFacade) manager.login(company.getEmail(), company.getPassword(),
					ClientType.Company);
			if (!(facade == null)) {
				System.out.println("SUCCESS!!");
			} else {
				System.out.println("FAILURE!!");
			}
			
			System.out.println("Trying to add 10 coupons as company[" + company.getName() + "]:");
			List<Coupon> couponsFromDB = facade.getCompanyCoupons();
			ArrayList<Coupon> couponsToDB = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				Coupon random = randomCoupon(company.getId());
				//System.out.println(random);
				facade.addCoupon(random);
				couponsToDB.add(random);
			}
			int found10 = 0;
			couponsFromDB = facade.getCompanyCoupons();
			for (Coupon coupon : couponsToDB) {
				for (Coupon coupon2 : couponsFromDB) {
					if (coupon.getTitle().equals(coupon2.getTitle())) {
						found10++;
					}
				}
			}
			if (found10 == 10) {
				System.out.println("SUCCESS!!");
			} else {
				System.out.println("FAILURE!!");
			}
			System.out.println("trying to update 5 coupons:");
			couponsFromDB = facade.getCompanyCoupons();
			for (int i = 0; i < 10; i++) {
				if (i % 2 == 0) {
					Coupon update = couponsFromDB.get(i);
					update.setAmount(-666);
					facade.updateCoupon(update);
				}
			}
			int found5 = 0;
			couponsFromDB = facade.getCompanyCoupons();
			for (Coupon coupon : couponsFromDB) {
				if (coupon.getAmount() == -666) {
					found5++;
				}
			}
			if (found5 == 5) {
				System.out.println("SUCCESS!!");
			} else {
				System.out.println("FAILURE!!");
			}
			System.out.println("trying to delete 5 coupons:");
			couponsFromDB = facade.getCompanyCoupons();
			for (Coupon coupon : couponsFromDB) {
				if (coupon.getAmount() == -666) {
					facade.deleteCoupon(coupon.getId());
				}
			}
			couponsFromDB = facade.getCompanyCoupons();
			if (couponsFromDB.size() == 5) {
				System.out.println("SUCCESS!!");
			} else {
				System.out.println("FAILURE!!");
			}
			System.out.println("trying to get coupons by category[food]:");
			int foodCoupons = 0;
			for (Coupon coupon : couponsFromDB) {
				if(coupon.getCategory().ordinal() == Category.Food.ordinal()) {
					foodCoupons++;
				}
			}
			List<Coupon> couponsByFood = facade.getCompanyCoupons(Category.Food);
			if (couponsByFood.size() == foodCoupons) {
				System.out.println("SUCCESS!!");
			} else {
				System.out.println("FAILURE!!");
			}
			System.out.println("trying to get coupons by max price[200]:");
			int sizeByPrice = 0;
			couponsFromDB = facade.getCompanyCoupons();
			for (Coupon coupon : couponsFromDB) {
				if (coupon.getPrice() <= 200) {
					sizeByPrice++;
				}
			}
			if (sizeByPrice == facade.getCompanyCoupons(200).size()) {
				System.out.println("SUCCESS!!");
			} else {
				System.out.println("FAILURE!!");
			}
			System.out.println(facade.getCompanyDetails());
		}
	}

	public void testAdmin() throws SQLException,
			CompanyNameAlreadyExistsException, CompanyEmailAlreadyExistsException, CompanyDoesntExistException,
			CompanyNameChangedException, CustomerEmailAlreadyExistsException, CustomerDoesntExistException, IncorrectCredentialsException {
		testAdminCompany();
		testAdminCustomer();
	}

	public void testAdminCustomer() throws SQLException,
			CustomerEmailAlreadyExistsException, CustomerDoesntExistException, IncorrectCredentialsException {
		LoginManager manager = ctx.getBean(LoginManager.class);
//		System.out.println("Trying to login as admin with incorrect credentials intentionally, should throw an exception:");
//		AdminFacade falseAdmin = (AdminFacade)manager.login("admin1", "password1", ClientType.Administrator);
//		if(falseAdmin == null) {
//			System.out.println("SUCCESS!!");
//		}else {
//			System.out.println("FAILURE!!");
//		}
		System.out.println("Trying to login as admin with correct credentials, should return AdminFacade instance:");
		AdminFacade admin = (AdminFacade) manager.login("admin@admin.com", "admin", ClientType.Administrator);
		if(!(admin == null)) {
			System.out.println("SUCCESS!!");
		}else {
			System.out.println("FAILURE!!");
		}
		System.out.println("Trying to add 1 customer as admin:");
		Customer random = randomCustomer();
		admin.addCustomer(random);
		boolean found = false;
		List<Customer> customersFromDB = admin.getAllCustomers();
		for (Customer customer : customersFromDB) {
			if (customer.getEmail().equals(random.getEmail())) {
				found = true;
			}
		}
		if (found) {
			System.out.println("SUCCESS!!");
		} else {
			System.out.println("FAILURE!!");
		}
		System.out.println("Trying to add 9 customers as admin:");
		ArrayList<Customer> customersToDB = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			Customer random2 = randomCustomer();
			admin.addCustomer(random2);
			customersToDB.add(random2);
		}
		int found9 = 0;
		customersFromDB = admin.getAllCustomers();
		for (Customer customer : customersToDB) {
			for (Customer customer2 : customersFromDB) {
				if (customer.getEmail().equals(customer2.getEmail())) {
					found9++;
				}
			}
		}
		if (found9 == 9) {
			System.out.println("SUCCESS!!");
		} else {
			System.out.println("FAILURE!!");
		}
		System.out.println("trying to update 5 customers:");
		customersFromDB = admin.getAllCustomers();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				Customer update = customersFromDB.get(i);
				update.setPassword(customersFromDB.get(i).getFirstName() + "654321");
				admin.updateCustomer(update);
			}
		}
		int found5 = 0;
		customersFromDB = admin.getAllCustomers();
		for (Customer customer : customersFromDB) {
			if (customer.getPassword().equals(customer.getFirstName() + "654321")) {
				found5++;
			}
		}
		if (found5 == 5) {
			System.out.println("SUCCESS!!");
		} else {
			System.out.println("FAILURE!!");
		}
		System.out.println("trying to delete 5 customers:");
		customersFromDB = admin.getAllCustomers();
		for (Customer customer : customersFromDB) {
			if (customer.getPassword().equals(customer.getFirstName() + "654321")) {
				admin.deleteCustomer(customer.getId());
			}
		}
		customersFromDB = admin.getAllCustomers();
		if (customersFromDB.size() == 5) {
			System.out.println("SUCCESS!!");
		} else {
			System.out.println("FAILURE!!");
		}
		System.out.println("trying to get 1 customer by id:");
		Customer getOneCustomer = admin.getOneCustomer(6);
		if (getOneCustomer instanceof Customer) {
			System.out.println("SUCCESS!!");
			System.out.println(getOneCustomer);
		} else {
			System.out.println("FAILURE!!");
		}
	}

	public void testAdminCompany()
			throws  SQLException, CompanyNameAlreadyExistsException,
			CompanyEmailAlreadyExistsException, CompanyDoesntExistException, CompanyNameChangedException, IncorrectCredentialsException {
		LoginManager manager = ctx.getBean(LoginManager.class);
//		System.out.println("Trying to login as admin with incorrect credentials intentionally, should return null:");
//		AdminFacade falseAdmin = (AdminFacade)manager.login("admin1", "password1", ClientType.Administrator);
//		if(falseAdmin == null) {
//			System.out.println("SUCCESS!!");
//		}else {
//			System.out.println("FAILURE!!");
//		}
		System.out.println("Trying to login as admin with correct credentials, should return AdminFacade instance:");
		AdminFacade admin = (AdminFacade) manager.login("admin@admin.com", "admin", ClientType.Administrator);
		if(!(admin == null)) {
			System.out.println("SUCCESS!!");
		}else {
			System.out.println("FAILURE!!");
		}
		
		System.out.println("Trying to add 10 companies as admin:");
		List<Company> companiesFromDB = admin.getAllCompanies();
		ArrayList<Company> companiesToDB = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Company random = randomCompany();
			admin.addCompany(random);
			companiesToDB.add(random);
		}
		int found10 = 0;
		companiesFromDB = admin.getAllCompanies();
		for (Company company : companiesToDB) {
			for (Company company2 : companiesFromDB) {
				if (company.getEmail().equals(company2.getEmail())) {
					found10++;
				}
			}
		}
		if (found10 == 10) {
			System.out.println("SUCCESS!!");
		} else {
			System.out.println("FAILURE!!");
		}
		companiesFromDB = admin.getAllCompanies();
		System.out.println("trying to update 5 companies:");
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				Company update = companiesFromDB.get(i);
				update.setPassword(companiesFromDB.get(i).getName() + "654321");
				admin.updateCompany(update);
			}
		}
		int found5 = 0;
		companiesFromDB = admin.getAllCompanies();
		for (Company company : companiesFromDB) {
			if (company.getPassword().equals(company.getName() + "654321")) {
				found5++;
			}
		}
		if (found5 == 5) {
			System.out.println("SUCCESS!!");
		} else {
			System.out.println("FAILURE!!");
		}
		System.out.println("trying to delete 5 companies:");
		companiesFromDB = admin.getAllCompanies();
		for (Company company : companiesFromDB) {
			if (company.getPassword().equals(company.getName() + "654321")) {
				admin.deleteCompany(company.getId());
			}
		}
		companiesFromDB = admin.getAllCompanies();
		if (companiesFromDB.size() == 5) {
			System.out.println("SUCCESS!!");
		} else {
			System.out.println("FAILURE!!");
		}
		System.out.println("trying to get 1 company by id:");
		Company getOneCompany = admin.getOneCompany(6);
		if (getOneCompany instanceof Company) {
			System.out.println("SUCCESS!!");
			System.out.println(getOneCompany);
		} else {
			System.out.println("FAILURE!!");
		}
	}

	public static Coupon randomCoupon(int companyId) {
		Category category = Category.values()[(int) (Math.random() * Category.values().length) ];
		String title = "coupon " + (int) (Math.random() * 90000) + 50;
		String description = title + " description";

		Calendar cal = Calendar.getInstance();
		Date startDate = new Date(cal.getTimeInMillis());
		int endYear = (int) (Math.random() * 4) + 2020;
		int randomMonth = (int) (Math.random() * 12);
		int endMonth = cal.get(Calendar.YEAR) == endYear ? Math.max(cal.get(Calendar.MONTH), randomMonth) : randomMonth;
		int randomDay = (int) (Math.random() * 30) + 1;
		int endDay = cal.get(Calendar.MONTH) == endMonth ? Math.max(cal.get(Calendar.DATE), randomDay) : randomDay;
		cal.set(endYear, endMonth, endDay);
		Date endDate = new Date(cal.getTimeInMillis());

		int amount = (int) (Math.random() * 100) + 30;
		double price = (int) (Math.random() * 750) + 50;
		String image = "image-path";
		Coupon coupon = new Coupon(companyId, category, title, description, startDate, endDate, amount, price, image);
		return coupon;
	}

	public static Customer randomCustomer() {
		String[] names = { "Floria Decuir", "Kanesha Mayer", "Era Lafromboise", "Kaye Benbow", "Margarite Weinert",
				"Lili Specht", "Tanika Nalls", "Drema Gummer", "Hal Firestone", "Ruth Garmon", "Lenna Kabel",
				"Elidia Hersom", "Lucile Havis", "Malcolm Sherrow", "Brandee Ensey", "Dorthey Benoit",
				"Sharice Daughtry", "Sharlene Evers", "Margeret Goodin", "Lucia Wedeking" };
		String firstName = names[((int) (Math.random() * names.length))].split(" ")[0];
		String lastName = names[((int) (Math.random() * names.length))].split(" ")[1];
		int rand = (int) (Math.random() * 9000) + 10;
		String email = firstName + "." + lastName + rand + "@gmail.com";
		String password = firstName + "123456";
		Customer customer = new Customer(firstName, lastName, email, password);
		return customer;
	}

	public static Company randomCompany() {
		
		String name = "company" + ((int) (Math.random() * 90000) + 100);
		String email = name + "@gmail.com";
		String password = name + "123456";
		Company company = new Company(name, email, password);
		return company;
	}

}
