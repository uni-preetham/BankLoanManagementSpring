package com.crimsonlogic.bankloanmanagementsystem.service;

import java.util.List;

import com.crimsonlogic.bankloanmanagementsystem.dto.RevenueDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.PaymentDetail;

public interface PaymentDetailService {

	List<PaymentDetail> getAllPayments();

	PaymentDetail savePayment(PaymentDetail paymentDetail);

	List<RevenueDTO> getMonthlyRevenueByBank(String bankId);


}
