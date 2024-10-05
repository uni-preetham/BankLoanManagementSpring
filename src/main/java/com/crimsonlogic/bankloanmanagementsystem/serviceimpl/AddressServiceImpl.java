package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimsonlogic.bankloanmanagementsystem.entity.Address;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.repository.AddressRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.UserRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.AddressService;


@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
   

    @Autowired
    private UserRepository userRepository;

    @Override
    public Address getAddressByUserId(String userId) {
        return addressRepository.findByUserUserId(userId);
    }

    @Override
    public Address saveAddress(String userId, Address address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public void saveAddress(Address address) {
        addressRepository.save(address);
    }

}
