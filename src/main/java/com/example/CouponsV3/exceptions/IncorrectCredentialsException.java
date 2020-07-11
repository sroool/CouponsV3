package com.example.CouponsV3.exceptions;

public class IncorrectCredentialsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1328026148996648770L;

	public IncorrectCredentialsException() {
		super("One of the details you eneted is wrong");
	}

}
