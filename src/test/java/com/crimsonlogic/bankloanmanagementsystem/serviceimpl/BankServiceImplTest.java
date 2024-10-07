package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crimsonlogic.bankloanmanagementsystem.dto.BankResponseDto;
import com.crimsonlogic.bankloanmanagementsystem.dto.LoanTypeResponseDto;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;
import com.crimsonlogic.bankloanmanagementsystem.repository.BankRepository;

@ExtendWith(MockitoExtension.class)
class BankServiceImplTest {

    @InjectMocks
    private BankServiceImpl bankService;

    @Mock
    private BankRepository bankRepository;

    private Bank bank;
    private List<LoanType> loanTypes;

    @BeforeEach
    void setUp() {
        // Initialize mock bank and loan types
        bank = new Bank();
        bank.setBankId("BANK123");
        bank.setBankName("Test Bank");
        bank.setBranchAddress("123 Test St, Test City");
        bank.setContactNumber("1234567890");

        loanTypes = new ArrayList<>();
        LoanType loanType1 = new LoanType();
        loanType1.setLoanTypeId("LOAN1");
        loanType1.setLoanName("Home Loan");
        loanType1.setInterestRate(5.5);
        loanType1.setMaxAmount(500000.0);
        loanType1.setDurationYears(15);
        loanTypes.add(loanType1);

        LoanType loanType2 = new LoanType();
        loanType2.setLoanTypeId("LOAN2");
        loanType2.setLoanName("Personal Loan");
        loanType2.setInterestRate(6.0);
        loanType2.setMaxAmount(200000.0);
        loanType2.setDurationYears(5);
        loanTypes.add(loanType2);

        bank.setLoanTypes(loanTypes);
    }

    @Test
    void findByLoginId_ShouldReturnBank_WhenBankExists() {
        when(bankRepository.findByLogin_LoginId(anyString())).thenReturn(bank);

        Bank foundBank = bankService.findByLoginId("loginId123");

        assertEquals("BANK123", foundBank.getBankId());
        assertEquals("Test Bank", foundBank.getBankName());
        verify(bankRepository, times(1)).findByLogin_LoginId("loginId123");
    }

    @Test
    void findAllBanksWithLoans_ShouldReturnListOfBankResponseDtos() {
        List<Bank> banks = new ArrayList<>();
        banks.add(bank);

        when(bankRepository.findAll()).thenReturn(banks);

        List<BankResponseDto> bankResponseDtos = bankService.findAllBanksWithLoans();

        assertEquals(1, bankResponseDtos.size());
        assertEquals("BANK123", bankResponseDtos.get(0).getBankId());
        assertEquals(2, bankResponseDtos.get(0).getLoanTypes().size());
        assertEquals("Home Loan", bankResponseDtos.get(0).getLoanTypes().get(0).getLoanName());
    }
}
