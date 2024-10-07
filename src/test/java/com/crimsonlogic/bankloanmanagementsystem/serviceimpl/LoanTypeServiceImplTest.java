package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import com.crimsonlogic.bankloanmanagementsystem.dto.LoanTypeDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.repository.LoanTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoanTypeServiceImplTest {

    @InjectMocks
    private LoanTypeServiceImpl loanTypeService;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @Mock
    private ModelMapper modelMapper;

    private Bank bank;
    private LoanType loanType;
    private LoanTypeDTO loanTypeDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initializing test data
        bank = new Bank();
        bank.setBankId("bank1");

        loanType = new LoanType();
        loanType.setLoanTypeId("loanType1");
        loanType.setBank(bank);

        loanTypeDTO = new LoanTypeDTO();
        loanTypeDTO.setLoanName("Home Loan");
        loanTypeDTO.setInterestRate(5.5);
        loanTypeDTO.setMaxAmount(500000.0);
        loanTypeDTO.setDurationYears(15);
    }

    @Test
    void testAddLoanType() {
        // Mocking behavior
        when(modelMapper.map(any(LoanTypeDTO.class), eq(LoanType.class))).thenReturn(loanType);

        // Call the method to test
        loanTypeService.addLoanType(loanTypeDTO, bank);

        // Verify that the repository saves the loan type
        verify(loanTypeRepository, times(1)).save(loanType);
        assertEquals(bank, loanType.getBank());  // Ensure bank is set
    }

    @Test
    void testGetLoanTypeById_Success() {
        // Mocking behavior
        when(loanTypeRepository.findById(anyString())).thenReturn(Optional.of(loanType));

        // Call the method to test
        LoanType fetchedLoanType = loanTypeService.getLoanTypeById("loanType1");

        // Assertions
        assertNotNull(fetchedLoanType);
        assertEquals("loanType1", fetchedLoanType.getLoanTypeId());
        verify(loanTypeRepository, times(1)).findById("loanType1");
    }

    @Test
    void testGetLoanTypeById_NotFound() {
        // Mocking behavior for not found
        when(loanTypeRepository.findById(anyString())).thenReturn(Optional.empty());

        // Call the method to test and verify exception is thrown
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            loanTypeService.getLoanTypeById("unknownLoanTypeId");
        });

        // Assertions
        assertEquals("Loan type with id unknownLoanTypeId not found", exception.getMessage());
        verify(loanTypeRepository, times(1)).findById("unknownLoanTypeId");
    }

    @Test
    void testGetLoanTypesByBank() {
        // Mocking behavior
        when(loanTypeRepository.findByBank(bank)).thenReturn(Collections.singletonList(loanType));

        // Call the method to test
        List<LoanType> loanTypes = loanTypeService.getLoanTypesByBank(bank);

        // Assertions
        assertNotNull(loanTypes);
        assertEquals(1, loanTypes.size());
        assertEquals("loanType1", loanTypes.get(0).getLoanTypeId());
        verify(loanTypeRepository, times(1)).findByBank(bank);
    }

    @Test
    void testDeleteLoanTypeById() {
        // Call the method to test
        loanTypeService.deleteLoanTypeById("loanType1");

        // Verify that the repository deletes the loan type
        verify(loanTypeRepository, times(1)).deleteById("loanType1");
    }

    @Test
    void testEditLoanType_Success() {
        // Mocking behavior
        when(loanTypeRepository.findById(anyString())).thenReturn(Optional.of(loanType));
        when(modelMapper.map(any(LoanTypeDTO.class), eq(LoanType.class))).thenReturn(loanType);

        // Call the method to test
        loanTypeService.editLoanType("loanType1", loanTypeDTO);

        // Verify that the loan type is saved with updated details
        verify(loanTypeRepository, times(1)).save(loanType);
    }

    @Test
    void testEditLoanType_NotFound() {
        // Mocking behavior for not found
        when(loanTypeRepository.findById(anyString())).thenReturn(Optional.empty());

        // Call the method to test and verify exception is thrown
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            loanTypeService.editLoanType("unknownLoanTypeId", loanTypeDTO);
        });

        // Assertions
        assertEquals("Loan type with id unknownLoanTypeId not found", exception.getMessage());
        verify(loanTypeRepository, times(1)).findById("unknownLoanTypeId");
    }
}
