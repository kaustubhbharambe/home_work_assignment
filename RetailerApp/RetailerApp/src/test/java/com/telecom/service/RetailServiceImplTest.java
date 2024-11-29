package com.telecom.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.telecom.model.RequestRetailModel;
import com.telecom.model.RetailOrder;
import com.telecom.repository.RetailOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class RetailServiceImplTest {

    @Mock
    private RetailOrderRepository retailOrderRepository;

    @Mock
    private Environment env;

    @InjectMocks
    private RetailServiceImpl retailService;

    private RequestRetailModel requestRetailModel;

    @BeforeEach
    void setUp() {
        requestRetailModel = new RequestRetailModel();
    }

    @Test
    void testSaveRetailOrderRewardPoints_ValidRequest_AmountGreaterThan100() 
    {
        requestRetailModel.setAmount("200");
        requestRetailModel.setRetailerName("Retailer A");
        when(env.getProperty("retail.order.reward1.value")).thenReturn("10");
        RetailOrder retailOrder = new RetailOrder();
        retailOrder.setId("12345");
        retailOrder.setAmount(200.0);
        retailOrder.setRetailerName("Retailer A");
        retailOrder.setOrderDate(new Date());
        retailOrder.setRewardPoints(450.0);
        retailOrder.setMode("ONLINE");
        when(retailOrderRepository.save(any(RetailOrder.class))).thenReturn(retailOrder);
        ResponseEntity<RetailOrder> response = retailService.saveRetailOrderRewardPoints(requestRetailModel);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(90.0, response.getBody().getRewardPoints());
    }

    @Test
    void testSaveRetailOrderRewardPoints_ValidRequest_AmountBetween50And100() 
    {
        requestRetailModel.setAmount("75");
        requestRetailModel.setRetailerName("Retailer B");
        when(env.getProperty("retail.order.reward1.value")).thenReturn("10");
        RetailOrder retailOrder = new RetailOrder();
        retailOrder.setId("12346");
        retailOrder.setAmount(75.0);
        retailOrder.setRetailerName("Retailer B");
        retailOrder.setOrderDate(new Date());
        retailOrder.setRewardPoints(75.0);
        retailOrder.setMode("ONLINE");
        when(retailOrderRepository.save(any(RetailOrder.class))).thenReturn(retailOrder);
        ResponseEntity<RetailOrder> response = retailService.saveRetailOrderRewardPoints(requestRetailModel);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(75.0, response.getBody().getRewardPoints());
    }

    @Test
    void testSaveRetailOrderRewardPoints_InvalidRequest_MissingAmount() 
    {
        requestRetailModel.setAmount(null);
        requestRetailModel.setRetailerName("Retailer C");
        ResponseEntity<RetailOrder> response = retailService.saveRetailOrderRewardPoints(requestRetailModel);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody().getRewardPoints());
    }

    @Test
    void testSaveRetailOrderRewardPoints_InvalidRequest_MissingRetailerName() 
    {
        requestRetailModel.setAmount("50");
        requestRetailModel.setRetailerName(null);
        ResponseEntity<RetailOrder> response = retailService.saveRetailOrderRewardPoints(requestRetailModel);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody().getRewardPoints());
    }

    @Test
    void testSaveRetailOrderRewardPoints_AmountLessThan50() 
    {
        requestRetailModel.setAmount("30");
        requestRetailModel.setRetailerName("Retailer D");
        RetailOrder retailOrder = new RetailOrder();
        retailOrder.setId("12347");
        retailOrder.setAmount(30.0);
        retailOrder.setRetailerName("Retailer D");
        retailOrder.setOrderDate(new Date());
        retailOrder.setRewardPoints(30.0);
        retailOrder.setMode("ONLINE");
        when(retailOrderRepository.save(any(RetailOrder.class))).thenReturn(retailOrder);
        ResponseEntity<RetailOrder> response = retailService.saveRetailOrderRewardPoints(requestRetailModel);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0.0,response.getBody().getRewardPoints());
    }
    
}
