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

	public Boolean createTenant(Tenant tenant) {
		try {
			tenant.setPassword(passwordEncoder.encode(tenant.getPassword()));
			tenant.setCreatedAt(LocalDate.now());
			tenant.setUpdatedAt(LocalDate.now());
			
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
