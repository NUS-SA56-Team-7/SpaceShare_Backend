package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.models.RecentSearch;

public interface RecentSearchService {

	Boolean createRecentSearch(UUID tenantId, Long propertyId);
	
	List<RecentSearch> getRecentSearchesByTenantId(UUID tenantId);
}
