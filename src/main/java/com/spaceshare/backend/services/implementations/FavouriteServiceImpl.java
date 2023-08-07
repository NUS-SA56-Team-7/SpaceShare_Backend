package com.spaceshare.backend.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Favourite;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.repos.FavouriteRepository;
import com.spaceshare.backend.repos.PropertyRepository;
import com.spaceshare.backend.repos.TenantRepository;
import com.spaceshare.backend.services.FavouriteService;

@Service
public class FavouriteServiceImpl implements FavouriteService {
	
	/*** Repositories ***/
	@Autowired
	FavouriteRepository repoFavourite;
	
	@Autowired
	TenantRepository repoTenant;
	
	@Autowired
	PropertyRepository repoProperty;
    
	/*** Methods ***/
	public Boolean createFavourite(UUID tenantId, Long propertyId) {
		Tenant tenant = repoTenant.findById(tenantId)
				.orElseThrow(() -> new ResourceNotFoundException());
		Property property = repoProperty.findById(propertyId)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		System.out.println(tenant);
		System.out.println(property);
		try {			
			Favourite favourite = new Favourite();
			favourite.setTenant(tenant);
			favourite.setProperty(property);
			
			repoFavourite.save(favourite);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public List<Favourite> getAllFavouritesByTenantId(UUID tenantId) {
		if (repoTenant.findById(tenantId).isEmpty()) {
			throw new ResourceNotFoundException();
		}
		return repoFavourite.findByTenantId(tenantId);
	}
	
	public Boolean deleteFavourite(Long id) {
		Favourite existingFavourite = repoFavourite.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		try {
			repoFavourite.delete(existingFavourite);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}
