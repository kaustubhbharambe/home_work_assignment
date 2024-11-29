package com.telecom.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.telecom.model.RetailOrder;

public interface RetailOrderRepository extends MongoRepository<RetailOrder,String>
{

}
