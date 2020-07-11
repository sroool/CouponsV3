
package com.example.CouponsV3.exceptions;



public class CouponAlreadyPurchasedException extends Exception {

	private static final long serialVersionUID = -505766662855629177L;

	public CouponAlreadyPurchasedException(String message) {
		super(message);
	}
}
