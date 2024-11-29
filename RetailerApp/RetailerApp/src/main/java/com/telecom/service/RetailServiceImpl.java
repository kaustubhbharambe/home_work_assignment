package com.telecom.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.telecom.model.RequestRetailModel;
import com.telecom.model.RetailOrder;
import com.telecom.repository.RetailOrderRepository;

@Service
public class RetailServiceImpl implements RetailService{

	@Autowired
	RetailOrderRepository retailOrderRepository;
	
	 @Autowired
	 private Environment env;
	
	@Override
	public ResponseEntity<RetailOrder> saveRetailOrderRewardPoints(RequestRetailModel requestRetailModel) throws NumberFormatException {
		Double amount = 0.0;
		int divisor = 0;
		int reminder = 0;
		int reward1 = 0;
		Double rewardPoints = 0.0;
		ResponseEntity<RetailOrder> retailOrderEntity;
		Date currentDate;
		if((requestRetailModel.getAmount() != null && !requestRetailModel.getAmount().equals("")) && ((requestRetailModel.getRetailerName() != null && !requestRetailModel.getRetailerName().equals(""))))
		{
			RetailOrder retailOrder = new RetailOrder();
			retailOrder.setId(""+System.currentTimeMillis());
			retailOrder.setAmount(Double.parseDouble(requestRetailModel.getAmount()));
			retailOrder.setRetailerName(requestRetailModel.getRetailerName());
			currentDate = new Date();
			retailOrder.setOrderDate(currentDate);
			amount = Double.parseDouble(requestRetailModel.getAmount());
			if(env.getProperty("retail.order.reward1.value") != null)
			{
				reward1 = Integer.valueOf(env.getProperty("retail.order.reward1.value"));
			}
			if(amount >= 100.0)
			{
				divisor = (int)(amount/reward1);
				System.out.println("divisor>>> "+divisor);
				reminder = (int)(amount%reward1);
				System.out.println("reminder>>> "+reminder);
				if(reminder != 0.0)
				{
					divisor = divisor * 2;
					rewardPoints = Double.valueOf((divisor * reminder));
				}
				else
				{
					divisor = divisor * 2;
					rewardPoints = Double.valueOf(divisor);
				}
				rewardPoints = rewardPoints + 50;
			}
			else if(amount >=50 && amount <= 100)
			{
				rewardPoints = amount;
			}
			System.out.println("rewardPoints>>>>> "+rewardPoints);
			retailOrder.setRewardPoints(rewardPoints);
			retailOrder.setMode("ONLINE");
			retailOrderRepository.save(retailOrder);
			retailOrderEntity = new ResponseEntity<RetailOrder>(retailOrder,HttpStatus.OK);
		}
		else
		{
			RetailOrder retailOrder = new RetailOrder();
			retailOrderEntity = new ResponseEntity<RetailOrder>(retailOrder,HttpStatus.BAD_REQUEST);
		}
		return retailOrderEntity;
	}


}
