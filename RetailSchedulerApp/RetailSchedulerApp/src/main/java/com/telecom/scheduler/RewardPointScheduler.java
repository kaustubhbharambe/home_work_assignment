package com.telecom.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.telecom.model.RetailOrder;

@Component
public class RewardPointScheduler 
{
	@Autowired
	Environment env;
	
	@Scheduled(cron = "0 0/2 * * * ?")
	//@Scheduled(cron = "0 0 0 1/30 * ?")
	public void scheduleTask()
	{
		System.out.println("Scheduler Running...");
		MongoClient mongo = MongoClients.create(env.getProperty("mongo.db.uri"));
		MongoDatabase database = mongo.getDatabase(env.getProperty("mongo.db.name"));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		int days = Integer.parseInt(env.getProperty("retail.total.rewards.calculate.days"));
		Calendar cal = GregorianCalendar.getInstance();
		Date toDate = cal.getTime();
		System.out.println("Current Date>>>> "+simpleDateFormat.format(toDate));
		cal.add( Calendar.DAY_OF_YEAR, days);
		Date fromDate = cal.getTime();
		System.out.println("Previous Date>>>> "+simpleDateFormat.format(fromDate));
		MongoCollection<Document> retailOrderCollection = database.getCollection(env.getProperty("mongo.db.retail.order.collection"));
		MongoCollection<Document> orderRewardsCollection = database.getCollection(env.getProperty("mongo.db.order.reward.collection"));
		FindIterable<Document> iterDoc = retailOrderCollection.find(new Document("orderDate", new Document("$gte",fromDate).append("$lte", toDate)));
		MongoCursor<Document> dbc = iterDoc.iterator();
		Map<String,List<RetailOrder>> retailMap = new HashMap<String,List<RetailOrder>>();
		Date currentDate;
		String lastUpdatedDate = "";
		double totalRewardsPoints = 0.0;
		Set<String> retailNameList = new HashSet<String>();
		while(dbc.hasNext())
		{
			try 
			{
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(dbc.next().toJson());
				RetailOrder retailOrder = new RetailOrder();
				if(jsonNode.get("retailerName") != null)
				{
					retailOrder.setRetailerName(jsonNode.get("retailerName").toString().replaceAll("\"", ""));
				}
				if(jsonNode.get("rewardPoints") != null)
				{
					retailOrder.setRewardPoints(Double.parseDouble(jsonNode.get("rewardPoints").toString()));
				}
				if(retailMap.get(retailOrder.getRetailerName()) != null)
				{
					List<RetailOrder> retailOrderList = retailMap.get(retailOrder.getRetailerName());
					retailOrderList.add(retailOrder);
					retailMap.put(retailOrder.getRetailerName(), retailOrderList);
				}
				else
				{
					List<RetailOrder> retailOrderList = new ArrayList<RetailOrder>();
					retailOrderList.add(retailOrder);
					retailMap.put(retailOrder.getRetailerName(), retailOrderList);
				}
				retailNameList.add(retailOrder.getRetailerName());
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		currentDate = new Date();
		lastUpdatedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(currentDate);
		System.out.println("map size>>>> "+retailMap.size());
		System.out.println("set size>>>> "+retailNameList.size());
		for(String retailName : retailNameList)
		{
			List<RetailOrder> retailOrderList = retailMap.get(retailName);
			if(retailOrderList.size() == 1)
			{
				totalRewardsPoints = retailOrderList.get(0).getRewardPoints();
			}
			else
			{
				for(RetailOrder retailOrder : retailOrderList)
				{
					totalRewardsPoints = totalRewardsPoints + retailOrder.getRewardPoints();
				}
			}
			BasicDBObject query = new BasicDBObject();
			query.put("retailerName", retailName);
			Document orderRewardDocument = orderRewardsCollection.find(query).first();
			if(orderRewardDocument == null)
			{
				System.out.println("No record found");
				orderRewardDocument = new Document();
				orderRewardDocument.append("_id", ""+System.currentTimeMillis());
				orderRewardDocument.append("retailerName",retailName);
				orderRewardDocument.append("lastUpdatedDate",lastUpdatedDate);
				orderRewardDocument.append("totalRewardPoints",totalRewardsPoints);
				orderRewardsCollection.insertOne(orderRewardDocument);
			}
			else
			{
				DeleteResult deleteResult = orderRewardsCollection.deleteOne(query);
				System.out.println("delete count>>>> "+deleteResult.getDeletedCount());
				orderRewardDocument = new Document();
				orderRewardDocument.append("_id", ""+System.currentTimeMillis());
				orderRewardDocument.append("retailerName",retailName);
				orderRewardDocument.append("lastUpdatedDate",lastUpdatedDate);
				orderRewardDocument.append("totalRewardPoints",totalRewardsPoints);
				orderRewardsCollection.insertOne(orderRewardDocument);
			}
		}
	
	}

}
