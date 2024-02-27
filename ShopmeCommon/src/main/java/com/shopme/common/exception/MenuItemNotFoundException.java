package com.shopme.common.exception;

@SuppressWarnings("serial")
public class MenuItemNotFoundException extends Exception {

	public MenuItemNotFoundException(String message) {
		super(message);
	}

}