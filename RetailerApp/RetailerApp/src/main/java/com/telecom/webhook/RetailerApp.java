package com.telecom.webhook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@ComponentScan("com.telecom.service")
@ComponentScan("com.telecom.controller")
@ComponentScan("com.telecom.model")
@ComponentScan("com.telecom.config")
@EnableMongoRepositories("com.telecom.repository")
@EnableAutoConfiguration
public class RetailerApp
{
	public static void main(String[] args) 
	{
		SpringApplication.run(RetailerApp.class, args);
	}
	
}
