package com.telecom.service;

import static org.mockito.Mockito.*;

import org.bson.Document;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.telecom.model.*;
import com.telecom.repository.CustomerRepository;
import com.telecom.repository.OrderRepository;

class RetailServiceImplTest 
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
    private MongoCollection<Document> orderCollection;
    
    @Mock
    private MongoCursor<Document> mongoCursor;
    
    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private RetailServiceImpl retailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRetailRewardReport_ValidCustomerAndOrderData() throws Exception {
        RequestRewardModel requestRewardModel = new RequestRewardModel();
        requestRewardModel.setCustomerName("John Doe");
        requestRewardModel.setTotalMonths("6");

        when(env.getProperty("spring.data.mongodb.uri")).thenReturn("mongodb://localhost");
        when(env.getProperty("spring.data.mongodb.database")).thenReturn("testDB");
        when(env.getProperty("spring.data.mongodb.collection.customer")).thenReturn("customer");
        when(env.getProperty("spring.data.mongodb.collection.order")).thenReturn("order");
        when(mongoClient.getDatabase(anyString())).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection(anyString())).thenReturn(customerCollection).thenReturn(orderCollection);

        // Mock the order documents
        Document orderDoc = mock(Document.class);
        when(mongoCursor.hasNext()).thenReturn(true, false);
        when(mongoCursor.next()).thenReturn(orderDoc);

        // Set up the order details
        when(orderDoc.getString("customerName")).thenReturn("John Doe");
        when(orderDoc.getString("amount")).thenReturn("50.0");
        when(orderDoc.getString("order_date")).thenReturn("2024-12-01T10:00:00Z");

        // Call the service method
        ResponseEntity<ResponseCustomerModel> response = retailService.getRetailRewardReport(requestRewardModel);

        // Verify the results
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getCustomerOrder().getTotalRewardPoints().length() > 0);
    }

    @Test
    void testGetRetailRewardReport_CustomerNotFound() {
        RequestRewardModel requestRewardModel = new RequestRewardModel();
        requestRewardModel.setCustomerName("NonExistingCustomer");
        requestRewardModel.setTotalMonths("6");

        when(env.getProperty("spring.data.mongodb.uri")).thenReturn("mongodb://localhost");
        when(mongoClient.getDatabase(anyString())).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection(anyString())).thenReturn(customerCollection);

        ResponseEntity<ResponseCustomerModel> response = retailService.getRetailRewardReport(requestRewardModel);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().getErrors().contains("This NonExistingCustomer is not present in table."));
    }
}
