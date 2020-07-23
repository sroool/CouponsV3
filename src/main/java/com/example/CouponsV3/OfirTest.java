//package com.example.CouponsV3;
//
//
//import java.sql.Date;
//import java.sql.SQLException;
//import java.util.Calendar;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.example.CouponsV3.beans.Category;
//import com.example.CouponsV3.beans.Company;
//import com.example.CouponsV3.beans.Coupon;
//import com.example.CouponsV3.beans.Customer;
//import com.example.CouponsV3.exceptions.CompanyDoesntExistException;
//import com.example.CouponsV3.exceptions.CompanyEmailAlreadyExistsException;
//import com.example.CouponsV3.exceptions.CompanyNameAlreadyExistsException;
//import com.example.CouponsV3.exceptions.CouponAlreadyPurchasedException;
//import com.example.CouponsV3.exceptions.CouponCompanyIdChangedException;
//import com.example.CouponsV3.exceptions.CouponDoesntExistException;
//import com.example.CouponsV3.exceptions.CouponExpiredException;
//import com.example.CouponsV3.exceptions.CouponOutOfStockException;
//import com.example.CouponsV3.exceptions.CouponTitleAlreadyExistsException;
//import com.example.CouponsV3.exceptions.CustomerDoesntExistException;
//import com.example.CouponsV3.exceptions.CustomerEmailAlreadyExistsException;
//import com.example.CouponsV3.exceptions.IncorrectCredentialsException;
//import com.example.CouponsV3.facades.AdminFacade;
//import com.example.CouponsV3.facades.ClientType;
//import com.example.CouponsV3.facades.CompanyFacade;
//import com.example.CouponsV3.facades.CustomerFacade;
//import com.example.CouponsV3.facades.LoginManager;
//
//@Component
//public class OfirTest {
//
//	@Autowired
//	private LoginManager log;
//
//	public void test() throws CouponAlreadyPurchasedException, CouponOutOfStockException, CouponExpiredException, IncorrectCredentialsException, CompanyDoesntExistException, CustomerDoesntExistException, SQLException, CompanyNameAlreadyExistsException, CompanyEmailAlreadyExistsException, CustomerEmailAlreadyExistsException, CouponTitleAlreadyExistsException, CouponCompanyIdChangedException, CouponDoesntExistException {
//
//		AdminFacade admin = (AdminFacade) log.login("admin@admin.com", "admin", ClientType.Administrator); // admin login
//		// database clearing for the test
//		if (!(admin.getAllCompanies().isEmpty())) {
//			for (Company item : admin.getAllCompanies()) {
//				admin.deleteCompany(item.getId());
//			}
//		}
//		if (!(admin.getAllCustomers().isEmpty())) {
//			for (Customer item : admin.getAllCustomers()) {
//				admin.deleteCustomer(item.getId());
//			}
//		}
//		// add companies
//		Company compy1 = new Company("acomp1", "a@a.com", "aa1234");
//		Company compy2 = new Company("bcomp2", "b@b.com", "bb1234");
//		Company compy3 = new Company("ccomp3", "c@c.com", "cc1234");
//		admin.addCompany(compy1);
//		admin.addCompany(compy2);
//		admin.addCompany(compy3);
//		
//		// add customers
//		Customer custy1 = new Customer("aaaafirst", "aaalast", "aaemail", "a1234");
//		Customer custy2 = new Customer("bbbbfirst", "bbblast", "bbemail", "b1234");
//		Customer custy3 = new Customer("ccccfirst", "ccclast", "ccemail", "c1234");
//		admin.addCustomer(custy1);
//		admin.addCustomer(custy2);
//		admin.addCustomer(custy3);
//		
//		// login with everything
//		CompanyFacade comp1 = (CompanyFacade) log.login("a@a.com", "aa1234", ClientType.Company);
//		CompanyFacade comp2 = (CompanyFacade) log.login("b@b.com", "bb1234", ClientType.Company);
//		CompanyFacade comp3 = (CompanyFacade) log.login("c@c.com", "cc1234", ClientType.Company);
//		CustomerFacade cust1 = (CustomerFacade) log.login("aaemail", "a1234", ClientType.Customer);
//		CustomerFacade cust2 = (CustomerFacade) log.login("bbemail", "b1234", ClientType.Customer);
//		CustomerFacade cust3 = (CustomerFacade) log.login("ccemail", "c1234", ClientType.Customer);
//		
//		// set companies java references
//		compy1 = comp1.getCompanyDetails();
//		compy2 = comp2.getCompanyDetails();
//		compy3 = comp3.getCompanyDetails();
//
//		// add shitton of coupons
//		Calendar start = Calendar.getInstance();
//		Calendar end = Calendar.getInstance();
//		start.set(2020, 0, 1);
//		end.set(2021, 0, 1);
//		Coupon coupya1 = new Coupon(compy1.getId(), Category.Fashion, "titlea", "description",
//				new Date(start.getTimeInMillis()), new Date(end.getTimeInMillis()), 10, 100, "image");
//		start.set(2021, 6, 11);
//		end.set(2023, 0, 1);
//		Coupon coupya2 = new Coupon(compy1.getId(), Category.Fashion, "titlea2", "description2",
//				new Date(start.getTimeInMillis()), new Date(end.getTimeInMillis()), 5, 12, "image");
//		start.set(2020, 7, 22);
//		end.set(2021, 5, 7);
//		Coupon coupya3 = new Coupon(compy1.getId(), Category.Food, "titlea3", "description3",
//				new Date(start.getTimeInMillis()), new Date(end.getTimeInMillis()), 5435, 5643, "image");
//		start.set(2021, 10, 12);
//		end.set(2022, 0, 1);
//		Coupon coupya4 = new Coupon(compy1.getId(), Category.Food, "titlea4", "description4", new Date(start.getTimeInMillis()),
//				new Date(end.getTimeInMillis()), 6, 423, "image");
//
//		Coupon coupyb1 = new Coupon(compy2.getId(), Category.Food, "titleb", "description", new Date(start.getTimeInMillis()),
//				new Date(end.getTimeInMillis()), 10, 100, "image");
//		start.set(2021, 6, 11);
//		end.set(2023, 0, 1);
//		Coupon coupyb2 = new Coupon(compy2.getId(), Category.Spa, "titleb2", "description2", new Date(start.getTimeInMillis()),
//				new Date(end.getTimeInMillis()), 200, 12, "image");
//		start.set(2020, 7, 22);
//		end.set(2021, 5, 7);
//		Coupon coupyb3 = new Coupon(compy2.getId(), Category.Food, "titleb3", "description3",
//				new Date(start.getTimeInMillis()), new Date(end.getTimeInMillis()), 5435, 5643, "image");
//		start.set(2021, 10, 12);
//		end.set(2022, 0, 1);
//		Coupon coupyb4 = new Coupon(compy2.getId(), Category.Fashion, "titleb4", "description4",
//				new Date(start.getTimeInMillis()), new Date(end.getTimeInMillis()), 6, 423, "image");
//
//		Coupon coupyc1 = new Coupon(compy3.getId(), Category.Furniture, "titlec", "description", new Date(start.getTimeInMillis()),
//				new Date(end.getTimeInMillis()), 10, 100, "image");
//		start.set(2021, 6, 11);
//		end.set(2023, 0, 1);
//		Coupon coupyc2 = new Coupon(compy3.getId(), Category.Furniture, "titlec2", "description2",
//				new Date(start.getTimeInMillis()), new Date(end.getTimeInMillis()), 200, 12, "image");
//		start.set(2020, 7, 22);
//		end.set(2021, 5, 7);
//		Coupon coupyc3 = new Coupon(compy3.getId(), Category.Electricity, "titlec3", "description3",
//				new Date(start.getTimeInMillis()), new Date(end.getTimeInMillis()), 5435, 5643, "image");
//		start.set(2021, 10, 12);
//		end.set(2022, 0, 1);
//		Coupon coupyc4 = new Coupon(compy3.getId(), Category.Fashion, "titlec4", "description4",
//				new Date(start.getTimeInMillis()), new Date(end.getTimeInMillis()), 6, 423, "image");
//
//		comp1.addCoupon(coupya1);
//		comp1.addCoupon(coupya2);
//		comp1.addCoupon(coupya3);
//		comp1.addCoupon(coupya4);
//
//		comp2.addCoupon(coupyb1);
//		comp2.addCoupon(coupyb2);
//		comp2.addCoupon(coupyb3);
//		comp2.addCoupon(coupyb4);
//
//		comp3.addCoupon(coupyc1);
//		comp3.addCoupon(coupyc2);
//		comp3.addCoupon(coupyc3);
//		comp3.addCoupon(coupyc4);
//
//		// set coupon java references
//		coupya1 = comp1.getCompanyCoupons().get(0);
//		coupya2 = comp1.getCompanyCoupons().get(1);
//		coupya3 = comp1.getCompanyCoupons().get(2);
//		coupya4 = comp1.getCompanyCoupons().get(3);
//
//		coupyb1 = comp2.getCompanyCoupons().get(0);
//		coupyb2 = comp2.getCompanyCoupons().get(1);
//		coupyb3 = comp2.getCompanyCoupons().get(2);
//		coupyb4 = comp2.getCompanyCoupons().get(3);
//
//		coupyc1 = comp3.getCompanyCoupons().get(0);
//		coupyc2 = comp3.getCompanyCoupons().get(1);
//		coupyc3 = comp3.getCompanyCoupons().get(2);
//		coupyc4 = comp3.getCompanyCoupons().get(3);
//
//		// set customers java references
//		custy1 = cust1.getCustomerDetails();
//		custy2 = cust2.getCustomerDetails();
//		custy3 = cust3.getCustomerDetails();
//
//		// buy all the coupons
//		for (Coupon c : comp1.getCompanyCoupons()) {
//			cust1.purchaseCoupon(c);
//		}
//
//		for (Coupon c : comp2.getCompanyCoupons()) {
//			cust2.purchaseCoupon(c);
//		}
//
//		for (Coupon c : comp3.getCompanyCoupons()) {
//			cust1.purchaseCoupon(c);
//		}
//
//		for (Coupon c : comp2.getCompanyCoupons()) {
//			cust3.purchaseCoupon(c);
//		}
//
//		for (Coupon c : comp3.getCompanyCoupons()) {
//			cust2.purchaseCoupon(c);
//		}
//
//		for (Coupon c : comp1.getCompanyCoupons()) {
//			cust3.purchaseCoupon(c);
//		}
//
//		// edit a coupon
//		coupya1.setDescription("new disc");
//		coupya1.setPrice(11115);
//		coupya1.setTitle("new title thing");
//		comp1.updateCoupon(coupya1);
//
//		// make some coupons have invalid dates for the thread to delete at the end of the test
//		// make sure the thread runs at the end of the test to remove those by the thread.
//		end.set(2020, 0, 1);
//		coupya2.setEndDate(new Date(end.getTimeInMillis()));
//		coupya4.setEndDate(new Date(end.getTimeInMillis()));
//		comp1.updateCoupon(coupya2);
//		comp1.updateCoupon(coupya4);
//
//		// delete coupons by the companies
//		comp1.deleteCoupon(coupya3.getId());
//		comp2.deleteCoupon(coupyb3.getId());
//		comp3.deleteCoupon(coupyc3.getId());
//
//	}
//
//	public void testGet() throws IncorrectCredentialsException, CustomerDoesntExistException, CompanyDoesntExistException, SQLException  {
//
//		// login with everything
//		AdminFacade admin = (AdminFacade) log.login("admin@admin.com", "admin", ClientType.Administrator);
//		CompanyFacade comp1 = (CompanyFacade) log.login("a@a.com", "aa1234", ClientType.Company);
//		CompanyFacade comp2 = (CompanyFacade) log.login("b@b.com", "bb1234", ClientType.Company);
//		CompanyFacade comp3 = (CompanyFacade) log.login("c@c.com", "cc1234", ClientType.Company);
//		CustomerFacade cust1 = (CustomerFacade) log.login("aaemail", "a1234", ClientType.Customer);
//		CustomerFacade cust2 = (CustomerFacade) log.login("bbemail", "b1234", ClientType.Customer);
//		CustomerFacade cust3 = (CustomerFacade) log.login("ccemail", "c1234", ClientType.Customer);
//
//		// set arrays for easy access
//		CompanyFacade[] comps = { comp1, comp2, comp3 };
//		CustomerFacade[] custs = { cust1, cust2, cust3 };
//		
//		// start priting everything...
//
//		System.out.println("\n\n\n\n===========Admin==========\n\n\n\n");
//		System.out.println("admin.getAllCompanies()");
//		System.out.println(admin.getAllCompanies());
//		System.out.println("\nadmin.getAllCustomers()");
//		System.out.println(admin.getAllCustomers());
//		
//		System.out.println("\nGet One Company");
//		for (Company comp : admin.getAllCompanies()) {
//			System.out.println(admin.getOneCompany(comp.getId()) + "\n");
//		}
//		System.out.println("\nGet One Customer");
//		for (Customer cust : admin.getAllCustomers()) {
//			System.out.println(admin.getOneCustomer(cust.getId()) + "\n");
//		}
//
//		System.out.println("\n\n\n\n============Company==============\n\n\n\n");
//
//		for (CompanyFacade comp : comps) {
//			System.out.println("!!START!! compID: " + comp.getCompanyDetails().getId() + " !!START!!");
//			System.out.println("\ncomp.getCompanyDetails()");
//			System.out.println(comp.getCompanyDetails());
//			System.out.println("\ncomp.getCompanyCoupons()");
//			System.out.println(comp.getCompanyCoupons());
//			System.out.println("\ncomp.getCompanyCoupons(maxPrice == 500)");
//			System.out.println(comp.getCompanyCoupons(500));
//			System.out.println(
//					"\ncomp.getCompanyCoupons(category) - will return an empty list if no coupons of this category are in the company database");
//			for (Category category : Category.values()) {
//				if (comp.getCompanyCoupons(category).size() > 0) {
//					System.out.println(category);
//					System.out.println(comp.getCompanyCoupons(category) + "\n");
//				}
//			}
////			System.out.println("\ncomp.getOneCoupon(coup.getId())");
////			for (Coupon coup : comp.getCompanyCoupons()) {
////				System.out.println(comp.getOneCoupon(coup.getId()) + "\n");
////			}
//			System.out.println("!!END!! compID: " + comp.getCompanyDetails().getId() + " !!END!!\n\n\n\n");
//		}
//
//		System.out.println("\n\n\n\n=========Customer==========\n\n\n\n");
//		for (CustomerFacade cust : custs) {
//			System.out.println("!!START!! custID: " + cust.getCustomerDetails().getId() + " !!START!!");
//			System.out.println("\ncust.getCustomerDetails()");
//			System.out.println(cust.getCustomerDetails());
//			System.out.println("\ncust.getCustomerCoupons()");
//			System.out.println(cust.getCustomerCoupons());
//			System.out.println("\ncust.getCustomerCoupons(maxPrice == 500)");
//			System.out.println(cust.getCustomerCoupons(500));
//			System.out.println(
//					"\ncust.getCustomerCoupons(category) - will return an empty list if no coupons of this category were purchased by this customer");
//			for (Category category : Category.values()) {
//				if (cust.getCustomerCoupons(category).size() > 0) {
//					System.out.println(category);
//					System.out.println(cust.getCustomerCoupons(category) + "\n");
//				}
//			}
//			for (Coupon coup : cust.getCustomerCoupons()) {
//				System.out.println(coup);
//			}
//			System.out.println("!!END!! custID: " + cust.getCustomerDetails().getId() + " !!END!!\n\n\n\n");
//		}
//
//	}
//	
//	
//	@Deprecated
//	public void testDelete() throws IncorrectCredentialsException, CustomerDoesntExistException, CompanyDoesntExistException {
//		AdminFacade admin = (AdminFacade) log.login("admin@admin.com", "admin", ClientType.Administrator);
//
//		for (Customer item : admin.getAllCustomers()) {
//			admin.deleteCustomer(item.getId());
//		}
//		for (Company item : admin.getAllCompanies()) {
//			admin.deleteCompany(item.getId());
//		}
//
//		
//	}
//
//}
