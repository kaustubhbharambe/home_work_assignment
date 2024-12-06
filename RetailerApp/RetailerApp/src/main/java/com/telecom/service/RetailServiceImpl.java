package com.telecom.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.telecom.model.CustomerEntity;
import com.telecom.model.CustomerOrder;
import com.telecom.model.Order;
import com.telecom.model.OrderEntity;
import com.telecom.model.RequestCustomerModel;
import com.telecom.model.RequestOrderModel;
import com.telecom.model.RequestRewardModel;
import com.telecom.model.ResponseCustomerModel;
import com.telecom.repository.CustomerRepository;
import com.telecom.repository.OrderRepository;

@Service
public class RetailServiceImpl implements RetailService
{
	private static final Logger logger = LoggerFactory.getLogger(RetailServiceImpl.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	OrderRepository orderRepository;

	@Override
	public ResponseEntity<ResponseCustomerModel> getRetailRewardReport(RequestRewardModel requestRewardModel) 
	{
		ResponseEntity<ResponseCustomerModel> responseEntity;
		MongoClient mongo = null;
		MongoDatabase database = null;
		MongoCollection<Document> customerCollection = null;
		long countCustomer = 0;
		int days = 0;
		Calendar cal = null;
		SimpleDateFormat simpleDateFormat = null;
		Date toDate = null;
		Date fromDate = null;
		MongoCollection<Document> orderCollection = null;
		FindIterable<Document> orderDocs = null;
		MongoCursor<Document> mongoCursour = null;
		Query orderQuery = null;
		Double amount = 0.0;
		int divisor = 0;
		int reminder = 0;
		int reward1 = 0;
		Double totalAmount = 0.0;
		Double rewardPoints = 0.0;
		List<Order> orders = new ArrayList<Order>();
		CustomerOrder customerOrder = new CustomerOrder();
		ResponseCustomerModel responseCustomerModel = new ResponseCustomerModel();
		try
		{
			logger.info("start getRetailRewardReport service impl");
			if(env.getProperty("retail.order.reward1.value") != null)
			{
				reward1 = Integer.valueOf(env.getProperty("retail.order.reward1.value"));
			}
			//mongo db database connection code
	 		mongo = MongoClients.create(env.getProperty("spring.data.mongodb.uri"));
			database = mongo.getDatabase(env.getProperty("spring.data.mongodb.database"));
			customerCollection = database.getCollection(env.getProperty("spring.data.mongodb.collection.customer"));
			countCustomer = customerCollection.countDocuments((new Document("customerName",requestRewardModel.getCustomerName())));
			logger.info("countCustomer>>>>> "+countCustomer);
			//Checking that customer is present or not
			if(countCustomer >= 1)
			{
				if(requestRewardModel.getTotalMonths().matches(".*\\d.*"))			
				{
					days = Integer.parseInt(requestRewardModel.getTotalMonths());
					if(days < 0)
					{
						responseCustomerModel.getErrors().add("Negative total months is not valid to generate reward report.");
						responseEntity = ResponseEntity.badRequest().body(responseCustomerModel);
					}
					else
					{
						days = Integer.parseInt(requestRewardModel.getTotalMonths()) * 30; //multiplying 30 into total Months.
						cal = GregorianCalendar.getInstance();
						simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
						toDate = cal.getTime();
						logger.info("Current Date>>>> "+simpleDateFormat.format(toDate));
						cal.add( Calendar.DAY_OF_YEAR, -days);
						fromDate = cal.getTime();
						logger.info("Previous Date>>>> "+simpleDateFormat.format(fromDate));
						//Querying to order table to get data
						orderCollection = database.getCollection(env.getProperty("spring.data.mongodb.collection.order"));
						orderQuery = new Query();
						orderQuery.addCriteria(Criteria.where("customerName").is(requestRewardModel.getCustomerName()));
						orderQuery.addCriteria(Criteria.where("order_date").gte(fromDate).lte(toDate));
						orderDocs = orderCollection.find(new Document("order_date", new Document("$gte",fromDate).append("$lte", toDate)));
						mongoCursour = orderDocs.iterator();
						logger.info("condition>>> "+(mongoCursour.hasNext()));
						//Fetching data from mongo db and added in orders list.
						while(mongoCursour.hasNext())
						{
							ObjectMapper mapper = new ObjectMapper();
							JsonNode jsonNode = mapper.readTree(mongoCursour.next().toJson());
							if(jsonNode.get("customerName") != null)
							{
								if(jsonNode.get("customerName").toString().replaceAll("\"", "").equals(requestRewardModel.getCustomerName()))
								{
									logger.info("customername>>>> "+jsonNode.get("customerName").toString());
									logger.info("amount>>>> "+jsonNode.get("amount").toString());
									logger.info("date>>>> "+jsonNode.get("order_date").toString().replaceAll("\"", ""));
									Order order = new Order();
									order.setOrderDate(getOderDate(jsonNode.get("order_date").toString().replaceAll("\"", "")));
									order.setAmount(jsonNode.get("amount").toString());
									amount = Double.parseDouble(jsonNode.get("amount").toString());
									totalAmount = totalAmount + amount;
									orders.add(order);
								}
							}
						}
					}
				}
				else
				{
					//validation if total months is not number.
					responseCustomerModel.getErrors().add("Please enter number in total months.");
					responseEntity = ResponseEntity.badRequest().body(responseCustomerModel);
				}
				logger.info("totalAmount>>>>> "+totalAmount);
				if(totalAmount != 0.0)
				{
					if(totalAmount >= 100.0)
					{
						divisor = (int)(totalAmount/reward1);
						logger.info("divisor>>> "+divisor);
						reminder = (int)(totalAmount%reward1);
						logger.info("reminder>>> "+reminder);
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
					else if(totalAmount >=50 && totalAmount <= 100)
					{
						rewardPoints = totalAmount;
					}
					logger.info("rewards points>>>>> "+rewardPoints);
				}
				customerOrder.setCustomerName(requestRewardModel.getCustomerName());
				customerOrder.setTotalAmount(""+totalAmount);
				customerOrder.setTotalRewardPoints(""+rewardPoints);
				customerOrder.setOrders(orders);
				responseCustomerModel.setCustomerOrder(customerOrder);
				responseEntity = ResponseEntity.ok(responseCustomerModel);
			}
			else
			{
				//validation if customer is not present in database.
				responseCustomerModel.getErrors().add("This "+ requestRewardModel.getCustomerName() +" is not present in table.");
				responseEntity = ResponseEntity.badRequest().body(responseCustomerModel);
			}
		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
			responseCustomerModel.getErrors().add("Internal Server Error.");
			responseEntity = ResponseEntity.internalServerError().body(responseCustomerModel);
		}
		return responseEntity;
	}
	
	public String getOderDate(String dateStr)
	{
		 String datePart = dateStr.substring(7, dateStr.length() - 1);
		 DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		 TemporalAccessor temporal = formatter.parse(datePart);
		 Instant instant = Instant.from(temporal);
		 System.out.println("Parsed Date and Time: " + instant.atOffset(ZoneOffset.UTC));
		 return ""+instant.atOffset(ZoneOffset.UTC);
	}

	@Override
	public ResponseEntity<String> createCustomer(RequestCustomerModel requestCustomerModel) 
	{
		SimpleDateFormat simpleDateFormat = null;
		ResponseEntity<String> responseEntity;
		Date createdDate = null;
		CustomerEntity customerEntity;
		Date currentDate = null;
		MongoClient mongo = null;
		MongoDatabase database = null;
		MongoCollection<Document> customerCollection = null;
		long countCustomer = 0;
		try
		{
			//checking data is present proper format or not
			simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			createdDate = simpleDateFormat.parse(requestCustomerModel.getDate());
			
		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
			responseEntity = ResponseEntity.badRequest().body("Please enter date format in dd-MM-yyyy HH:mm:ss");
			return responseEntity;
		}
		currentDate = new Date();
		if(createdDate.getTime() > currentDate.getTime())
		{
			//validation if created date is not greater than current date
			responseEntity = ResponseEntity.badRequest().body("Future Date is not possible for customer creation.");
			return responseEntity;
		}
		mongo = MongoClients.create(env.getProperty("spring.data.mongodb.uri"));
		database = mongo.getDatabase(env.getProperty("spring.data.mongodb.database"));
		customerCollection = database.getCollection(env.getProperty("spring.data.mongodb.collection.customer"));
		countCustomer = customerCollection.countDocuments((new Document("customerName",requestCustomerModel.getCustomerName())));
		logger.info("countCustomer>>>>> "+countCustomer);
		if(countCustomer >= 1)
		{
			responseEntity = ResponseEntity.badRequest().body("Customer name is already present. Please use different name.");
			return responseEntity;
		}
		try
		{
			//customer creation code
			customerEntity = new  CustomerEntity();
			customerEntity.setId(""+System.currentTimeMillis());
			customerEntity.setCustomerName(requestCustomerModel.getCustomerName());
			customerEntity.setCreated_date(createdDate);
			customerRepository.save(customerEntity);
			responseEntity = ResponseEntity.ok("Customer Created Successfully.");
		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
			responseEntity = ResponseEntity.internalServerError().body("Internal server error.");
			return responseEntity;
		}
		return responseEntity;
	}

	@Override
	public ResponseEntity<String> createOrder(RequestOrderModel requestOrderModel) 
	{
		SimpleDateFormat simpleDateFormat = null;
		ResponseEntity<String> responseEntity;
		Date orderDate = null;
		Date currentDate = null;
		Double amount = 0.0;
		MongoClient mongo = null;
		MongoDatabase database = null;
		MongoCollection<Document> customerCollection = null;
		long countCustomer = 0;
		
		//mongo db database connection code
		mongo = MongoClients.create(env.getProperty("spring.data.mongodb.uri"));
		database = mongo.getDatabase(env.getProperty("spring.data.mongodb.database"));
		customerCollection = database.getCollection(env.getProperty("spring.data.mongodb.collection.customer"));
		countCustomer = customerCollection.countDocuments((new Document("customerName",requestOrderModel.getCustomerName())));
		logger.info("countCustomer>>>>> "+countCustomer);
		if(countCustomer >= 1)
		{
			try
			{
				//checking data is present proper format or not
				simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				orderDate = simpleDateFormat.parse(requestOrderModel.getOrderDate());
			}
			catch(Exception e)
			{
				logger.info(e.getMessage());
				responseEntity = ResponseEntity.badRequest().body("Please enter date format in dd-MM-yyyy HH:mm:ss");
				return responseEntity;
			}
			currentDate = new Date();
			if(orderDate.getTime() > currentDate.getTime())
			{
				//validation if order date is not greater than current date
				responseEntity = ResponseEntity.badRequest().body("Future Date is not possible for order creation.");
				return responseEntity;
			}
			try
			{
				amount = Double.parseDouble(requestOrderModel.getAmount());
				if(amount < 0.0)
				{
					//validation for amount is not less than zero.
					responseEntity = ResponseEntity.badRequest().body("Negative amount is not valid for order creation.");
					return responseEntity;
				}
			}
			catch(Exception e)
			{
				logger.info(e.getMessage());
				responseEntity = ResponseEntity.internalServerError().body("Please enter valid amount value");
				return responseEntity;
			}
			try
			{
				//order creation code.
				OrderEntity orderEntity = new OrderEntity();
				orderEntity.setId(""+System.currentTimeMillis());
				orderEntity.setOrder_date(orderDate);
				orderEntity.setCustomerName(requestOrderModel.getCustomerName());
				orderEntity.setAmount(amount);
				orderRepository.save(orderEntity);
				responseEntity = ResponseEntity.ok("Order Created Successfully.");
			}
			catch(Exception e)
			{
				logger.info(e.getMessage());
				responseEntity = ResponseEntity.internalServerError().body("Internal server error.");
				return responseEntity;
			}
		}
		else
		{
			//validation if customer is not present in database.
			responseEntity = ResponseEntity.badRequest().body("This "+ requestOrderModel.getCustomerName() +" is not present in table.");
		}
		return responseEntity;
	}
}
