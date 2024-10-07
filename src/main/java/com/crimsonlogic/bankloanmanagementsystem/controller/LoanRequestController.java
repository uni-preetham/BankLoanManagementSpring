package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.dto.LoanRequestDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Address;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanRequest;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.service.AddressService;
import com.crimsonlogic.bankloanmanagementsystem.service.LoanRequestService;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;

import jakarta.servlet.http.HttpSession;

import com.crimsonlogic.bankloanmanagementsystem.service.LoanTypeService;
import com.crimsonlogic.bankloanmanagementsystem.service.OccupationService;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;
import com.crimsonlogic.bankloanmanagementsystem.entity.Occupation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true") // adjust the React frontend URL
@RestController
@RequestMapping("/api/loan-requests")
public class LoanRequestController {

    @Autowired
    private LoanRequestService loanRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoanTypeService loanTypeService;
    @Autowired
    private OccupationService occupationService;
    @Autowired
    private AddressService addressService;

    
    @PostMapping("/apply")
    public ResponseEntity<String> applyForLoan(
            @RequestParam("userId") String userId,
            @RequestParam("loanTypeId") String loanTypeId,
            @RequestParam("requestedAmount") Double requestedAmount,
            @RequestParam("loanTerm") int loanTerm,
//            @RequestParam("addressLine1") String addressLine1,
//            @RequestParam("addressLine2") String addressLine2,
//            @RequestParam("city") String city,
//            @RequestParam("state") String state,
//            @RequestParam("zipCode") String zipCode,
//            @RequestParam("occupationType") String occupationType,
//            @RequestParam("occupationName") String occupationName,
//            @RequestParam("companyName") String companyName,
            @RequestParam("salary") double salary,
            @RequestParam("document") MultipartFile document) {

        try {
            // Find the User by userId
            User user = userService.getUser(userId);
            if (user == null) {
                throw new ResourceNotFoundException("User not found with ID: " + userId);
            }
            
            if (user.getCreditScore() < 700) {
                return new ResponseEntity<>("You're not eligible to apply for a loan due to low credit score.", HttpStatus.FORBIDDEN);
            }


            // Find the LoanType by loanTypeId
            LoanType loanType = loanTypeService.getLoanTypeById(loanTypeId);
            System.out.println(loanType);
            if (loanType == null) {
                throw new ResourceNotFoundException("Loan Type not found with ID: " + loanTypeId);
            }

            // Create a new LoanRequest object
            LoanRequest loanRequest = new LoanRequest();
            loanRequest.setUser(user);
            loanRequest.setLoanType(loanType);
            loanRequest.setRequestedAmount(requestedAmount);
            loanRequest.setLoanTerm(loanTerm);
            loanRequest.setRequestDate(LocalDate.now());
            loanRequest.setStatus("Pending");

//            // Check if the user already has an address
//            if (user.getAddress() == null) {
//                // Save address details (assuming Address is a separate entity)
//                Address address = new Address();
//                address.setAddressLine1(addressLine1);
//                address.setAddressLine2(addressLine2);
//                address.setCity(city);
//                address.setState(state);
//                address.setZipCode(zipCode);
//                address.setUser(user); // Assuming address is linked to the user
//                user.setAddress(address);
//                addressService.saveAddress(address);
//                userService.saveUser(user); // Update user with address
//            }
//
//            // Check if the user already has an occupation
//            if (user.getOccupation() == null) {
//                // Save occupation details (assuming Occupation is a separate entity)
//                Occupation occupation = new Occupation();
//                occupation.setOccupationType(occupationType);
//                occupation.setOccupationName(occupationName);
//                occupation.setCompanyName(companyName);
//                occupation.setSalary(salary);
//                occupation.setUser(user); // Assuming occupation is linked to the user
//                user.setOccupation(occupation);
//                occupationService.saveOccupation(occupation);
//                userService.saveUser(user); // Update user with occupation
//            }

            // Save the document (you can modify this part to save the file)
            if (!document.isEmpty()) {
                String directory = "uploaded-documents/";
                Path uploadPath = Paths.get(directory);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String documentFileName = userId + "_" + document.getOriginalFilename();
                Path documentPath = uploadPath.resolve(documentFileName);

                Files.write(documentPath, document.getBytes());

                loanRequest.setDocumentPath(documentPath.toString());
            }

            // Save the loan request
            loanRequestService.createLoanRequest(loanRequest);

            return new ResponseEntity<>("Loan request submitted successfully!", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException ex) {
            return new ResponseEntity<>("Error processing document: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error submitting loan request: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    

    @GetMapping("/userloans")
    public ResponseEntity<List<LoanRequestDTO>> getUserLoanRequests(HttpSession session) {
        User user = (User) session.getAttribute("user");
        System.out.println(user);
        List<LoanRequestDTO> loanRequests = loanRequestService.getLoanRequestsByUserId(user.getUserId());
        return new ResponseEntity<>(loanRequests, HttpStatus.OK);
    }

    
    
    @GetMapping("/document/{loanRequestId}")
    public ResponseEntity<byte[]> getLoanRequestDocument(@PathVariable String loanRequestId) {
        try {
            LoanRequest loanRequest = loanRequestService.getLoanRequestById(loanRequestId);

            if (loanRequest == null || loanRequest.getDocumentPath() == null) {
                throw new ResourceNotFoundException("Document not found for loan request ID: " + loanRequestId);
            }

            // Retrieve the document file
            Path documentPath = Paths.get(loanRequest.getDocumentPath());
            byte[] documentData = Files.readAllBytes(documentPath);

            // Create the response with the file data and content type
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + documentPath.getFileName().toString() + "\"")
                    .body(documentData);

        } catch (IOException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
