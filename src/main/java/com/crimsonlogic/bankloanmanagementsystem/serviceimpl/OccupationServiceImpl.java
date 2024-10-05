package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimsonlogic.bankloanmanagementsystem.entity.Occupation;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.repository.OccupationRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.UserRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.OccupationService;


@Service
public class OccupationServiceImpl implements OccupationService {

    @Autowired
    private OccupationRepository occupationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveOccupation(Occupation occupation) {
        occupationRepository.save(occupation);
    }
    
    @Override
    public Occupation getOccupationByUserId(String userId) {
        return occupationRepository.findByUserUserId(userId);
    }

    @Override
    public Occupation saveOccupation(String userId, Occupation occupation) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        occupation.setUser(user);
        return occupationRepository.save(occupation);
    }

}
