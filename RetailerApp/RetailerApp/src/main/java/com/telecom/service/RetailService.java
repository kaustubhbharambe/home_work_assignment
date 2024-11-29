package com.telecom.service;

import org.springframework.http.ResponseEntity;
import com.telecom.model.RequestRetailModel;
import com.telecom.model.RetailOrder;

public interface RetailService 
{
	public ResponseEntity<RetailOrder> saveRetailOrderRewardPoints(RequestRetailModel requestRetailModel);
}
