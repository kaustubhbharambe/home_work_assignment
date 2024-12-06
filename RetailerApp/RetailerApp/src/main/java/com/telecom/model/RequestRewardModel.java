package com.telecom.model;

import jakarta.validation.constraints.NotBlank;

public class RequestRewardModel 
{
	@NotBlank(message = "Please provide customer Name")
	private String customerName;
	@NotBlank(message = "Please provide total months")
	private String totalMonths;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getTotalMonths() {
		return totalMonths;
	}
	public void setTotalMonths(String totalMonths) {
		this.totalMonths = totalMonths;
	}
}
