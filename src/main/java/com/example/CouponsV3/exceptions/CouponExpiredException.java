
package com.example.CouponsV3.exceptions;


public class CouponExpiredException extends Exception {

	private static final long serialVersionUID = -3715356561076970920L;

	public CouponExpiredException(String message) {
		super(message);
	}
}
