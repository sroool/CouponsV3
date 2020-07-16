package com.example.CouponsV3.facades;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.example.CouponsV3.exceptions.IncorrectCredentialsException;
@Service
public class LoginManager {
	@Autowired
	private ApplicationContext ctx;
	
	

	/**
	 * Accepts an email, password and a clientType enum and returns an appropriate ClientFacade object if the credentials are
	 * correct or null if not.
	 * 
	 * @param email the email of the Client trying to log in
	 * @param password the password of the Client trying to log in
	 * @param clientType the clientType of the Client trying to log in
	 * @return an instance of AdminFacade, CompanyFacade or ClientFacde if the credentials are correct or null if not
	 * @throws IncorrectCredentialsException
	 */
	public ClientFacade login(String email, String password, ClientType clientType)  throws IncorrectCredentialsException{
		switch(clientType) {
		case Administrator:
			AdminFacade adminF =  ctx.getBean(AdminFacade.class);
			if(adminF.login(email, password)) {
				return adminF;
			}
			throw new IncorrectCredentialsException();
		
		case Company:
			CompanyFacade compF = ctx.getBean(CompanyFacade.class);
			if(compF.login(email, password)) {
				return compF;
			}
			throw new IncorrectCredentialsException();
			
		case Customer:
			CustomerFacade custF = ctx.getBean(CustomerFacade.class);
			if(custF.login(email, password)) {
				return custF;
			}
			throw new IncorrectCredentialsException();
		}
		return null;
	}
}
