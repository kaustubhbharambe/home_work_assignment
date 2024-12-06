package com.telecom.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.telecom.model.RequestCustomerModel;
import com.telecom.model.RequestOrderModel;
import com.telecom.model.RequestRewardModel;
import com.telecom.model.ResponseCustomerModel;
import com.telecom.service.RetailService;

import jakarta.validation.Valid;

@RestController
public class RetailController 
{

	private static final Logger logger = LoggerFactory.getLogger(RetailController.class);
	
	@Autowired
	RetailService retailService; 
	
	@RequestMapping(value = "/getRewardReport", method = RequestMethod.GET)
	public ResponseEntity<ResponseCustomerModel> getRetailRewardReport(@RequestBody @Valid RequestRewardModel requestRewardModel,BindingResult bindingResult)
	{
		ResponseEntity<ResponseCustomerModel> responseEntity;
		logger.info("start getRewardReport controller");
		if (bindingResult.hasErrors()) 
		{
			logger.info("in getRewardReport validation");
			List<FieldError> errors = bindingResult.getFieldErrors();
			ResponseCustomerModel responseCustomerModel = new ResponseCustomerModel();
			logger.info("errors size :::: "+errors.size());
			for(FieldError error : errors)
			{
				responseCustomerModel.getErrors().add(error.getDefaultMessage());
			}
			responseEntity = ResponseEntity.badRequest().body(responseCustomerModel);
        }
		else
		{
			responseEntity = retailService.getRetailRewardReport(requestRewardModel);
		}
		logger.info("end getRewardReport controller");
		return responseEntity;
	}
	
	@RequestMapping(value = "/createCustomer", method = RequestMethod.POST)
	public ResponseEntity<String> createCustomer(@RequestBody @Valid RequestCustomerModel requestCustomerModel,BindingResult bindingResult)
	{
		logger.info("start createCustomer controller");
		ResponseEntity<String> responseEntity;
		if (bindingResult.hasErrors()) 
		{
			logger.info("in createCustomer validation");
			List<FieldError> errors = bindingResult.getFieldErrors();
			logger.info("errors size :::: "+errors.size());
			String validateMsg = "";
			for(FieldError error : errors)
			{
				validateMsg = validateMsg + error.getDefaultMessage() + "\n";
			}
			responseEntity = ResponseEntity.badRequest().body(validateMsg);
		}
		else
		{
			responseEntity = retailService.createCustomer(requestCustomerModel);
		}
		logger.info("end createCustomer controller");
		return responseEntity;
	}
	
	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
	public ResponseEntity<String> createOrder(@RequestBody @Valid RequestOrderModel requestOrderModel ,BindingResult bindingResult)
	{
		logger.info("start createOrder controller");
		ResponseEntity<String> responseEntity;
		if (bindingResult.hasErrors()) 
		{
			logger.info("in createOrder validation");
			List<FieldError> errors = bindingResult.getFieldErrors();
			logger.info("errors size :::: "+errors.size());
			String validateMsg = "";
			for(FieldError error : errors)
			{
				validateMsg = validateMsg + error.getDefaultMessage() + "\n";
			}
			responseEntity = ResponseEntity.badRequest().body(validateMsg);
		}
		else
		{
			responseEntity = retailService.createOrder(requestOrderModel);
		}
		logger.info("end createOrder controller");
		return responseEntity;
	}

}
