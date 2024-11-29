package com.telecom.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@ComponentScan("com.telecom.scheduler")
public class RetailSchedulerApp implements CommandLineRunner
{
	public static void main(String[] args) {
		SpringApplication.run(RetailSchedulerApp.class, args);
	}

	@Override
	public void run(String... args) throws Exception 
	{
		System.out.println("Standalone Spring boot Application");
	}
	
}
