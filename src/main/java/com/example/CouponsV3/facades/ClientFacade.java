package com.example.CouponsV3.facades;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.CouponsV3.dao.CompanyRepo;
import com.example.CouponsV3.dao.CouponRepo;
import com.example.CouponsV3.dao.CustomerRepo;


/**
 * Client Facade parent class 

 */
@Service
@Scope("prototype")
public abstract class ClientFacade {
	@Autowired
	protected CompanyRepo compRepo;
	@Autowired
	protected CouponRepo coupRepo;
	@Autowired
	protected CustomerRepo custRepo;
	
	public abstract boolean login(String email, String password);
}
