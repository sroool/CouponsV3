package com.example.CouponsV3.dao;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.CouponsV3.beans.Category;
import com.example.CouponsV3.beans.Coupon;
/**
 * Coupons Repository Interface
 * This interface is used to achieve complete separation between the data layer and 
 * the application logic layer.
 *
 */
public interface CouponRepo extends JpaRepository<Coupon, Integer> {
	/**
	 * Deletes entries from customers_vs_coupons by the coupon company_id using JOIN
	 * 
	 * @param companyId The company id of the coupon to delete
	 */
	@Modifying
	@Transactional
	//@Query(value = "DELETE c FROM customers_vs_coupons c JOIN coupons d ON c.coupon_id=d.id WHERE d.company_id = ?1 ", nativeQuery = true) MySQL QUERY
	@Query(value = "DELETE FROM customers_vs_coupons c USING coupons as d WHERE c.coupon_id=d.id AND d.company_id = ?1;", nativeQuery = true) // POSTGRESQL QUERY
	void deletePurchasesByCompanyId(int companyId);
	/**
	 * Deletes entries from customers_vs_coupons by the customer_id given
	 * 
	 * @param customerId The customer id to delete
	 */
	
	@Modifying
	@Transactional
//	@Query(value = "DELETE c FROM customers_vs_coupons c WHERE c.customer_id = ?1", nativeQuery = true) MySQL QUERY
	@Query(value = "DELETE FROM customers_vs_coupons WHERE customer_id = ?1 ;", nativeQuery = true) //PostgreSQL QUERY
	void deletePurchaseByCustomerId(int customerId);
	/**
	 * Deletes entries from customers_vs_coupons by the coupon_id given
	 * 
	 * @param couponId The coupon id to delete
	 */
	@Modifying
	@Transactional
//	@Query(value = "DELETE c FROM customers_vs_coupons c WHERE c.coupon_id = ?1", nativeQuery = true) MySQL Query
	@Query(value = "DELETE FROM customers_vs_coupons WHERE coupon_id= ?1", nativeQuery = true) //PostgreSQL Query
	void deletePurchaseByCouponId(int couponId);
	/**
	 * Deletes entries from customers_vs_coupons by the coupon enddate using JOIN
	 * @param date The date used to determine which entries to delete
	 */
	@Modifying
	@Transactional
//	@Query(value = "DELETE c FROM customers_vs_coupons c JOIN coupons b ON c.coupon_id=b.id WHERE b.end_date < ?1 ", nativeQuery = true) MySQL Query
	@Query(value = "DELETE FROM customers_vs_coupons c USING coupons as d WHERE c.coupon_id=d.id AND d.end_date < ?1;", nativeQuery = true) //PostGresql query
	void deletePurchaseByEnddate(Date date);
	/**
	 * Deletes entries from coupons by the coupon company_id
	 * @param companyId
	 */
	@Modifying
	@Transactional
	@Query("DELETE Coupon c WHERE c.companyId = ?1 ")
	void deleteByCompanyId(int companyId);
	/**
	 * Deletes entries from coupons by the end_date
	 * @param date
	 */
	@Modifying
	@Transactional
	@Query("DELETE Coupon c WHERE c.endDate < ?1")
	void deleteByEnddate(Date date);
	/**
	 * Returns entries from coupons by company_id and category 
	 * @param companyId
	 * @param category
	 * @return
	 */
	List<Coupon> findByCompanyIdAndCategory(int companyId, Category category);
	/**
	 * Returns entries from coupons by company_id and category 
	 * @param companyId
	 * @param category
	 * @return
	 */
	List<Coupon> findByCompanyIdAndPriceLessThan(int companyId, double maxPrice);
	/**
	 * Returns entries from coupons by customer_id from customers_vs_coupons and category using JOIN
	 * @param customerId
	 * @param category
	 * @return
	 */
	@Query(value = "SELECT * FROM coupons d JOIN customers_vs_coupons c ON c.coupon_id=d.id WHERE c.customer_id= ?1 AND d.category = ?2", nativeQuery = true) // MySQL and postgresql query
	Set<Coupon> findByCustomerIdAndCategory(int customerId, int category);
	/**
	 * Returns entries from coupons by customer_id from customers_vs_coupons and a given maximum price
	 * @param customerId
	 * @param maxPrice
	 * @return
	 */
	@Query(value = "SELECT * FROM coupons d JOIN customers_vs_coupons c ON c.coupon_id=d.id WHERE c.customer_id= ?1 AND d.price <= ?2", nativeQuery = true) // MySQL and postgresql query
	Set<Coupon> findByCustomerIdAndMaxprice(int customerId, double maxPrice);
	
	Optional<Coupon> findByTitle(String title);
	/**
	 * Helper method to reset AI to a certain value
	 * @param start
	 */
	@Transactional
	@Modifying
//	@Query(value = "ALTER TABLE coupons AUTO_INCREMENT = ?1", nativeQuery = true) // MySQL Query
	@Query(value = "ALTER SEQUENCE coupons_id_seq RESTART WITH ?1;", nativeQuery = true) // PostGresql QUERY
	void resetAutoIncrement(int start);
	
	
}
