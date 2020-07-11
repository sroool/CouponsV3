
package com.example.CouponsV3.exceptions;


public class CouponDoesntExistException extends Exception {

	private static final long serialVersionUID = -9175319403158373020L;
	public CouponDoesntExistException(String message) {
		super(message);
	}
}
