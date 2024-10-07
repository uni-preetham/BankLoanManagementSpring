package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.entity.PaymentDetail;
import com.crimsonlogic.bankloanmanagementsystem.service.PaymentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
@RestController
@RequestMapping("/api/payments")
public class PaymentDetailController {

    @Autowired
    private PaymentDetailService paymentDetailService;

    @GetMapping
    public List<PaymentDetail> getAllPayments() {
        return paymentDetailService.getAllPayments();
    }

    @PostMapping
    public PaymentDetail savePayment(@RequestBody PaymentDetail paymentDetail) {
        return paymentDetailService.savePayment(paymentDetail);
    }

}
