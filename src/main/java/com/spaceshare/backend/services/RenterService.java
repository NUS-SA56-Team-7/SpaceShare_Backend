package com.spaceshare.backend.services;

import java.util.UUID;

import com.spaceshare.backend.models.Renter;

public interface RenterService {

	Boolean createRenter(Renter renter);
	
	Renter getRenterById(UUID id);
	
	Renter getRenterByEmail(String email);
}
