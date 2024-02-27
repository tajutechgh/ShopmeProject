package com.shopme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.Customer;
import com.shopme.customer.UserCustomerService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class ControllerHelper {
	
	@Autowired 
	private UserCustomerService customerService;
	
	public Customer getAuthenticatedCustomer(HttpServletRequest request) {
		
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		return customerService.getCustomerByEmail(email);
	}
}