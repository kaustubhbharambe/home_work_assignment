package com.telecom.controller;

import com.telecom.model.RequestCustomerModel;
import com.telecom.model.RequestOrderModel;
import com.telecom.model.RequestRewardModel;
import com.telecom.model.ResponseCustomerModel;
import com.telecom.service.RetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class RetailControllerTest
{
    @InjectMocks
    private RetailController retailController;

    @Mock
    private RetailService retailService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        // Initialize mocks before each test if necessary
    }

    @Test
    public void testGetRetailRewardReport_ValidRequest() {
        // Arrange
        RequestRewardModel requestRewardModel = new RequestRewardModel();
        requestRewardModel.setCustomerName("John Doe");
        ResponseCustomerModel responseCustomerModel = new ResponseCustomerModel();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(retailService.getRetailRewardReport(requestRewardModel)).thenReturn(ResponseEntity.ok(responseCustomerModel));

        // Act
        ResponseEntity<ResponseCustomerModel> responseEntity = retailController.getRetailRewardReport(requestRewardModel, bindingResult);

        // Assert
        assertEquals("Response should be OK", 200, responseEntity.getStatusCodeValue());
        verify(retailService).getRetailRewardReport(requestRewardModel);
    }

    @Test
    public void testGetRetailRewardReport_InvalidRequest() {
        // Arrange
        RequestRewardModel requestRewardModel = new RequestRewardModel();
        requestRewardModel.setCustomerName("John Doe");

        FieldError fieldError = new FieldError("requestRewardModel", "customerName", "Customer name is required.");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));

        // Act
        ResponseEntity<ResponseCustomerModel> responseEntity = retailController.getRetailRewardReport(requestRewardModel, bindingResult);

        // Assert
        assertEquals("Response should be BadRequest", 400, responseEntity.getStatusCodeValue());
        verify(retailService, never()).getRetailRewardReport(any());
    }

    @Test
    public void testCreateCustomer_ValidRequest() {
        // Arrange
        RequestCustomerModel requestCustomerModel = new RequestCustomerModel();
        requestCustomerModel.setCustomerName("Jane Doe");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(retailService.createCustomer(requestCustomerModel)).thenReturn(ResponseEntity.ok("Customer Created Successfully."));

        // Act
        ResponseEntity<String> responseEntity = retailController.createCustomer(requestCustomerModel, bindingResult);

        // Assert
        assertEquals("Response should be OK", 200, responseEntity.getStatusCodeValue());
        verify(retailService).createCustomer(requestCustomerModel);
    }

    @Test
    public void testCreateCustomer_InvalidRequest() {
        // Arrange
        RequestCustomerModel requestCustomerModel = new RequestCustomerModel();
        requestCustomerModel.setCustomerName("Jane Doe");
        FieldError fieldError = new FieldError("requestCustomerModel", "customerName", "Customer name is required.");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));

        // Act
        ResponseEntity<String> responseEntity = retailController.createCustomer(requestCustomerModel, bindingResult);

        // Assert
        assertEquals("Response should be BadRequest", 400, responseEntity.getStatusCodeValue());
        verify(retailService, never()).createCustomer(any());
    }

    @Test
    public void testCreateOrder_ValidRequest() {
        // Arrange
        RequestOrderModel requestOrderModel = new RequestOrderModel();
        requestOrderModel.setCustomerName("Jane Doe");
        requestOrderModel.setAmount("100");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(retailService.createOrder(requestOrderModel)).thenReturn(ResponseEntity.ok("Order Created Successfully."));

        // Act
        ResponseEntity<String> responseEntity = retailController.createOrder(requestOrderModel, bindingResult);

        // Assert
        assertEquals("Response should be OK", 200, responseEntity.getStatusCodeValue());
        verify(retailService).createOrder(requestOrderModel);
    }

    @Test
    public void testCreateOrder_InvalidRequest() {
        // Arrange
        RequestOrderModel requestOrderModel = new RequestOrderModel();
        requestOrderModel.setCustomerName("Jane Doe");
        requestOrderModel.setAmount("100");
        FieldError fieldError = new FieldError("requestOrderModel", "amount", "Amount is required.");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));

        // Act
        ResponseEntity<String> responseEntity = retailController.createOrder(requestOrderModel, bindingResult);

        // Assert
        assertEquals("Response should be BadRequest", 400, responseEntity.getStatusCodeValue());
        verify(retailService, never()).createOrder(any());
    }
}
