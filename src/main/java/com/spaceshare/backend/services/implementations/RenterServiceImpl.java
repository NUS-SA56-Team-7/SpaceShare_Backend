package com.spaceshare.backend.services.implementations;

import java.util.List;
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

	@Override
	public Renter findRenterById(UUID id) {
		return repoRenter.findById(id).orElse(null);
	}

	@Override
	public Renter saveRenter(Renter renter) {
		if(renter.getPassword() != null)
		{
			renter.setPassword(passwordEncoder.encode(renter.getPassword()));
		}
		return repoRenter.saveAndFlush(renter);
	}

	@Override
	public Renter updateRenter(Renter renter) {
		if(renter.getPassword() != null)
		{
			renter.setPassword(passwordEncoder.encode(renter.getPassword()));
		}
		return repoRenter.saveAndFlush(renter);
	}

	@Override
	public void deleteRenter(Renter renter) {
		repoRenter.delete(renter);
	}

	@Override
	public List<Renter> findAllRenters() {
		return repoRenter.findAll();
	}

	@Override
	public Boolean createRenter(Renter renter) {
		throw new UnsupportedOperationException("Unimplemented method 'createRenter'");
	}

	public Boolean checkPassword(Renter renter, String password) {
		return false;
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
