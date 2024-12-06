package com.telecom.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.telecom.model.CustomerEntity;


public interface CustomerRepository extends MongoRepository<CustomerEntity,String> 
{

}
