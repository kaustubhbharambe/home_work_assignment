package com.telecom.service;

import org.springframework.http.ResponseEntity;

import com.telecom.model.RequestCustomerModel;
import com.telecom.model.RequestOrderModel;
import com.telecom.model.RequestRewardModel;
import com.telecom.model.ResponseCustomerModel;

public interface RetailService 
{
	
	public ResponseEntity<ResponseCustomerModel> getRetailRewardReport(RequestRewardModel  requestRewardModel);
	
	public ResponseEntity<String> createCustomer(RequestCustomerModel requestCustomerModel);
	
	public ResponseEntity<String> createOrder(RequestOrderModel requestOrderModel);
}
