package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Favourite;
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

	@Override
	public List<Tenant> getAllTenants() {
		return repoTenant.findAll();
	}

	@Override
	public Boolean saveTenant(Tenant tenant) {

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

	@Override
	public Tenant updateTenant(UUID id, Tenant inTenant) {

		try {
			Tenant t = getTenantById(id);
			if (t != null) {

				t.setAddress(inTenant.getAddress());
				t.setDateOfBirth(inTenant.getDateOfBirth());
				t.setEmail(inTenant.getEmail());
				t.setFirstName(inTenant.getFirstName());
				t.setIdentificationNumber(inTenant.getIdentificationNumber());
				t.setLastName(inTenant.getLastName());
				t.setPassword(passwordEncoder.encode(inTenant.getPassword()));
				t.setPhone(inTenant.getPhone());
				// r.setProfileImageUrl(inTenant.getProfileImageUrl());
				t.setUpdatedAt(LocalDate.now());

				return repoTenant.saveAndFlush(t);
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	@Override
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

}
