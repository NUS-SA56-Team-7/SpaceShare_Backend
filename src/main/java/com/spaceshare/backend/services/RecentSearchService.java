package com.spaceshare.backend.services;

import java.util.UUID;

public interface RecentSearchService {

	Boolean createRecentSearch(UUID tenantId, Long propertyId);
}
