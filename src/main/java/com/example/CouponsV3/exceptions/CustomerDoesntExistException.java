
package com.example.CouponsV3.exceptions;


public class CustomerDoesntExistException extends Exception {


	private static final long serialVersionUID = 6276060075565782637L;
	public CustomerDoesntExistException() {
		super();
	}
	public CustomerDoesntExistException(String message) {
		super(message);
	}
}
