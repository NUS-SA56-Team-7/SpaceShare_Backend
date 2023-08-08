package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.models.Favourite;

public interface FavouriteService {
    
	Boolean createFavourite(UUID tenantId, Long propertyId);
	
	Boolean deleteFavourite(Long id);
}
