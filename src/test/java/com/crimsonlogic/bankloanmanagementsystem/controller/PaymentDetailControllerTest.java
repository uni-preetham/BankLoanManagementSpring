package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.entity.PaymentDetail;
import com.crimsonlogic.bankloanmanagementsystem.service.PaymentDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class PaymentDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PaymentDetailService paymentDetailService;

    @InjectMocks
    private PaymentDetailController paymentDetailController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentDetailController).build();
    }

    @Test
    void testGetAllPayments() throws Exception {
        // Mock data
        PaymentDetail payment1 = new PaymentDetail(1, 1000.0, 200.0, 50.0, 800.0);
        PaymentDetail payment2 = new PaymentDetail(2, 1000.0, 200.0, 50.0, 600.0);
        List<PaymentDetail> paymentList = Arrays.asList(payment1, payment2);

        // Mock the service
        when(paymentDetailService.getAllPayments()).thenReturn(paymentList);

        // Perform the GET request
        mockMvc.perform(get("/api/payments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].month").value(1))
                .andExpect(jsonPath("$[1].month").value(2));
    }

    @Test
    void testSavePayment() throws Exception {
        // Mock data
        PaymentDetail paymentDetail = new PaymentDetail(1, 1000.0, 200.0, 50.0, 800.0);
        when(paymentDetailService.savePayment(any(PaymentDetail.class))).thenReturn(paymentDetail);

        // Perform the POST request
        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"month\":1,\"emi\":1000.0,\"principalPayment\":200.0,\"interestPayment\":50.0,\"outstandingPrincipal\":800.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.month").value(1))
                .andExpect(jsonPath("$.emi").value(1000.0));
    }
}
