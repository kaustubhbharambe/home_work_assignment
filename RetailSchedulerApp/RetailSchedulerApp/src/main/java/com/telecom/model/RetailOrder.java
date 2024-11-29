package com.telecom.model;


public class RetailOrder {
	

    private String _id;
	private double amount;
	private String retailerName;
	private String orderDate;
	private String mode;
	private double rewardPoints;
	private String _class;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public double getAmount() 
	{
		return amount;
	}
	public void setAmount(double amount) 
	{
		this.amount = amount;
	}
	public String getRetailerName() 
	{
		return retailerName;
	}
	public void setRetailerName(String retailerName) 
	{
		this.retailerName = retailerName;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public double getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(double rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public String get_class() {
		return _class;
	}
	public void set_class(String _class) {
		this._class = _class;
	}
}
