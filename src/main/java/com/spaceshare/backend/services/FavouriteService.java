package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.models.Favourite;

public interface FavouriteService {
    
	Boolean createFavourite(UUID tenantId, Long propertyId);
	
	List<Favourite> getAllFavouritesByTenantId(UUID tenantId);
	
	Boolean deleteFavourite(Long id);
}
