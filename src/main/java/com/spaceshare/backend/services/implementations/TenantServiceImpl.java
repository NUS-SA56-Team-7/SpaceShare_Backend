package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.DuplicateResourceException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.models.enums.Status;
import com.spaceshare.backend.repos.TenantRepository;
import com.spaceshare.backend.services.TenantService;

@Service
public class TenantServiceImpl implements TenantService {

	/*** Repositories ***/
	@Autowired
	TenantRepository repoTenant;
	
	/*** Miscellaneous ***/
	@Autowired
	PasswordEncoder passwordEncoder;

	/*** Methods ***/
	public List<Tenant> getAllTenants() {
		return repoTenant.findAll();
	}

	public Boolean saveTenant(Tenant tenant) {
		if (checkTenantEmailExist(tenant.getEmail()))
			throw new DuplicateResourceException();
		try {
			if (tenant.getPassword() != null) {
				tenant.setPassword(passwordEncoder.encode(tenant.getPassword()));
			}
			tenant.setCreatedAt(LocalDate.now());
			repoTenant.saveAndFlush(tenant);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public Tenant updateTenant(UUID id, Tenant inTenant) {
		Tenant t = repoTenant.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		t.setEmail(inTenant.getEmail());
		t.setFirstName(inTenant.getFirstName());
		t.setLastName(inTenant.getLastName());
		t.setIdentificationNumber(t.getIdentificationNumber());
		t.setPhone(inTenant.getPhone());
		t.setAddress(inTenant.getAddress());
		t.setDateOfBirth(inTenant.getDateOfBirth());
		t.setPhotoUrl(inTenant.getPhotoUrl());
		t.setUpdatedAt(LocalDate.now());

		return repoTenant.saveAndFlush(t);
	}

	public Boolean deleteTenant(UUID id) {

		try {
			Tenant tenant = getTenantById(id);
		if (tenant != null) {
			tenant.setStatus(Status.INACTIVE);
			repoTenant.saveAndFlush(tenant);
			return true;
		} else {
			return false;
		}
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean createTenant(Tenant tenant) {
		try {
			tenant.setPassword(passwordEncoder.encode(tenant.getPassword()));
			tenant.setCreatedAt(LocalDate.now());
			
			repoTenant.save(tenant);
			return true;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public Tenant getTenantById(UUID id) {
		return repoTenant.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
	
	public Tenant getTenantByEmail(String email) {
		return repoTenant.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException());
	}

	public Boolean checkTenantEmailExist(String email) {
		return repoTenant.existsByEmail(email);
	}

}
