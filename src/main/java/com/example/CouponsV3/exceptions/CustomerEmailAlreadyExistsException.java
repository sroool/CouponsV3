
package com.example.CouponsV3.exceptions;



public class CustomerEmailAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -7092602415237036915L;

	public CustomerEmailAlreadyExistsException(String message) {
		super(message);
	}
}
