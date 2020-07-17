package com.example.CouponsV3.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.CouponsV3.beans.Customer;
/**
 * Customers Repository Interface
 * This interface is used to achieve complete separation between the data layer and 
 * the application logic layer.
 *
 */
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
	/**
	 * returns an Optional Object containing the customer corresponding to the provided
	 * email and password
	 *  
	 * @param email
	 * @param password
	 * @return
	 */
	Optional<Customer> findByEmailAndPassword(String email, String password);
	/**
	 * returns an Optional Object containing the company corresponding to the provided
	 * email
	 * @param email
	 * @return
	 */
	Optional<Customer> findByEmail(String email);
	
	/**
	 * Helper method to reset AI to a certain value
	 * @param start
	 */
	@Transactional
	@Modifying
//	@Query(value = "ALTER TABLE customers AUTO_INCREMENT = ?1", nativeQuery = true) mysql query
	@Query(value = "ALTER SEQUENCE customers_id_seq RESTART WITH 1", nativeQuery = true) // postgresql query
	void resetAutoIncrement(int start);
}
