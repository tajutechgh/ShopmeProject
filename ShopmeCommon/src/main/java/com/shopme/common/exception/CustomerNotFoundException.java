package com.shopme.common.exception;

@SuppressWarnings("serial")
public class CustomerNotFoundException extends Exception {

	public CustomerNotFoundException(String message) {
		
		super(message);
	}

}