package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.repos.RenterRepository;
import com.spaceshare.backend.services.RenterService;

@Service
public class RenterServiceImpl implements RenterService {
	
	/*** Repositories ***/
	@Autowired
	RenterRepository repoRenter;
	
	/*** Miscellaneous ***/
	@Autowired
	PasswordEncoder passwordEncoder;

	public Boolean createRenter(Renter renter) {
		try {
			renter.setPassword(passwordEncoder.encode(renter.getPassword()));
			renter.setCreatedAt(LocalDate.now());
			renter.setUpdatedAt(LocalDate.now());
			
			repoRenter.save(renter);
			return true;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public Renter getRenterById(UUID id) {
		return repoRenter.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public Renter getRenterByEmail(String email) {
		return repoRenter.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
}
