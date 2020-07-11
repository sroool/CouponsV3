
package com.example.CouponsV3.exceptions;


public class CompanyDoesntExistException extends Exception {

	private static final long serialVersionUID = -1039764458749374415L;
	public CompanyDoesntExistException() {
		
	}
	public CompanyDoesntExistException(String message) {
		super(message);
	}
}
