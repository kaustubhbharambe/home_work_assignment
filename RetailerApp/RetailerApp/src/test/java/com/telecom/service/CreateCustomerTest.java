package com.telecom.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.bson.Document;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.telecom.model.RequestCustomerModel;
import com.telecom.repository.CustomerRepository;

class CreateCustomerTest 
{
    @Mock
    private Environment env;

    @Mock
    private MongoClient mongoClient;

    @Mock
    private MongoDatabase mongoDatabase;

    @Mock
    private MongoCollection<Document> customerCollection;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RetailServiceImpl retailService;

    @Test
    void testCreateCustomer_Success() throws Exception {
        RequestCustomerModel requestCustomerModel = new RequestCustomerModel();
        requestCustomerModel.setCustomerName("John Doe");
        requestCustomerModel.setDate("01-01-2023 12:00:00");

        when(env.getProperty("spring.data.mongodb.uri")).thenReturn("mongodb://localhost");
        when(mongoClient.getDatabase(anyString())).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection(anyString())).thenReturn(customerCollection);
        when(customerRepository.save(any())).thenReturn(null); // Simulate successful save

        ResponseEntity<String> response = retailService.createCustomer(requestCustomerModel);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Customer Created Successfully.", response.getBody());
    }

    @Test
    void testCreateCustomer_CustomerExists() {
        RequestCustomerModel requestCustomerModel = new RequestCustomerModel();
        requestCustomerModel.setCustomerName("John Doe");
        requestCustomerModel.setDate("01-01-2023 12:00:00");

        when(env.getProperty("spring.data.mongodb.uri")).thenReturn("mongodb://localhost");
        when(mongoClient.getDatabase(anyString())).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection(anyString())).thenReturn(customerCollection);

        ResponseEntity<String> response = retailService.createCustomer(requestCustomerModel);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Customer name is already present. Please use different name.", response.getBody());
    }
}
