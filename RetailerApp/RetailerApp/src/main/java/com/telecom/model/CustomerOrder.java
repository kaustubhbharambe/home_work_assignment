package com.telecom.model;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrder 
{
	private String customerName;
	private String totalAmount;
	private String totalRewardPoints;
	private List<Order> orders = new ArrayList<Order>();
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getTotalRewardPoints() {
		return totalRewardPoints;
	}
	public void setTotalRewardPoints(String totalRewardPoints) {
		this.totalRewardPoints = totalRewardPoints;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
