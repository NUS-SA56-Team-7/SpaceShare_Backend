package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.models.RecentSearch;
import com.spaceshare.backend.projections.RecentSearchProjection;

public interface RecentSearchService {

	Boolean createRecentSearch(UUID tenantId, Long propertyId);
	
	List<RecentSearchProjection> getRecentSearchesByTenantId(UUID tenantId);
}
