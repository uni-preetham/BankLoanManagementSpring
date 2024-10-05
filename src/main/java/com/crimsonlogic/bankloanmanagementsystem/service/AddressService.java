package com.crimsonlogic.bankloanmanagementsystem.service;

import java.util.List;

import com.crimsonlogic.bankloanmanagementsystem.entity.Address;

public interface AddressService {
    void saveAddress(Address address);

	Address getAddressByUserId(String userId);

	Address saveAddress(String userId, Address address);
}
