package com.telecom.model;

import jakarta.validation.constraints.NotBlank;

public class RequestOrderModel 
{
	@NotBlank(message = "Please provide customer Name")
	private String customerName;
	@NotBlank(message = "Please provide order date")
	private String orderDate;
	@NotBlank(message = "Please provide amount")
	private String amount;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
