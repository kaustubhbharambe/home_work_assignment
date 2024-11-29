package com.telecom.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "RETAIL_ORDER")
public class RetailOrder {
	
	@Id
    private String id;
	private double amount;
	private String retailerName;
	private Date orderDate;
	private String mode;
	private Double rewardPoints = null;
	
	public String getId() 
	{
		return id;
	}
	public void setId(String id) 
	{
		this.id = id;
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
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Double getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(Double rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
}
