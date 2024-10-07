package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.entity.Address;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.service.AddressService;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AddressControllerTest {

    @InjectMocks
    private AddressController addressController;

    @Mock
    private AddressService addressService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    private User user;
    private Address address;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId("USER1");
        user.setFirstName("John");
        user.setLastName("Doe");

        address = new Address();
        address.setUser(user);
        address.setState("Karnataka");
        address.setCity("Bengaluru");
        address.setZipCode("12345");
    }

    @Test
    void testGetAddressByUserId() {
        when(addressService.getAddressByUserId("USER1")).thenReturn(address);

        ResponseEntity<Address> response = addressController.getAddressByUserId("USER1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(address, response.getBody());
        verify(addressService).getAddressByUserId("USER1");
    }

    @Test
    void testAddOrUpdateAddress() {
        when(addressService.saveAddress(anyString(), any())).thenReturn(address);
        when(userService.getUser("USER1")).thenReturn(user);

        ResponseEntity<Address> response = addressController.addOrUpdateAddress("USER1", address, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(address, response.getBody());
        verify(addressService).saveAddress("USER1", address);
        verify(userService).getUser("USER1");
        verify(session).setAttribute("user", user);
    }
}
