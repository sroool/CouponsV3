/**
 * 
 */
package com.example.CouponsV3.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.CouponsV3.beans.Company;


/**
 * Companies Repository Interface
 * This interface is used to achieve complete separation between the data layer and 
 * the application logic layer.
 *
 */
public interface CompanyRepo extends JpaRepository<Company, Integer> {
	
	/**
	 * returns an Optional Object containing the company corresponding to the provided
	 * email and password
	 * 
	 * @param email Company Email
	 * @param password Company Password
	 * @return
	 */
	Optional<Company> findByEmailAndPassword(String email, String password);
	
	Optional<Company> findByEmail(String email);
	/**
	 * Helper method to reset AI to a certain value
	 * @param start
	 */
	@Transactional
	@Modifying
//	@Query(value = "ALTER TABLE companies AUTO_INCREMENT = ?1", nativeQuery = true) // MySql query
	@Query(value = "ALTER SEQUENCE companies_id_seq RESTART WITH 1", nativeQuery = true) // postgresql query
	void resetAutoIncrement(int start);
	
}
