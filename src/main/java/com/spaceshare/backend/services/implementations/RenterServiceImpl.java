package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.repos.RenterRepository;
import com.spaceshare.backend.services.RenterService;

@Service
public class RenterServiceImpl implements RenterService {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	RenterRepository repoRenter;

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
}
