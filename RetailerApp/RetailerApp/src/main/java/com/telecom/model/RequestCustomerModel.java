package com.telecom.model;

import jakarta.validation.constraints.NotBlank;

public class RequestCustomerModel 
{
	@NotBlank(message = "Please provide customer Name")
	private String customerName;
	@NotBlank(message = "Please provide date")
	private String date;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
