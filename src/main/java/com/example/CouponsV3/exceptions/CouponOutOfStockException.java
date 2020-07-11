
package com.example.CouponsV3.exceptions;


public class CouponOutOfStockException extends Exception {

	private static final long serialVersionUID = -4538193031915983251L;

	public CouponOutOfStockException(String message) {
		super(message);
	}
}
