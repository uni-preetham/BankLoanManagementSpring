package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import com.crimsonlogic.bankloanmanagementsystem.dto.RevenueDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Account;
import com.crimsonlogic.bankloanmanagementsystem.entity.PaymentDetail;
import com.crimsonlogic.bankloanmanagementsystem.repository.AccountRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.PaymentDetailRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.PaymentDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentDetailServiceImpl implements PaymentDetailService{

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;
    @Autowired
    private AccountRepository accountRepository;
    
    

    @Override
    public List<PaymentDetail> getAllPayments() {
        return paymentDetailRepository.findAll();
    }

    @Override
    public PaymentDetail savePayment(PaymentDetail paymentDetail) {
        return paymentDetailRepository.save(paymentDetail);
    }

    @Override
    public List<RevenueDTO> getMonthlyRevenueByBank(String bankId) {
        // Get the current year
        int currentYear = LocalDate.now().getYear();
        List<RevenueDTO> revenueList = new ArrayList<>();

        // Calculate revenue for each month
        for (int month = 1; month <= 12; month++) {
            LocalDate startDate = YearMonth.of(month, 1).atDay(1);
            LocalDate endDate = YearMonth.of(month, 1).atEndOfMonth(); // No need for plusDays(1)

            // Fetch accounts for the specific bank
            List<Account> accounts = accountRepository.findByBankBankId(bankId);
            double totalRevenue = 0.0;

            // Calculate total revenue from payments made for each account in the current month
            for (Account account : accounts) {
                // Check the approval date to ensure we're only summing revenues from the current month
                if (account.getApprovalDate().getYear() == currentYear &&
                    account.getApprovalDate().getMonthValue() == month) {

                	totalRevenue += account.getTotalPayment();
                	
//                    for (PaymentDetail paymentDetail : account.getPaymentSchedule()) {
//                        // Check if the payment detail month matches the current month
//                        if (paymentDetail.getMonth() == month) {
//                            // Assuming you want to sum up the interest payments
//                            totalRevenue += paymentDetail.getInterestPayment();
//                        }
//                    }
                }
            }

            revenueList.add(new RevenueDTO(month, totalRevenue));
        }

        return revenueList;
    }
}
