package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.models.Renter;

public interface RenterService {

	List<Renter> getAllRenters();
	
	Renter getRenterById(UUID id);
	
	Renter getRenterByEmail(String email);

	Renter findRenterById(UUID id);

	Boolean createRenter(Renter renter);

	Renter updateRenter(UUID id, Renter renter);

	Boolean deleteRenter(UUID id);
}
