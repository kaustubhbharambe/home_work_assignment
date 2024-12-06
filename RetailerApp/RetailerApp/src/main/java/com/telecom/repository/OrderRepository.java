package com.telecom.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.telecom.model.OrderEntity;

public interface OrderRepository extends MongoRepository<OrderEntity,String> 
{

}
