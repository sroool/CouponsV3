package com.example.CouponsV3.web;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CouponsV3.exceptions.IncorrectCredentialsException;
import com.example.CouponsV3.facades.ClientFacade;
import com.example.CouponsV3.facades.ClientType;
import com.example.CouponsV3.facades.CompanyFacade;
import com.example.CouponsV3.facades.CustomerFacade;
import com.example.CouponsV3.facades.LoginManager;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private Map<String, Session> sessions;
	@PostMapping("{email}/{password}/{clientType}")
	public ResponseEntity<?> login(@PathVariable String email, @PathVariable String password, @PathVariable ClientType clientType){
		
		try {
			ClientFacade clientFacade = loginManager.login(email, password, clientType);
			String token = UUID.randomUUID().toString();
			Session newSession = new Session(clientFacade, System.currentTimeMillis());
			sessions.put(token, newSession);
			return ResponseEntity.ok(token);
		} catch (IncorrectCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
	@PostMapping("/logout/{token}")
	public ResponseEntity<?> logout(@PathVariable String token){		
		sessions.remove(token);
		return ResponseEntity.ok("Logged out succesfully");
	}
	@PostMapping("/loggedin/{token}")
	public ResponseEntity<?> loggedin(@PathVariable String token){
		if(sessions.get(token) != null) {
			ClientFacade cf = sessions.get(token).getClientFacade();
			
			if(cf instanceof CompanyFacade)
				return ResponseEntity.ok("Company");
			else if(cf instanceof CustomerFacade)
				return ResponseEntity.ok("Customer");
			else
				return ResponseEntity.ok( "Administrator" );
			
			
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
	}
}
