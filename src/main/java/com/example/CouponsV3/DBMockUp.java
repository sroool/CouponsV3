package com.example.CouponsV3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.CouponsV3.exceptions.CouponDoesntExistException;
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
	private Map<String, byte[]> imagesData =  new HashMap<String, byte[]>();
	/**
	 * Helper method to clear the db before each test and reset the auto increment
	 * of every table to 1. to remove also delete the methods from the DAO's,
	 * DBDAO's and admin facade.
	 * 
	 * @throws SQLException
	 * @throws IncorrectCredentialsException
	 */
	public void ClearDB()  {
		LoginManager manager = ctx.getBean(LoginManager.class);

		try {
			AdminFacade admin = (AdminFacade) manager.login("admin@admin.com", "admin", ClientType.Administrator);
			for (Company company : admin.getAllCompanies()) {
				admin.deleteCompany(company.getId());
			}
			for (Customer customer : admin.getAllCustomers()) {
				admin.deleteCustomer(customer.getId());
			}
			admin.resetAutoIncrement(1);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	public void generateMockUp() {
		try {
			ClearDB();
			File imagesDir = new File("src//main//resources//static//assets//images");
			File[] images = imagesDir.listFiles();
			if(imagesDir == null || images.length ==0) {
				System.out.println("Error loading images");
				return;
			}
			for(File image : images) {
				byte[] imageData;
				String base64;
				try(FileInputStream is = new FileInputStream(image)){
					imageData = new byte[(int)image.length()];
					is.read(imageData);
					base64 = Base64.getEncoder().encodeToString(imageData);
					base64 = "data:image/"+image.getName().split("\\.")[1] +";base64,"+base64 ;
					
					imagesData.put(image.getName().toLowerCase(), base64.getBytes());
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch( NullPointerException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
			LoginManager loginManager = ctx.getBean(LoginManager.class);
			AdminFacade adminFacade = (AdminFacade) loginManager.login("admin@admin.com", "admin",
					ClientType.Administrator);
			// FOOD COMPANIES
			Company company1 = new Company("culinia", "culinia@culinia.com", "culinia1234");
			Company company2 = new Company("vegetard", "vegetard@vegetard.com", "vegetard1234");
			Company company3 = new Company("foodate", "foodate@foodate.com", "foodate1234");

			adminFacade.addCompany(company1);
			adminFacade.addCompany(company2);
			adminFacade.addCompany(company3);

			CompanyFacade companyFacade1 = (CompanyFacade) loginManager.login("culinia@culinia.com", "culinia1234",
					ClientType.Company);
			CompanyFacade companyFacade2 = (CompanyFacade) loginManager.login("vegetard@vegetard.com", "vegetard1234",
					ClientType.Company);
			CompanyFacade companyFacade3 = (CompanyFacade) loginManager.login("foodate@foodate.com", "foodate1234",
					ClientType.Company);
			Date today = new Date(System.currentTimeMillis());
			long dayInMillis = 1000 * 60 * 60 * 24;
			int company1Id = companyFacade1.getCompanyDetails().getId();
			int company2Id = companyFacade2.getCompanyDetails().getId();
			int company3Id = companyFacade3.getCompanyDetails().getId();
			// FOOD COMPANIES COUPONS
			Coupon coupon1 = new Coupon(company1Id, Category.Food, "Delicious Apples", "Delicious red apples", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 2)), 35, randomPrice(10, 20),
					imagesData.get("apple.webp"));
			Coupon coupon2 = new Coupon(company1Id, Category.Food, "Amazing Bananas", "Huge ripe tasty bananas", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 1)), 35, randomPrice(10, 20),
					imagesData.get("bananas.webp"));
			Coupon coupon3 = new Coupon(company1Id, Category.Food, "HomeMade MeatLoaf",
					"Delicious MeatLoaf made with our secret decades old recipe, tender and juicy!", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 5)), 15, randomPrice(100, 130),
					imagesData.get("beef.webp"));
			Coupon coupon4 = new Coupon(company1Id, Category.Food, "Classic goldren loaf of bread",
					"A golden , crispy loaf of bread baked in a stone oven, rich in flavour and aroma!", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 3)), 20, randomPrice(10, 20),
					imagesData.get("bread.webp"));
			Coupon coupon5 = new Coupon(company1Id, Category.Food, "Fresh Brocolli",
					"Delicious fresh brocolli, naturally grown with no pestecide", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 5)), 35, randomPrice(10, 20),
					imagesData.get("brocolli.webp"));

			Coupon coupon6 = new Coupon(company2Id, Category.Food, "Food", "Food", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 3)), 15, randomPrice(10, 20),
					imagesData.get("food.webp"));
			Coupon coupon7 = new Coupon(company2Id, Category.Food, "Cookie Monster Cookie jar",
					"Delightful cookie jar in the shape of the iconic Cookie Monster, your kids will love this! Limited availability!!",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 2)), 15, randomPrice(45, 75),
					imagesData.get("cookie_jar.webp"));
			Coupon coupon8 = new Coupon(company2Id, Category.Food, "Delicious Chocolate kisses",
					"Wonderful chocolate kisses guerenteed to make every occasion more special, Valentines, great for birthdays!",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 35, randomPrice(10, 20),
					imagesData.get("chocolate.webp"));
			Coupon coupon9 = new Coupon(company2Id, Category.Food, "IceCube Tray",
					"Best Ice Cube tray, fits 40 cubes of ice, state of the art technology", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 7)), 50, randomPrice(2, 6),
					imagesData.get("ice_cube_tray.webp"));

			Coupon coupon10 = new Coupon(company3Id, Category.Food, "Milk",
					"Fantastic Milk high in calcium and protein, milked from Freedom cows.", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 4)), 50, randomPrice(5, 10),
					imagesData.get("milk.webp"));
			Coupon coupon11 = new Coupon(company3Id, Category.Food, "Blue Plate",
					"High quality plates, comes in blue only!, price is for a set of 10!", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 2)), 50, randomPrice(15, 40),
					imagesData.get("plate.webp"));
			Coupon coupon12 = new Coupon(company3Id, Category.Food, "Soy Sauce Packets",
					"Make every meal better with our home made Soy sauce, now in packets!!", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 5)), 100, randomPrice(5, 10),
					imagesData.get("soy_sauce_packet.webp"));
			Coupon coupon13 = new Coupon(company3Id, Category.Food, "Tomatoes",
					"Excellent ripe red tomatoes good for salads , salsa and when the theater play your watching sucks",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 5)), 35, randomPrice(5, 10),
					imagesData.get("tomato.webp"));
			Coupon coupon14 = new Coupon(company3Id, Category.Food, "Toothpics", "High quality bamboo toothpicks",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 50, randomPrice(5, 10),
					imagesData.get("tooth_picks.webp"));
			Coupon coupon15 = new Coupon(company3Id, Category.Food, "Water Bottle",
					"Fresh Mineral water from the alpes, costs accordingly!!", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 1)), 15, randomPrice(1000, 1500),
					imagesData.get("water_bottle.webp"));

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

			// CUSTOMERS
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

			CustomerFacade customerFacade1 = (CustomerFacade) loginManager.login("nirg@gmail.com", "nir1234",
					ClientType.Customer);
			CustomerFacade customerFacade2 = (CustomerFacade) loginManager.login("itay@gmail.com", "itay1234",
					ClientType.Customer);
			CustomerFacade customerFacade3 = (CustomerFacade) loginManager.login("ido@gmail.com", "ido1234",
					ClientType.Customer);
			CustomerFacade customerFacade4 = (CustomerFacade) loginManager.login("gad@gmail.com", "gad1234",
					ClientType.Customer);
			CustomerFacade customerFacade5 = (CustomerFacade) loginManager.login("ofir@gmail.com", "ofir1234",
					ClientType.Customer);
			CustomerFacade customerFacade6 = (CustomerFacade) loginManager.login("herman@gmail.com", "herman1234",
					ClientType.Customer);
			CustomerFacade customerFacade7 = (CustomerFacade) loginManager.login("netanel@gmail.com", "netanel1234",
					ClientType.Customer);
			CustomerFacade customerFacade8 = (CustomerFacade) loginManager.login("rotem@gmail.com", "rotem1234",
					ClientType.Customer);
			CustomerFacade customerFacade9 = (CustomerFacade) loginManager.login("noa@gmail.com", "noa1234",
					ClientType.Customer);
			CustomerFacade customerFacade10 = (CustomerFacade) loginManager.login("peleg@gmail.com", "peleg1234",
					ClientType.Customer);
			CustomerFacade customerFacade11 = (CustomerFacade) loginManager.login("noy@gmail.com", "noy1234",
					ClientType.Customer);

			List<Coupon> companyFacade1Coupons = companyFacade1.getCompanyCoupons();
			List<Coupon> companyFacade2Coupons = companyFacade2.getCompanyCoupons();
			List<Coupon> companyFacade3Coupons = companyFacade3.getCompanyCoupons();
			for (Coupon coupon : companyFacade1Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {

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
			for (Coupon coupon : companyFacade2Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				customerFacade3.purchaseCoupon(coupon);
				if (coupon.getOriginalAmount() < 5) {
					continue;
				}
				customerFacade4.purchaseCoupon(coupon);
				customerFacade5.purchaseCoupon(coupon);
				customerFacade6.purchaseCoupon(coupon);
				customerFacade7.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {
					continue;
				}
				customerFacade8.purchaseCoupon(coupon);
				customerFacade9.purchaseCoupon(coupon);
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}
			for (Coupon coupon : companyFacade3Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				if (coupon.getOriginalAmount() < 5) {
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

			// ELECTRONICS companies

			Company company4 = new Company("electrolo", "electrolo@electrolo.com", "electrolo1234");
			Company company5 = new Company("digistics", "digistics@digistics.com", "digistics1234");

			adminFacade.addCompany(company4);
			adminFacade.addCompany(company5);

			CompanyFacade companyFacade4 = (CompanyFacade) loginManager.login("electrolo@electrolo.com",
					"electrolo1234", ClientType.Company);
			CompanyFacade companyFacade5 = (CompanyFacade) loginManager.login("digistics@digistics.com",
					"digistics1234", ClientType.Company);

			int company4Id = companyFacade4.getCompanyDetails().getId();
			int company5Id = companyFacade5.getCompanyDetails().getId();

			// ELECTRONICS COMPANIES COUPONS
			Coupon coupon16 = new Coupon(company4Id, Category.Electricity, "OLD SCHOOL BoomBox", "Hip retro boombox",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 2)), 35, randomPrice(10, 20),
					imagesData.get("boom_box.webp"));
			Coupon coupon17 = new Coupon(company4Id, Category.Electricity,
					"Verbatim CD-R 700MB 80 Minute 52x Recordable Disc - 100 Pack",
					"100 high-grade non-rewritable CD-R discs with one hundred year archival life", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 2)), 100, randomPrice(5, 8),
					imagesData.get("cd.webp"));
			Coupon coupon18 = new Coupon(company4Id, Category.Electricity,
					"Boogle - Bixel 3a with 64GB Memory Cell Phone",
					"Comes with 3 years of OS and security updates] and the custom-built Titan M chip.", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 1)), 100, randomPrice(200, 400),
					imagesData.get("cell_phone.webp"));
			Coupon coupon19 = new Coupon(company4Id, Category.Electricity,
					"New Genuine Enspiron 11 13 14 15 Laptop Charger 45W(watt)",
					" New Original AC Adapter include Power Cord with non-retail packaging", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 5)), 100, randomPrice(10, 20),
					imagesData.get("charger.webp"));
			Coupon coupon20 = new Coupon(company4Id, Category.Electricity,
					"Black Wall Clock Silent Non Ticking - 10 Inch",
					"10 inch clock, Large black bold numbers against white face creates easy viewing. Precise quartz movements guarantee accurate time",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 3)), 35, randomPrice(10, 20),
					imagesData.get("clock.webp"));
			Coupon coupon21 = new Coupon(company4Id, Category.Electricity, "xTreme Gaming CompuTER pc",
					"CYBERPOWERPC Gamer Xtreme VR Gaming PC, Intel Core i5-9400F 2.9GHz, NVIDIA GeForce GTX 1660 6GB, 8GB DDR4, 240GB SSD, 1TB HDD, WiFi Ready & Win 10 Home (GXiVR8060A8, Black)",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 2)), 35, randomPrice(700, 900),
					imagesData.get("computer.webp"));
			Coupon coupon22 = new Coupon(company4Id, Category.Electricity, "Xbox Wireless Controller - Black",
					"Compatible with Xbox One X, Xbox One S, Xbox One, Windows 10", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 4)), 35, randomPrice(60, 90),
					imagesData.get("controller.webp"));
			Coupon coupon23 = new Coupon(company4Id, Category.Electricity,
					"Midea 3.1 Cu. Ft. Compact Refrigerator, WHD-113FSS1 - Stainless Steel",
					"Midea 3.1 Cu. Ft. Compact Refrigerator, WHD-113FSS1 - Stainless Steel", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 2)), 35, randomPrice(1000, 1200),
					imagesData.get("fridge.webp"));
			Coupon coupon24 = new Coupon(company4Id, Category.Electricity, "classic bPod",
					"Music Player ePod Classic 6th Generation 120gb Silver Packaged in Plain White Box", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 2)), 35, randomPrice(100, 120),
					imagesData.get("ipod.webp"));
			Coupon coupon25 = new Coupon(company4Id, Category.Electricity, "ULTRA MAJESTIC GAMING KEYBOARD. GAMING!!",
					"Redragon S101 Wired Gaming Keyboard and Mouse Combo RGB Backlit Gaming Keyboard with Multimedia Keys Wrist Rest and Red Backlit Gaming Mouse 3200 DPI for Windows PC Gamers",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 2)), 35, randomPrice(125, 200),
					imagesData.get("keyboard.webp"));

			Coupon coupon26 = new Coupon(company5Id, Category.Electricity, "Acme Magnet",
					"Huge red magnet just like the ones in the Acme cartoons", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(10, 20),
					imagesData.get("magnet.webp"));
			Coupon coupon27 = new Coupon(company5Id, Category.Electricity,
					"XXX GAMING SCREEN MONITOR WITH RGB AND PREBUILT SKILLZ",
					"Sceptre E248W-19203R 24\" Ultra Thin 75Hz 1080p LED Monitor 2x HDMI VGA Build-in Speakers, Metallic Black 2018",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 1)), 100, randomPrice(222, 300),
					imagesData.get("monitor.webp"));
			Coupon coupon28 = new Coupon(company5Id, Category.Electricity, "RGB Mouse pad for real gamerz",
					"VicTsing Mouse Pad, Ergonomic Mouse Pad with Gel Wrist Rest Support, Gaming Mouse Pad with Lycra Cloth, Non-Slip PU Base for Computer, Laptop, Home, Office & Travel, Black",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 1)), 100, randomPrice(200, 400),
					imagesData.get("mouse_pad.webp"));
			Coupon coupon29 = new Coupon(company5Id, Category.Electricity, "mp3 player",
					"Mymahdi MP3/MP4 Portable Player,1.8 Inch LCD Screen,Max Support 64GB,Pink", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 5)), 100, randomPrice(10, 20),
					imagesData.get("mp3_player.webp"));
			Coupon coupon30 = new Coupon(company5Id, Category.Electricity, "outlet",
					"Leviton T5320-W Straight Blade Tamper Resistant Duplex Receptacle, 125 V, 15 A, 2 Pole, 3 Wire, 1 Pack, White",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 3)), 35, randomPrice(10, 20),
					imagesData.get("outlet.webp"));
			Coupon coupon31 = new Coupon(company5Id, Category.Electricity, "Universal Remote",
					"GE Universal Remote Control for Samsung, Vizio, LG, Sony, Sharp, Roku, Apple TV, RCA, Panasonic, Smart TVs, Streaming Players, Blu-ray, DVD, 4-Device, Black, 34708",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 11)), 35, randomPrice(70, 90),
					imagesData.get("remote.webp"));
			Coupon coupon32 = new Coupon(company5Id, Category.Electricity, "Thermometer",
					"Springfield Vertical Thermometer and Hygrometer (9.125-Inch)", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 4)), 35, randomPrice(10, 20),
					imagesData.get("thermometer.webp"));
			Coupon coupon33 = new Coupon(company5Id, Category.Electricity, "Thermostat",
					"Honeywell RTH221B1021/E1 RTH221B1021 Programmable Thermostat, Off White", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 2)), 35, randomPrice(30, 40),
					imagesData.get("thermostat.webp"));
			Coupon coupon34 = new Coupon(company5Id, Category.Electricity, "128 GB USB Drive",
					"Sungsam BAR Plus USB 3.1 Flash Drive 128GB - 300MB/s (MUF-128BE3/AM) - Night Black", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 2)), 35, randomPrice(100, 120),
					imagesData.get("usb_drive.webp"));
			Coupon coupon35 = new Coupon(company5Id, Category.Electricity, "washing machine",
					"Panda Compact Washer 1.60cu.ft, High-End Fully Automatic Portable Washing Machine, white", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 2)), 35, randomPrice(400, 600),
					imagesData.get("washing_machine.webp"));

			companyFacade4.addCoupon(coupon16);
			companyFacade4.addCoupon(coupon17);
			companyFacade4.addCoupon(coupon18);
			companyFacade4.addCoupon(coupon19);
			companyFacade4.addCoupon(coupon20);

			companyFacade4.addCoupon(coupon21);
			companyFacade4.addCoupon(coupon22);
			companyFacade4.addCoupon(coupon23);
			companyFacade4.addCoupon(coupon24);
			companyFacade4.addCoupon(coupon25);

			companyFacade5.addCoupon(coupon26);
			companyFacade5.addCoupon(coupon27);
			companyFacade5.addCoupon(coupon28);
			companyFacade5.addCoupon(coupon29);
			companyFacade5.addCoupon(coupon30);

			companyFacade5.addCoupon(coupon31);
			companyFacade5.addCoupon(coupon32);
			companyFacade5.addCoupon(coupon33);
			companyFacade5.addCoupon(coupon34);
			companyFacade5.addCoupon(coupon35);

			List<Coupon> companyFacade4Coupons = companyFacade4.getCompanyCoupons();
			List<Coupon> companyFacade5Coupons = companyFacade5.getCompanyCoupons();

			for (Coupon coupon : companyFacade4Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {

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
			for (Coupon coupon : companyFacade5Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				customerFacade3.purchaseCoupon(coupon);
				if (coupon.getId() % 3 == 0) {
					continue;
				}
				customerFacade4.purchaseCoupon(coupon);
				customerFacade5.purchaseCoupon(coupon);
				customerFacade6.purchaseCoupon(coupon);
				customerFacade7.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {
					continue;
				}
				customerFacade8.purchaseCoupon(coupon);
				customerFacade9.purchaseCoupon(coupon);
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}

			// FASHION companies
			Company company6 = new Company("styline", "styline@styline.com", "styline1234");
			Company company7 = new Company("lewear", "lewear@lewear.com", "lewear1234");
			Company company8 = new Company("designero", "designero@designero.com", "designero1234");
			Company company9 = new Company("fashiongarb", "fashiongarb@fashiongarb.com", "fashiongarb1234");

			adminFacade.addCompany(company6);
			adminFacade.addCompany(company7);
			adminFacade.addCompany(company8);
			adminFacade.addCompany(company9);

			CompanyFacade companyFacade6 = (CompanyFacade) loginManager.login("styline@styline.com", "styline1234",
					ClientType.Company);
			CompanyFacade companyFacade7 = (CompanyFacade) loginManager.login("lewear@lewear.com", "lewear1234",
					ClientType.Company);
			CompanyFacade companyFacade8 = (CompanyFacade) loginManager.login("designero@designero.com", "designero1234",
					ClientType.Company);
			CompanyFacade companyFacade9 = (CompanyFacade) loginManager.login("fashiongarb@fashiongarb.com", "fashiongarb1234",
					ClientType.Company);

			int company6Id = companyFacade6.getCompanyDetails().getId();
			int company7Id = companyFacade7.getCompanyDetails().getId();
			int company8Id = companyFacade8.getCompanyDetails().getId();
			int company9Id = companyFacade9.getCompanyDetails().getId();

			Coupon coupon36 = new Coupon(company6Id, Category.Fashion, "Stylish Red Blouse",
					"ACKKIA Women's Casual V Neck 3/4 Bell Sleeves Blouse Mesh Panel Shirt Summer Top", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 3)), 100, randomPrice(49, 60),
					imagesData.get("blouse.webp"));
			Coupon coupon37 = new Coupon(company6Id, Category.Fashion, "Stacy Adams Men's Solid Bow Tie",
					"Stacy Adams Men's Solid Bow Tie", today, new Date(System.currentTimeMillis() + (dayInMillis * 7)),
					100, randomPrice(5, 20), imagesData.get("bow.webp"));
			Coupon coupon38 = new Coupon(company6Id, Category.Fashion, "Stylish nits bracelet",
					"Hamoery Men Women 8mm Lava Rock 7 Chakras Aromatherapy Essential Oil Diffuser Bracelet Braided Rope Natural Stone Yoga Beads Bracelet Bangle",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 100, randomPrice(5, 20),
					imagesData.get("bracelet.webp"));
			Coupon coupon39 = new Coupon(company6Id, Category.Fashion, "Black Button , 50 pack",
					"GANSSIA 1 Inch Buttons 25mm Sewing Flatback Button Black Colored Pack of 50", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(3, 7),
					imagesData.get("button.webp"));
			Coupon coupon40 = new Coupon(company6Id, Category.Fashion, "Black CheckBook",
					"Black Vinyl Checkbook Cover, Top Tear Personal Vinyl Checkbook Cover", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(10, 20),
					imagesData.get("checkbook.webp"));
			Coupon coupon41 = new Coupon(company6Id, Category.Fashion, "Colorful Turkish towels",
					"Turkish Towel Set of 6 Beach Towels Oversized Clearance Fast drying towel 100% Cotton Sand Free beach towel",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(10, 20),
					imagesData.get("clothes.webp"));

			Coupon coupon42 = new Coupon(company7Id, Category.Fashion, "Stylish Glasses",
					"SOJOS Cat Eye Blue Light Blocking Glasses Hipster Metal Frame Women Eyeglasses She Young", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 5)), 100, randomPrice(15, 60),
					imagesData.get("glasses.webp"));
			Coupon coupon43 = new Coupon(company7Id, Category.Fashion, "Colorful hair ties 100 pcs",
					"Hair Ties - 100 Pcs 2mm Black Rubber Hair Bands for Ponytail (2mm, colors)", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 7)), 100, randomPrice(5, 20),
					imagesData.get("hair_tie.webp"));
			Coupon coupon44 = new Coupon(company7Id, Category.Fashion, "Stylish nits bracelet",
					"Hamoery Men Women 8mm Lava Rock 7 Chakras Aromatherapy Essential Oil Diffuser Bracelet Braided Rope Natural Stone Yoga Beads Bracelet Bangle",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 100, randomPrice(5, 20),
					imagesData.get("bracelet.webp"));
			Coupon coupon45 = new Coupon(company7Id, Category.Fashion, "Brown wooden Hangers, 20 pack",
					"High-Grade Wooden Suit Hangers (20 Pack) - Smooth Finish Solid Wood Coat Hanger with Non Slip Pants Bar, 360° Swivel Hook and Precisely Cut Notches for Camisole, Jacket, Pant, Dress Clothes Hangers",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(13, 17),
					imagesData.get("hanger.webp"));
			Coupon coupon46 = new Coupon(company7Id, Category.Fashion, "Red Leg Warmers",
					"Long Leg Warmer, V28 Women’s Men 80s Party Ribbed Knit Dance Sports", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(10, 20),
					imagesData.get("leg_warmers.webp"));
			Coupon coupon47 = new Coupon(company7Id, Category.Fashion, "Stylish Black Purse",
					"Purses and Handbags for Women Fashion Ladies PU Leather Top Handle Satchel Shoulder Tote Bags",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(50, 70),
					imagesData.get("purse.webp"));

			Coupon coupon48 = new Coupon(company8Id, Category.Fashion, "Amazing Silver Engagement Ring",
					"MAIHAO New Princess Square Simulation Diamond Ring Double-layer super-flash zircon ring in 925 sterling silve Cubic Zirconia Promise Halo Engagement Wedding Band Ring for Women Size 6-10 (Size 10)",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 2)), 100, randomPrice(1200, 2000),
					imagesData.get("ring.webp"));
			Coupon coupon49 = new Coupon(company8Id, Category.Fashion, "Colorful Rubber Bands",
					"Alliance Rubber Bands Assorted Dimensions 227G/Approx. 400 Rubber Bands, Multi Color, 1/2 lb",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(10, 20),
					imagesData.get("rubber_band.webp"));
			Coupon coupon50 = new Coupon(company8Id, Category.Fashion, "Reef Women's Cushion Breeze Sandal",
					"Reef Women's Cushion Breeze Sandal", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(50, 70),
					imagesData.get("sandal.webp"));

			Coupon coupon51 = new Coupon(company9Id, Category.Fashion, "Shoe Laces",
					"Miscly Round Boot Laces [1 Pair] Heavy Duty and Durable Shoelaces for Boots, Work Boots & Hiking Shoes",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 100, randomPrice(2, 5),
					imagesData.get("shoe_lace.webp"));
			Coupon coupon52 = new Coupon(company9Id, Category.Fashion, "Shoes",
					"Sperry Men's Striper II Salt Washed CVO Sneaker", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 3)), 100, randomPrice(100, 400),
					imagesData.get("shoes.webp"));
			Coupon coupon53 = new Coupon(company9Id, Category.Fashion, "Socks",
					"Dickies Men's Dri-tech Moisture Control Crew Socks Multipack", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 4)), 100, randomPrice(2, 5),
					imagesData.get("socks.webp"));
			Coupon coupon54 = new Coupon(company9Id, Category.Fashion, "Sun Glasses",
					"MERRY'S Unisex Polarized Aluminum Sunglasses Vintage Sun Glasses For Men/Women S8286", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 3)), 100, randomPrice(100, 400),
					imagesData.get("sun_glasses.webp"));
			Coupon coupon55 = new Coupon(company9Id, Category.Fashion, "Toe Ring",
					"925 Sterling Silver Toe Ring, BoRuo Wave Hawaiian Adjustable Band Ring", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 4)), 100, randomPrice(2, 5),
					imagesData.get("toe_ring.webp"));
			Coupon coupon56 = new Coupon(company9Id, Category.Fashion, "Watch", "Timex Men's Expedition Scout 40 Watch",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 3)), 100, randomPrice(100, 400),
					imagesData.get("watch.webp"));
			Coupon coupon57 = new Coupon(company9Id, Category.Fashion, "Towel",
					"JML Microfiber Bath Towel 2 Pack(30\" x 60\"), Oversized, Soft, Super Absorbent and Fast Drying, No Fading Multipurpose Use for Sports, Travel, Fitness, Yoga, Grey",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 3)), 100, randomPrice(100, 400),
					imagesData.get("towel.webp"));
			Coupon coupon58 = new Coupon(company9Id, Category.Fashion, "Zipper",
					"YaHoGa 2PCS #5 32 Inch Separating Jacket Zippers for Sewing Coats Jacket Zipper Black Molded Plastic Zippers Bulk (32\" 2pc)",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 3)), 100, randomPrice(100, 400),
					imagesData.get("zipper.webp"));

			companyFacade6.addCoupon(coupon36);
			companyFacade6.addCoupon(coupon37);
			companyFacade6.addCoupon(coupon38);
			companyFacade6.addCoupon(coupon39);
			companyFacade6.addCoupon(coupon40);
			companyFacade6.addCoupon(coupon41);

			companyFacade7.addCoupon(coupon42);
			companyFacade7.addCoupon(coupon43);
			companyFacade7.addCoupon(coupon44);
			companyFacade7.addCoupon(coupon45);
			companyFacade7.addCoupon(coupon46);
			companyFacade7.addCoupon(coupon47);

			companyFacade8.addCoupon(coupon48);
			companyFacade8.addCoupon(coupon49);
			companyFacade8.addCoupon(coupon50);

			companyFacade9.addCoupon(coupon51);
			companyFacade9.addCoupon(coupon52);
			companyFacade9.addCoupon(coupon53);
			companyFacade9.addCoupon(coupon54);
			companyFacade9.addCoupon(coupon55);
			companyFacade9.addCoupon(coupon56);
			companyFacade9.addCoupon(coupon57);
			companyFacade9.addCoupon(coupon58);

			List<Coupon> companyFacade6Coupons = companyFacade6.getCompanyCoupons();
			List<Coupon> companyFacade7Coupons = companyFacade7.getCompanyCoupons();
			List<Coupon> companyFacade8Coupons = companyFacade8.getCompanyCoupons();
			List<Coupon> companyFacade9Coupons = companyFacade9.getCompanyCoupons();

			for (Coupon coupon : companyFacade6Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {

					customerFacade3.purchaseCoupon(coupon);
					customerFacade4.purchaseCoupon(coupon);
					customerFacade5.purchaseCoupon(coupon);
				}
				if (coupon.getId() % 3 == 0) {

					customerFacade6.purchaseCoupon(coupon);
					customerFacade7.purchaseCoupon(coupon);
					customerFacade8.purchaseCoupon(coupon);
				}
				customerFacade9.purchaseCoupon(coupon);
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}
			for (Coupon coupon : companyFacade7Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				
				customerFacade2.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {

					customerFacade3.purchaseCoupon(coupon);
					customerFacade4.purchaseCoupon(coupon);
					customerFacade5.purchaseCoupon(coupon);
					customerFacade6.purchaseCoupon(coupon);
					customerFacade7.purchaseCoupon(coupon);
				}
				if (coupon.getId() % 3 == 0) {

					customerFacade8.purchaseCoupon(coupon);
					customerFacade9.purchaseCoupon(coupon);
				}
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}
			for (Coupon coupon : companyFacade8Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {

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
			for (Coupon coupon : companyFacade9Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {

					customerFacade3.purchaseCoupon(coupon);
					customerFacade4.purchaseCoupon(coupon);
					customerFacade5.purchaseCoupon(coupon);
					customerFacade6.purchaseCoupon(coupon);
					customerFacade7.purchaseCoupon(coupon);
				}
				if (coupon.getId() % 3 == 0) {

					customerFacade8.purchaseCoupon(coupon);
					customerFacade9.purchaseCoupon(coupon);
				}
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}
			// Furniture companies
			Company company10 = new Company("shopture", "shopture@shopture.com", "shopture1234");
			Company company11 = new Company("cedarhome", "cedarhome@cedarhome.com", "cedarhome1234");
			Company company12 = new Company("craftial", "craftial@craftial.com", "craftial1234");
			Company company13 = new Company("handcraftd", "handcraftd@handcraftd.com", "handcraftd1234");

			adminFacade.addCompany(company10);
			adminFacade.addCompany(company11);
			adminFacade.addCompany(company12);
			adminFacade.addCompany(company13);

			CompanyFacade companyFacade10 = (CompanyFacade) loginManager.login("shopture@shopture.com", "shopture1234",
					ClientType.Company);
			CompanyFacade companyFacade11 = (CompanyFacade) loginManager.login("cedarhome@cedarhome.com", "cedarhome1234",
					ClientType.Company);
			CompanyFacade companyFacade12 = (CompanyFacade) loginManager.login("craftial@craftial.com", "craftial1234",
					ClientType.Company);
			CompanyFacade companyFacade13 = (CompanyFacade) loginManager.login("handcraftd@handcraftd.com", "handcraftd1234",
					ClientType.Company);

			int company10Id = companyFacade10.getCompanyDetails().getId();
			int company11Id = companyFacade11.getCompanyDetails().getId();
			int company12Id = companyFacade12.getCompanyDetails().getId();
			int company13Id = companyFacade13.getCompanyDetails().getId();

			Coupon coupon59 = new Coupon(company10Id, Category.Furniture, "Zinus Shalini Queen Bed",
					"Zinus Shalini Upholstered Platform Bed / Mattress Foundation / Wood Slat Support / No Box Spring Needed / Easy Assembly, Queen", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 3)), 100, randomPrice(800, 1200),
					imagesData.get("bed.webp"));
			Coupon coupon60 = new Coupon(company10Id, Category.Furniture, "Cozy Blanket",
					"Bedsure Fleece Blanket Throw Size Navy Lightweight Super Soft Cozy Luxury Bed Blanket Microfiber", today, new Date(System.currentTimeMillis() + (dayInMillis * 7)),
					100, randomPrice(5, 20), imagesData.get("blanket.webp"));
			Coupon coupon61 = new Coupon(company10Id, Category.Furniture, "Packing Box",
					"Cardboard box for packing and stuff",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 100, randomPrice(1, 3),
					imagesData.get("box.webp"));
			Coupon coupon62 = new Coupon(company10Id, Category.Furniture, "White Candle",
					"Standard white candle , 10 pcs", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(3, 7),
					imagesData.get("candle.webp"));
			Coupon coupon63 = new Coupon(company10Id, Category.Furniture, "Canvas Painting of Tree",
					"Canvas painting of tree", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(45, 90),
					imagesData.get("canvas.webp"));
			
			Coupon coupon64 = new Coupon(company11Id, Category.Furniture, "Sofa",
					"Homelegance Platina 103\" Sectional Sofa with Pull Out Bed and Ottoman, Chocolate Fabric",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(450, 650),
					imagesData.get("couch.webp"));
			Coupon coupon65 = new Coupon(company11Id, Category.Furniture, "Armchair",
					"Rivet Revolve Modern Upholstered Armchair with Tapered Legs, 33\"W, Heritage Green", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 5)), 100, randomPrice(200, 250),
					imagesData.get("chair.webp"));
			Coupon coupon66 = new Coupon(company11Id, Category.Furniture, "Desk",
					"Writing Computer Desk Modern Simple Study Desk Industrial Style Folding Laptop Table for Home Office Notebook Desk Brown Desktop Black Frame", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 7)), 100, randomPrice(150, 200),
					imagesData.get("desk.webp"));
			
			
			Coupon coupon67 = new Coupon(company12Id, Category.Furniture, "Stylish nits bracelet",
					"Door",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 100, randomPrice(53, 203),
					imagesData.get("door.webp"));
			Coupon coupon68 = new Coupon(company12Id, Category.Furniture, "Drawer",
					"ROMOON Dresser Organizer with 5 Drawers, Fabric Dresser Tower for Bedroom, Hallway, Entryway, Closets - Gray",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(130, 170),
					imagesData.get("drawer.webp"));
			Coupon coupon69 = new Coupon(company12Id, Category.Furniture, "Synthetic flowers",
					"Synthetic flowers", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(10, 20),
					imagesData.get("fake_flowers.webp"));
			Coupon coupon70 = new Coupon(company12Id, Category.Furniture, "Flowers",
					"Flowers",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(13, 15),
					imagesData.get("flowers.webp"));

			Coupon coupon71 = new Coupon(company13Id, Category.Furniture, "Glow Stick",
					"Glow Stick",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 2)), 100, randomPrice(1200, 2000),
					imagesData.get("glow_stick.webp"));
			Coupon coupon72 = new Coupon(company13Id, Category.Furniture, "House",
					"House",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(1_000_000, 1_200_000),
					imagesData.get("house.webp"));
			Coupon coupon73 = new Coupon(company13Id, Category.Furniture, "Lamp Shade",
					"Lamp Shade", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(50, 70),
					imagesData.get("lamp_shade.webp"));

			companyFacade10.addCoupon(coupon59);
			companyFacade10.addCoupon(coupon60);
			companyFacade10.addCoupon(coupon61);
			companyFacade10.addCoupon(coupon62);
			companyFacade10.addCoupon(coupon63);

			companyFacade11.addCoupon(coupon64);
			companyFacade11.addCoupon(coupon65);
			companyFacade11.addCoupon(coupon66);
		

			companyFacade12.addCoupon(coupon67);
			companyFacade12.addCoupon(coupon68);
			companyFacade12.addCoupon(coupon69);
			companyFacade12.addCoupon(coupon70);

			companyFacade13.addCoupon(coupon71);
			companyFacade13.addCoupon(coupon72);
			companyFacade13.addCoupon(coupon73);


			List<Coupon> companyFacade10Coupons = companyFacade10.getCompanyCoupons();
			List<Coupon> companyFacade11Coupons = companyFacade11.getCompanyCoupons();
			List<Coupon> companyFacade12Coupons = companyFacade12.getCompanyCoupons();
			List<Coupon> companyFacade13Coupons = companyFacade13.getCompanyCoupons();

			for (Coupon coupon : companyFacade10Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {

					customerFacade3.purchaseCoupon(coupon);
					customerFacade4.purchaseCoupon(coupon);
					customerFacade5.purchaseCoupon(coupon);
				}
				if (coupon.getId() % 3 == 0) {

					customerFacade6.purchaseCoupon(coupon);
					customerFacade7.purchaseCoupon(coupon);
					customerFacade8.purchaseCoupon(coupon);
				}
				customerFacade9.purchaseCoupon(coupon);
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}
			for (Coupon coupon : companyFacade11Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {

					customerFacade3.purchaseCoupon(coupon);
					customerFacade4.purchaseCoupon(coupon);
					customerFacade5.purchaseCoupon(coupon);
				}
				if (coupon.getId() % 3 == 0) {

					customerFacade6.purchaseCoupon(coupon);
					customerFacade7.purchaseCoupon(coupon);
					customerFacade8.purchaseCoupon(coupon);
				}
				customerFacade9.purchaseCoupon(coupon);
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}
			for (Coupon coupon : companyFacade12Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {

					customerFacade3.purchaseCoupon(coupon);
					customerFacade4.purchaseCoupon(coupon);
					customerFacade5.purchaseCoupon(coupon);
				}
				if (coupon.getId() % 3 == 0) {

					customerFacade6.purchaseCoupon(coupon);
					customerFacade7.purchaseCoupon(coupon);
					customerFacade8.purchaseCoupon(coupon);
				}
				customerFacade9.purchaseCoupon(coupon);
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}
			for (Coupon coupon : companyFacade13Coupons) {
				customerFacade1.purchaseCoupon(coupon);
				customerFacade2.purchaseCoupon(coupon);
				if (coupon.getId() % 2 == 0) {

					customerFacade3.purchaseCoupon(coupon);
					customerFacade4.purchaseCoupon(coupon);
					customerFacade5.purchaseCoupon(coupon);
				}
				if (coupon.getId() % 3 == 0) {

					customerFacade6.purchaseCoupon(coupon);
					customerFacade7.purchaseCoupon(coupon);
					customerFacade8.purchaseCoupon(coupon);
				}
				customerFacade9.purchaseCoupon(coupon);
				customerFacade10.purchaseCoupon(coupon);
				customerFacade11.purchaseCoupon(coupon);
			}
			
			
			////////
			Company company14 = new Company("jiraffe", "jiraffe@jiraffe.com", "jiraffe1234");
			
			adminFacade.addCompany(company14);
			
			CompanyFacade companyFacade14 = (CompanyFacade) loginManager.login("jiraffe@jiraffe.com", "jiraffe1234", ClientType.Company);
			
			int company14Id = companyFacade14.getCompanyDetails().getId();
			
			Coupon coupon74 = new Coupon(company14Id, Category.Pets, "Cat Castle",
					"PAWZ Road Cat Tree Luxury Cat Tower with Double Condos, Spacious Perch, Fully Wrapped Scratching Sisal Posts and Replaceable Dangling Balls", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 5)), 100, randomPrice(200, 250),
					imagesData.get("cat_castle.webp"));
			Coupon coupon75 = new Coupon(company14Id, Category.Pets, "Stylish cat glasses",
					"Glasses for cats", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 7)), 100, randomPrice(25, 30),
					imagesData.get("cat_glasses.jpg"));
			
			
			Coupon coupon76 = new Coupon(company14Id, Category.Pets, "Hodden Cat Litter Box",
					"PetSafe ScoopFree Ultra Automatic Self Cleaning Hooded Cat Litter Box – Includes Disposable Trays with Crystal Litter and Hood - 2 Colors",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 100, randomPrice(53, 203),
					imagesData.get("cat_toilet.jpg"));
			Coupon coupon77 = new Coupon(company14Id, Category.Pets, "Fun toy feeder",
					"Fun toy feeder",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(15, 16),
					imagesData.get("toy_feeder.jpg"));
			Coupon coupon78 = new Coupon(company14Id, Category.Pets, "Dog Bed",
					"PetFusion Ultimate Dog Bed, CertiPUR-US Orthopedic Memory Foam, Size/Color Options, Medium Firmness Pillow, Waterproof Liner, YKK Zippers, Breathable 35% Cotton, Cert. Skin Contact Safe, 3yr. Warranty", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(10, 20),
					imagesData.get("dog_bed.jpg"));
			Coupon coupon79 = new Coupon(company14Id, Category.Pets, "Dog House",
					"always-quality Indoor Outdoor Dog House Small to Medium Pet All Weather Doghouse Puppy Shelter",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(13, 15),
					imagesData.get("dog_house.jpg"));

			Coupon coupon80 = new Coupon(company14Id, Category.Pets, "Dog Haircut Groomer",
					"oneisall Dog Shaver Clippers Low Noise Rechargeable Cordless Electric Quiet Hair Clippers Set for Dogs Cats Pets",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 2)), 100, randomPrice(70, 90),
					imagesData.get("groomer.webp"));
			Coupon coupon81 = new Coupon(company14Id, Category.Pets, "Leash",
					"Leash",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(1_000_000, 1_200_000),
					imagesData.get("leash.jpg"));
			Coupon coupon82 = new Coupon(company14Id, Category.Pets, "Chew toy",
					"Chew toy", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(50, 70),
					imagesData.get("loop_chew_toy.jpg"));
			companyFacade14.addCoupon(coupon74);
			companyFacade14.addCoupon(coupon75);
			companyFacade14.addCoupon(coupon76);
			companyFacade14.addCoupon(coupon77);
			companyFacade14.addCoupon(coupon78);
			companyFacade14.addCoupon(coupon79);
			companyFacade14.addCoupon(coupon80);
			companyFacade14.addCoupon(coupon81);
			companyFacade14.addCoupon(coupon82);
			
			List<Coupon> companyFacade14Coupons = companyFacade14.getCompanyCoupons();
			
			for(Coupon coupon : companyFacade14Coupons) {
				if(flipCoin()) {
					customerFacade1.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade2.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade3.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade4.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade5.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade6.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade7.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade8.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade9.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade10.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade11.purchaseCoupon(coupon);					
				}
			}
			////////////
			Company company15 = new Company("beautivia", "beautivia@beautivia.com", "beautivia1234");
			Company company16 = new Company("beautic", "beautic@beautic.com", "beautic1234");
			
			adminFacade.addCompany(company15);
			adminFacade.addCompany(company16);
			
			CompanyFacade companyFacade15 = (CompanyFacade) loginManager.login("beautivia@beautivia.com", "beautivia1234", ClientType.Company);
			CompanyFacade companyFacade16 = (CompanyFacade) loginManager.login("beautic@beautic.com", "beautic1234", ClientType.Company);
			
			int company15Id = companyFacade15.getCompanyDetails().getId();
			int company16Id = companyFacade16.getCompanyDetails().getId();
			
			Coupon coupon83 = new Coupon(company15Id, Category.Spa, "Conditioner",
					"Conditiner", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 5)), 100, randomPrice(10, 20),
					imagesData.get("conditioner.webp"));
			Coupon coupon84 = new Coupon(company15Id, Category.Spa, "Stylish eye liner",
					"Glasses for cats", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 7)), 100, randomPrice(25, 30),
					imagesData.get("eye_liner.webp"));
			
			Coupon coupon85 = new Coupon(company15Id, Category.Spa, "Face Wash",
					"Face Wash",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 100, randomPrice(13,16),
					imagesData.get("face_wash.webp"));
			Coupon coupon86 = new Coupon(company15Id, Category.Spa, "Hair brush",
					"Hair brush",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 7)), 100, randomPrice(15, 16),
					imagesData.get("hair_brush.webp"));
			Coupon coupon87 = new Coupon(company15Id, Category.Spa, "Nice lip gloss",
					"Lip Gloss", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 5)), 100, randomPrice(10, 20),
					imagesData.get("lip_gloss.webp"));
			Coupon coupon88 = new Coupon(company15Id, Category.Spa, "Lotion",
					"Lotion",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 4)), 100, randomPrice(13, 15),
					imagesData.get("lotion.webp"));

			
			Coupon coupon89 = new Coupon(company16Id, Category.Spa, "Nail Clippers",
					"Nail Clippers",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 2)), 100, randomPrice(70, 90),
					imagesData.get("nail_clippers.webp"));
			Coupon coupon90 = new Coupon(company16Id, Category.Spa, "Nail File",
					"Nail File",
					today, new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(1_000_000, 1_200_000),
					imagesData.get("nail_file.webp"));
			Coupon coupon91 = new Coupon(company16Id, Category.Spa, "Needle",
					"Needle", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(50, 70),
					imagesData.get("needle.webp"));
			Coupon coupon92 = new Coupon(company16Id, Category.Spa, "Perfume",
					"Perfume", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(50, 70),
					imagesData.get("perfume.webp"));
			Coupon coupon93 = new Coupon(company16Id, Category.Spa, "shampoo",
					"shampoo", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(50, 70),
					imagesData.get("shampoo.webp"));
			Coupon coupon94 = new Coupon(company16Id, Category.Spa, "tissue box",
					"tissue box", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(50, 70),
					imagesData.get("tissue_box.webp"));
			Coupon coupon95 = new Coupon(company16Id, Category.Spa, "toothbrush",
					"toothbrush", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(50, 70),
					imagesData.get("toothbrush.webp"));
			Coupon coupon96 = new Coupon(company16Id, Category.Spa, "toothpaste",
					"toothpaste", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(50, 70),
					imagesData.get("toothpaste.webp"));
			Coupon coupon97 = new Coupon(company16Id, Category.Spa, "towel",
					"towel", today,
					new Date(System.currentTimeMillis() + (dayInMillis * 10)), 100, randomPrice(50, 70),
					imagesData.get("towel.webp"));
			companyFacade15.addCoupon(coupon83);
			companyFacade15.addCoupon(coupon84);
			companyFacade15.addCoupon(coupon85);
			companyFacade15.addCoupon(coupon86);
			companyFacade15.addCoupon(coupon87);
			companyFacade15.addCoupon(coupon88);
			
			companyFacade16.addCoupon(coupon89);
			companyFacade16.addCoupon(coupon90);
			companyFacade16.addCoupon(coupon91);
			companyFacade16.addCoupon(coupon92);
			companyFacade16.addCoupon(coupon93);
			companyFacade16.addCoupon(coupon94);
			companyFacade16.addCoupon(coupon95);
			companyFacade16.addCoupon(coupon96);
			companyFacade16.addCoupon(coupon97);
			
			
			List<Coupon> companyFacade15Coupons = companyFacade15.getCompanyCoupons();
			List<Coupon> companyFacade16Coupons = companyFacade16.getCompanyCoupons();
			
			for(Coupon coupon : companyFacade15Coupons) {
				if(flipCoin()) {
					customerFacade1.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade2.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade3.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade4.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade5.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade6.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade7.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade8.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade9.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade10.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade11.purchaseCoupon(coupon);					
				}
			}
			for(Coupon coupon : companyFacade16Coupons) {
				if(flipCoin()) {
					customerFacade1.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade2.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade3.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade4.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade5.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade6.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade7.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade8.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade9.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade10.purchaseCoupon(coupon);					
				}
				if(flipCoin()) {
					customerFacade11.purchaseCoupon(coupon);					
				}
			}
			System.out.println("DONE CREATING TEST COUPONS");

		} catch ( IncorrectCredentialsException | CompanyNameAlreadyExistsException
				| CompanyEmailAlreadyExistsException | CouponTitleAlreadyExistsException
				| CustomerEmailAlreadyExistsException | CouponAlreadyPurchasedException | CouponOutOfStockException
				| CouponExpiredException | CouponDoesntExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public int randomPrice(int lowerLimit, int upperLimit) {
		return (int) (lowerLimit + Math.random() * (upperLimit - lowerLimit));
	}
	public boolean flipCoin() {
		return ((int) (Math.random() * 2) ) == 1 ? true : false;
	}

	
}
