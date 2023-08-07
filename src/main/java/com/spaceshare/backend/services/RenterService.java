package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.models.Renter;

public interface RenterService {

	Boolean createRenter(Renter renter);
	
	Renter getRenterById(UUID id);
	
	Renter getRenterByEmail(String email);

	Renter findRenterById(UUID id);

	Renter saveRenter(Renter renter);

	Renter updateRenter(Renter renter);

	void deleteRenter(Renter renter);
	
	List<Renter> findAllRenters();
}
