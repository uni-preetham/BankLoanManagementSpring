package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.dto.LoanRequestDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanRequest;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.service.LoanRequestService;
import com.crimsonlogic.bankloanmanagementsystem.service.LoanTypeService;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanRequestControllerTest {

    @InjectMocks
    private LoanRequestController loanRequestController;

    @MockBean
    private LoanRequestService loanRequestService;

    @MockBean
    private UserService userService;

    @MockBean
    private LoanTypeService loanTypeService;

    @MockBean
    private HttpSession session;

    @MockBean
    private MultipartFile document;

    private User user;
    private LoanType loanType;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId("USER1");
        user.setCreditScore(750); // Eligible for loan
        loanType = new LoanType();
        loanType.setLoanTypeId("LOAN1");
        loanType.setInterestRate(5.0);
    }

    @Test
    public void testApplyForLoan_Success() throws IOException {
        // Mock behavior
        when(userService.getUser("USER1")).thenReturn(user);
        when(loanTypeService.getLoanTypeById("LOAN1")).thenReturn(loanType);
        when(document.isEmpty()).thenReturn(false);
        when(document.getOriginalFilename()).thenReturn("test-doc.pdf");
        when(document.getBytes()).thenReturn(new byte[]{1, 2, 3}); // Simulated file bytes

        ResponseEntity<String> response = loanRequestController.applyForLoan(
                "USER1", "LOAN1", 10000.0, 5, 5000, document
        );

        // Verify interactions
        verify(loanRequestService).createLoanRequest(any(LoanRequest.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Loan request submitted successfully!", response.getBody());

        // Verify that the document was saved
        Path uploadPath = Paths.get("uploaded-documents/");
        assertEquals(true, Files.exists(uploadPath.resolve("USER1_test-doc.pdf")));
    }

    @Test
    public void testApplyForLoan_UserNotFound() {
        // Mock behavior
        when(userService.getUser("USER2")).thenThrow(new ResourceNotFoundException("User not found with ID: USER2"));

        ResponseEntity<String> response = loanRequestController.applyForLoan(
                "USER2", "LOAN1", 10000.0, 5, 5000, document
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found with ID: USER2", response.getBody());
    }

    @Test
    public void testApplyForLoan_LoanTypeNotFound() {
        // Mock behavior
        when(userService.getUser("USER1")).thenReturn(user);
        when(loanTypeService.getLoanTypeById("LOAN2")).thenThrow(new ResourceNotFoundException("Loan Type not found with ID: LOAN2"));

        ResponseEntity<String> response = loanRequestController.applyForLoan(
                "USER1", "LOAN2", 10000.0, 5, 5000, document
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Loan Type not found with ID: LOAN2", response.getBody());
    }

    @Test
    public void testApplyForLoan_LowCreditScore() throws Exception {
        // Create and configure the user
        user = new User();
        user.setUserId("USER1");
        user.setCreditScore(650); // Not eligible for loan

        when(userService.getUser(user.getUserId())).thenReturn(user);

        // Call the applyForLoan method
        ResponseEntity<String> response = loanRequestController.applyForLoan(
                "USER1", "LOAN1", 10000.0, 5, 5000, document
        );

        // Assertions to check the response
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You're not eligible to apply for a loan due to low credit score.", response.getBody());
    }


    @Test
    public void testApplyForLoan_DocumentProcessingError() throws IOException {
        // Mock behavior
        when(userService.getUser("USER1")).thenReturn(user);
        when(loanTypeService.getLoanTypeById("LOAN1")).thenReturn(loanType);
        when(document.isEmpty()).thenReturn(false);
        when(document.getBytes()).thenThrow(new IOException("Error reading file"));

        ResponseEntity<String> response = loanRequestController.applyForLoan(
                "USER1", "LOAN1", 10000.0, 5, 5000, document
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error processing document: Error reading file", response.getBody());
    }
}
