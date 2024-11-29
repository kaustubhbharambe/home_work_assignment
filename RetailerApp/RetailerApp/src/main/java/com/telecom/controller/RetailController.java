package com.telecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.telecom.model.RequestRetailModel;
import com.telecom.model.RetailOrder;
import com.telecom.service.RetailService;

@RestController
public class RetailController 
{

	@Autowired
	RetailService retailService; 
	
	@RequestMapping(value = "/saveRetailReward", method = RequestMethod.POST)
	public ResponseEntity<RetailOrder> saveRetailReward(@RequestBody RequestRetailModel requestRetailModel)
	{
		ResponseEntity<RetailOrder> responseEntity = retailService.saveRetailOrderRewardPoints(requestRetailModel);
		return responseEntity;
	}

}
