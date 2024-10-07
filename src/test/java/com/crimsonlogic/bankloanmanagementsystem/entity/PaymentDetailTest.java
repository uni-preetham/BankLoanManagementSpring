package com.crimsonlogic.bankloanmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentDetailTest {

    private PaymentDetail paymentDetail;

    @BeforeEach
    void setUp() {
        // Initialize a PaymentDetail object before each test case
        paymentDetail = new PaymentDetail();
    }

    @Test
    void testGenerateId() {
        // Verify that the ID is generated before persisting
        paymentDetail.generateId();
        assertNotNull(paymentDetail.getPaymentId());
        assertTrue(paymentDetail.getPaymentId().startsWith("PAY"));
    }

    @Test
    void testPaymentDetailConstructor() {
        // Create an instance of PaymentDetail using the constructor
        PaymentDetail paymentDetail = new PaymentDetail(1, 1000.0, 500.0, 200.0, 300.0);
        
        // Validate the properties of the paymentDetail object
        assertEquals(1, paymentDetail.getMonth());
        assertEquals(1000.0, paymentDetail.getEmi());
        assertEquals(500.0, paymentDetail.getPrincipalPayment());
        assertEquals(200.0, paymentDetail.getInterestPayment());
        assertEquals(300.0, paymentDetail.getOutstandingPrincipal());
    }

    @Test
    void testSettersAndGetters() {
        // Set values using setters
        paymentDetail.setMonth(2);
        paymentDetail.setEmi(1200.0);
        paymentDetail.setPrincipalPayment(600.0);
        paymentDetail.setInterestPayment(250.0);
        paymentDetail.setOutstandingPrincipal(350.0);
        
        // Validate values using getters
        assertEquals(2, paymentDetail.getMonth());
        assertEquals(1200.0, paymentDetail.getEmi());
        assertEquals(600.0, paymentDetail.getPrincipalPayment());
        assertEquals(250.0, paymentDetail.getInterestPayment());
        assertEquals(350.0, paymentDetail.getOutstandingPrincipal());
    }

   
}
