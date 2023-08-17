package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.models.enums.Status;
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

	/*** Methods ***/
	@Override
	public List<Renter> getAllRenters() {
		return repoRenter.findAll();
	}
	
	public Renter getRenterById(UUID id) {
		return repoRenter.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
	}

	public Renter getRenterByEmail(String email) {
		return repoRenter.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException());
	}

	@Override
	public Boolean createRenter(Renter renter) {
		try {
			if (renter.getPassword() != null)
				renter.setPassword(passwordEncoder.encode(renter.getPassword()));

			renter.setCreatedAt(LocalDate.now());
			repoRenter.saveAndFlush(renter);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Renter updateRenter(UUID id, Renter inRenter) {
		Renter r = repoRenter.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		r.setEmail(inRenter.getEmail());
		r.setFirstName(inRenter.getFirstName());
		r.setLastName(inRenter.getLastName());
		r.setIdentificationNumber(r.getIdentificationNumber());
		r.setPhone(inRenter.getPhone());
		r.setAddress(inRenter.getAddress());
		r.setDateOfBirth(inRenter.getDateOfBirth());
		r.setPhotoUrl(inRenter.getPhotoUrl());
		r.setUpdatedAt(LocalDate.now());

		return repoRenter.saveAndFlush(r);
	}

	@Override
	public Boolean deleteRenter(UUID id) {
		Renter renter = getRenterById(id);
		if (renter != null) {
			renter.setStatus(Status.INACTIVE);
			repoRenter.saveAndFlush(renter);
			return true;
		} else {
			return false;
		}

	}

	public Boolean checkPassword(Renter renter, String password) {
		return false;
	}
}
