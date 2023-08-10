package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.models.Favourite;
import com.spaceshare.backend.models.Tenant;

public interface TenantService {

	List<Tenant> getAllTenants();

	Tenant getTenantById(UUID id);
	
	Tenant getTenantByEmail(String email);

	Boolean createTenant(Tenant tenant);

	Boolean saveTenant(Tenant tenant);

	Tenant updateTenant(UUID id, Tenant tenant);

	Boolean deleteTenant(UUID id);
}
