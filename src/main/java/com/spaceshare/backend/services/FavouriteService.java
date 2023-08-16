package com.spaceshare.backend.services;

import java.util.UUID;

public interface FavouriteService {
    
	Boolean createFavourite(UUID tenantId, Long propertyId);
	
	Boolean deleteFavourite(UUID tenantId, Long propertyId);
}
