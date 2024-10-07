package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crimsonlogic.bankloanmanagementsystem.entity.Address;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.repository.AddressRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Address address;

    @BeforeEach
    void setUp() {
        // Initialize mock user and address
        user = new User();
        user.setUserId("USER123");
        user.setFirstName("John");
        user.setLastName("Doe");

        address = new Address();
        address.setState("Karnataka");
        address.setCity("Test City");
        address.setZipCode("12345");
    }

    @Test
    void getAddressByUserId_ShouldReturnAddress_WhenAddressExists() {
        when(addressRepository.findByUserUserId(anyString())).thenReturn(address);

        Address foundAddress = addressService.getAddressByUserId("USER123");

        assertEquals("Karnataka", foundAddress.getState());
        assertEquals("Test City", foundAddress.getCity());
        assertEquals("12345", foundAddress.getZipCode());
        verify(addressRepository, times(1)).findByUserUserId("USER123");
    }

    @Test
    void getAddressByUserId_ShouldReturnNull_WhenAddressDoesNotExist() {
        when(addressRepository.findByUserUserId(anyString())).thenReturn(null);

        Address foundAddress = addressService.getAddressByUserId("USER123");

        assertEquals(null, foundAddress);
        verify(addressRepository, times(1)).findByUserUserId("USER123");
    }

    @Test
    void saveAddress_ShouldSaveAddress_WhenUserExists() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address savedAddress = addressService.saveAddress("USER123", address);

        assertEquals("Karnataka", savedAddress.getState());
        verify(userRepository, times(1)).findById("USER123");
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void saveAddress_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            addressService.saveAddress("USER123", address);
        });

        assertEquals("User not found", thrown.getMessage());
        verify(userRepository, times(1)).findById("USER123");
        verify(addressRepository, never()).save(any(Address.class)); // Ensure save is never called
    }

    @Test
    void saveAddress_WithAddress_ShouldSaveAddress() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        addressService.saveAddress(address);

        verify(addressRepository, times(1)).save(address);
    }
}
