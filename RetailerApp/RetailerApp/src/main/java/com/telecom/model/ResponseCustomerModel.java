package com.telecom.model;

import java.util.ArrayList;
import java.util.List;


public class ResponseCustomerModel 
{
	private List<String> errors = new  ArrayList<String>();
	private CustomerOrder customerOrder = new CustomerOrder();
	
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public CustomerOrder getCustomerOrder() {
		return customerOrder;
	}
	public void setCustomerOrder(CustomerOrder customerOrder) {
		this.customerOrder = customerOrder;
	}
}
