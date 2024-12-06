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
import com.telecom.model.RequestOrderModel;
import com.telecom.repository.OrderRepository;

class CreateOrderTest 
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
    private OrderRepository orderRepository;

    @InjectMocks
    private RetailServiceImpl retailService;

    @Test
    void testCreateOrder_Success() throws Exception {
        RequestOrderModel requestOrderModel = new RequestOrderModel();
        requestOrderModel.setCustomerName("John Doe");
        requestOrderModel.setOrderDate("01-01-2023 12:00:00");
        requestOrderModel.setAmount("100.0");

        when(env.getProperty("spring.data.mongodb.uri")).thenReturn("mongodb://localhost");
        when(mongoClient.getDatabase(anyString())).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection(anyString())).thenReturn(customerCollection);
        when(orderRepository.save(any())).thenReturn(null); // Simulate successful save

        ResponseEntity<String> response = retailService.createOrder(requestOrderModel);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Order Created Successfully.", response.getBody());
    }

    @Test
    void testCreateOrder_CustomerNotFound() {
        RequestOrderModel requestOrderModel = new RequestOrderModel();
        requestOrderModel.setCustomerName("NonExistingCustomer");
        requestOrderModel.setOrderDate("01-01-2023 12:00:00");
        requestOrderModel.setAmount("100.0");

        when(env.getProperty("spring.data.mongodb.uri")).thenReturn("mongodb://localhost");
        when(mongoClient.getDatabase(anyString())).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection(anyString())).thenReturn(customerCollection);

        ResponseEntity<String> response = retailService.createOrder(requestOrderModel);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("This NonExistingCustomer is not present in table.", response.getBody());
    }
}
