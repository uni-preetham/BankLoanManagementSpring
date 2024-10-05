package com.crimsonlogic.bankloanmanagementsystem.service;

import com.crimsonlogic.bankloanmanagementsystem.entity.Occupation;

public interface OccupationService {
    void saveOccupation(Occupation occupation);


	Occupation saveOccupation(String userId, Occupation occupation);

	Occupation getOccupationByUserId(String userId);
}
