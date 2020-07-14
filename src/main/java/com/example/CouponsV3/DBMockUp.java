package com.example.CouponsV3;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.example.CouponsV3.beans.Category;
import com.example.CouponsV3.beans.Company;
import com.example.CouponsV3.beans.Coupon;
import com.example.CouponsV3.beans.Customer;
import com.example.CouponsV3.exceptions.CompanyEmailAlreadyExistsException;
import com.example.CouponsV3.exceptions.CompanyNameAlreadyExistsException;
import com.example.CouponsV3.exceptions.CouponAlreadyPurchasedException;
import com.example.CouponsV3.exceptions.CouponExpiredException;
import com.example.CouponsV3.exceptions.CouponOutOfStockException;
import com.example.CouponsV3.exceptions.CouponTitleAlreadyExistsException;
import com.example.CouponsV3.exceptions.CustomerEmailAlreadyExistsException;
import com.example.CouponsV3.exceptions.IncorrectCredentialsException;
import com.example.CouponsV3.facades.AdminFacade;
import com.example.CouponsV3.facades.ClientType;
import com.example.CouponsV3.facades.CompanyFacade;
import com.example.CouponsV3.facades.CustomerFacade;
import com.example.CouponsV3.facades.LoginManager;
@Service
public class DBMockUp {
	@Autowired
	private ConfigurableApplicationContext ctx;
	
	public void generateMockUp() {
		try {
			ClearDB();
			LoginManager loginManager = ctx.getBean(LoginManager.class);
			AdminFacade adminFacade = (AdminFacade) loginManager.login("admin@admin.com", "admin", ClientType.Administrator);
			//FOOD COMPANIES
			Company company1 = new Company("culinia", "culinia@culinia.com", "culunia1234");
			Company company2 = new Company("vegetard", "vegetard@vegetard.com", "vegetard1234");
			Company company3 = new Company("foodate", "foodate@foodate.com", "foodate1234");
			
			adminFacade.addCompany(company1);
			adminFacade.addCompany(company2);
			adminFacade.addCompany(company3);
			
			CompanyFacade companyFacade1 = (CompanyFacade) loginManager.login("culinia@culinia.com", "culunia1234", ClientType.Company);
			CompanyFacade companyFacade2 = (CompanyFacade) loginManager.login("vegetard@vegetard.com", "vegetard1234", ClientType.Company);
			CompanyFacade companyFacade3 = (CompanyFacade) loginManager.login("foodate@foodate.com", "foodate1234", ClientType.Company);
			Date today = new Date(System.currentTimeMillis());
			long dayInMillis = 1000 * 60 * 60 * 24;
			int company1Id = companyFacade1.getCompanyDetails().getId();
			int company2Id = companyFacade2.getCompanyDetails().getId();
			int company3Id = companyFacade3.getCompanyDetails().getId();
			//FOOD COMPANIES COUPONS
			Coupon coupon1 = new Coupon(company1Id, Category.Food, "Delicious Apples", "Delicious red apples", today, new Date(System.currentTimeMillis() + (dayInMillis * 2)), 
										35, randomAmount(10, 20), "assets/images/apple.webp");
			Coupon coupon2 = new Coupon(company1Id, Category.Food, "Amazing Bananas", "Huge ripe tasty bananas", today, new Date(System.currentTimeMillis() + (dayInMillis * 1)), 
						35, randomAmount(10, 20), "assets/images/bananas.webp");
			Coupon coupon3 = new Coupon(company1Id, Category.Food, "HomeMade MeatLoaf", "Delicious MeatLoaf made with our secret decades old recipe, tender and juicy!", today, new Date(System.currentTimeMillis() + (dayInMillis * 5)), 
					15, randomAmount(100, 130), "assets/images/beef.webp");
			Coupon coupon4 = new Coupon(company1Id, Category.Food, "Classic goldren loaf of bread", "A golden , crispy loaf of bread baked in a stone oven, rich in flavour and aroma!", today, new Date(System.currentTimeMillis() + (dayInMillis * 3)), 
					20, randomAmount(10, 20), "assets/images/bread.webp");
			Coupon coupon5 = new Coupon(company1Id, Category.Food, "Fresh Brocolli", "Delicious fresh brocolli, naturally grown with no pestecide", today, new Date(System.currentTimeMillis() + (dayInMillis * 5)), 
					35, randomAmount(10, 20), "assets/images/brocolli.webp");
			
			Coupon coupon6 = new Coupon(company2Id, Category.Food, "Food", "Food", today, new Date(System.currentTimeMillis() + (dayInMillis * 3)), 
					15, randomAmount(10, 20), "assets/images/food.webp");
			Coupon coupon7 = new Coupon(company2Id, Category.Food, "Cookie Monster Cookie jar", "Delightful cookie jar in the shape of the iconic Cookie Monster, your kids will love this! Limited availability!!", today, new Date(System.currentTimeMillis() + (dayInMillis * 2)), 
					5, randomAmount(45, 75), "assets/images/cookie_jar.webp");
			Coupon coupon8 = new Coupon(company2Id, Category.Food, "Delicious Chocolate kisses", "Wonderful chocolate kisses guerenteed to make every occasion more special, Valentines, great for birthdays!", today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 
					35, randomAmount(10, 20), "assets/images/chocolate.webp");
			Coupon coupon9 = new Coupon(company2Id, Category.Food, "IceCube Tray", "Best Ice Cube tray, fits 40 cubes of ice, state of the art technology", today, new Date(System.currentTimeMillis() + (dayInMillis * 7)), 
					50, randomAmount(2, 6), "assets/images/ice_cube_tray.webp");
			
			Coupon coupon10 = new Coupon(company3Id, Category.Food, "Milk", "Fantastic Milk high in calcium and protein, milked from Freedom cows.", today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 
					50, randomAmount(5, 10), "assets/images/milk.webp");
			Coupon coupon11 = new Coupon(company3Id, Category.Food, "Blue Plate", "High quality plates, comes in blue only!, price is for a set of 10!", today, new Date(System.currentTimeMillis() + (dayInMillis * 2)), 
					50, randomAmount(15, 40), "assets/images/plate.webp");
			Coupon coupon12 = new Coupon(company3Id, Category.Food, "Soy Sauce Packets", "Make every meal better with our home made Soy sauce, now in packets!!", today, new Date(System.currentTimeMillis() + (dayInMillis * 5)), 
					100, randomAmount(5, 10), "assets/images/soy_sauce_packet.webp");
			Coupon coupon13 = new Coupon(company3Id, Category.Food, "Tomatoes", "Excellent ripe red tomatoes good for salads , salsa and when the theater play your watching sucks", today, new Date(System.currentTimeMillis() + (dayInMillis * 5)), 
					35, randomAmount(5, 10), "assets/images/tomato.webp");
			Coupon coupon14 = new Coupon(company3Id, Category.Food, "Toothpics", "High quality bamboo toothpicks", today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 
					50, randomAmount(5, 10), "assets/images/tooth_picks.webp");
			Coupon coupon15 = new Coupon(company3Id, Category.Food, "Water Bottle", "Fresh Mineral water from the alpes, costs accordingly!!", today, new Date(System.currentTimeMillis() + (dayInMillis * 1)), 
					5, randomAmount(1000,1500), "assets/images/water_bottle.webp");
			
			companyFacade1.addCoupon(coupon1);
			companyFacade1.addCoupon(coupon2);
			companyFacade1.addCoupon(coupon3);
			companyFacade1.addCoupon(coupon4);
			companyFacade1.addCoupon(coupon5);
			
			companyFacade2.addCoupon(coupon6);
			companyFacade2.addCoupon(coupon7);
			companyFacade2.addCoupon(coupon8);
			companyFacade2.addCoupon(coupon9);
			
			companyFacade3.addCoupon(coupon10);
			companyFacade3.addCoupon(coupon11);
			companyFacade3.addCoupon(coupon12);
			companyFacade3.addCoupon(coupon13);
			companyFacade3.addCoupon(coupon14);
			companyFacade3.addCoupon(coupon15);
			
			//CUSTOMERS
			Customer customer1 = new Customer("Nir", "Gal", "nirg@gmail.com", "nir1234");
			Customer customer2 = new Customer("Itay", "foo", "itay@gmail.com", "itay1234");
			Customer customer3 = new Customer("Ido", "foo", "ido@gmail.com", "ido1234");
			Customer customer4 = new Customer("Gad", "foo", "gad@gmail.com", "gad1234");
			Customer customer5 = new Customer("Ofir", "foo", "ofir@gmail.com", "ofir1234");
			Customer customer6 = new Customer("Herman", "Herninio", "herman@gmail.com", "herman1234");
			Customer customer7 = new Customer("Netanel", "foo", "netanel@gmail.com", "netanel1234");
			Customer customer8 = new Customer("Rotem", "foo", "rotem@gmail.com", "rotem1234");
			Customer customer9 = new Customer("Noa", "foo", "noa@gmail.com", "noa1234");
			Customer customer10 = new Customer("Peleg", "foo", "peleg@gmail.com", "peleg1234");
			Customer customer11 = new Customer("Noy", "foo", "noy@gmail.com", "noy1234");
			
			adminFacade.addCustomer(customer1);
			adminFacade.addCustomer(customer2);
			adminFacade.addCustomer(customer3);
			adminFacade.addCustomer(customer4);
			adminFacade.addCustomer(customer5);
			adminFacade.addCustomer(customer6);
			adminFacade.addCustomer(customer7);
			adminFacade.addCustomer(customer8);
			adminFacade.addCustomer(customer9);
			adminFacade.addCustomer(customer10);
			adminFacade.addCustomer(customer11);
			
			
			CustomerFacade customerFacade1 = (CustomerFacade) loginManager.login("nirg@gmail.com", "nir1234", ClientType.Customer);
			CustomerFacade customerFacade2 = (CustomerFacade) loginManager.login("itay@gmail.com", "itay1234", ClientType.Customer);
			CustomerFacade customerFacade3 = (CustomerFacade) loginManager.login("ido@gmail.com", "ido1234", ClientType.Customer);
			CustomerFacade customerFacade4 = (CustomerFacade) loginManager.login("gad@gmail.com", "gad1234", ClientType.Customer);
			CustomerFacade customerFacade5 = (CustomerFacade) loginManager.login("ofir@gmail.com", "ofir1234", ClientType.Customer);
			CustomerFacade customerFacade6 = (CustomerFacade) loginManager.login("herman@gmail.com", "herman1234", ClientType.Customer);
			CustomerFacade customerFacade7 = (CustomerFacade) loginManager.login("netanel@gmail.com", "netanel1234", ClientType.Customer);
			CustomerFacade customerFacade8 = (CustomerFacade) loginManager.login("rotem@gmail.com", "rotem1234", ClientType.Customer);
			CustomerFacade customerFacade9 = (CustomerFacade) loginManager.login("noa@gmail.com", "noa1234", ClientType.Customer);
			CustomerFacade customerFacade10 = (CustomerFacade) loginManager.login("peleg@gmail.com", "peleg1234", ClientType.Customer);
			CustomerFacade customerFacade11 = (CustomerFacade) loginManager.login("noy@gmail.com", "noy1234", ClientType.Customer);
			
			List<Coupon> companyFacade1Coupons = companyFacade1.getCompanyCoupons();
			List<Coupon> companyFacade2Coupons = companyFacade2.getCompanyCoupons();
			List<Coupon> companyFacade3Coupons = companyFacade3.getCompanyCoupons();
			for(Coupon coupon : companyFacade1Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				if(coupon.getId() % 2 == 0) {
					
					customerFacade3.purchaseCoupon(coupon);
					customerFacade4.purchaseCoupon(coupon);
					customerFacade5.purchaseCoupon(coupon);
					customerFacade6.purchaseCoupon(coupon);
					customerFacade7.purchaseCoupon(coupon);
				}
				customerFacade8.purchaseCoupon(coupon);
				customerFacade9.purchaseCoupon(coupon);
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}
			for(Coupon coupon : companyFacade2Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				customerFacade3.purchaseCoupon(coupon);
				if(coupon.getAmount() < 5) {
					continue;
				}
				customerFacade4.purchaseCoupon(coupon);
				customerFacade5.purchaseCoupon(coupon);
				customerFacade6.purchaseCoupon(coupon);
				customerFacade7.purchaseCoupon(coupon);
				if(coupon.getId() %2 == 0) {
					continue;
				}
				customerFacade8.purchaseCoupon(coupon);
				customerFacade9.purchaseCoupon(coupon);
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}
			for(Coupon coupon : companyFacade3Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				if(coupon.getAmount() < 5) {
					continue;
				}
				customerFacade2.purchaseCoupon(coupon);
				customerFacade3.purchaseCoupon(coupon);
				customerFacade4.purchaseCoupon(coupon);
				customerFacade5.purchaseCoupon(coupon);
				customerFacade6.purchaseCoupon(coupon);
				customerFacade7.purchaseCoupon(coupon);
				
				customerFacade8.purchaseCoupon(coupon);
				customerFacade9.purchaseCoupon(coupon);
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}
			
			
		} catch (SQLException | IncorrectCredentialsException | CompanyNameAlreadyExistsException | CompanyEmailAlreadyExistsException | CouponTitleAlreadyExistsException | CustomerEmailAlreadyExistsException | CouponAlreadyPurchasedException | CouponOutOfStockException | CouponExpiredException e) {
			System.out.println(e.getMessage());
		}
	}
	public int randomAmount(int lowerLimit, int upperLimit) {
		return (int)(lowerLimit + Math.random() * (upperLimit - lowerLimit));
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
}
