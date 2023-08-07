package com.spaceshare.backend.services;

import java.util.UUID;

import com.spaceshare.backend.models.Tenant;

public interface TenantService {

	Boolean createTenant(Tenant tenant);
	
	Tenant getTenantById(UUID id);
	
	Tenant getTenantByEmail(String email);
}
