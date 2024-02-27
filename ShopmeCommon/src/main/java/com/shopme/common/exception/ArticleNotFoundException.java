package com.shopme.common.exception;

@SuppressWarnings("serial")
public class ArticleNotFoundException extends Exception {

	public ArticleNotFoundException(String message) {
		super(message);
	}

}