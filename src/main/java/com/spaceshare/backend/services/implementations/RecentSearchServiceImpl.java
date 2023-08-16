package com.spaceshare.backend.services.implementations;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.InternalServerErrorException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.RecentSearch;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.repos.PropertyRepository;
import com.spaceshare.backend.repos.RecentSearchRepository;
import com.spaceshare.backend.repos.TenantRepository;
import com.spaceshare.backend.services.RecentSearchService;

@Service
public class RecentSearchServiceImpl implements RecentSearchService {
	
	/*** Repositories ***/
	@Autowired
	RecentSearchRepository repoRecentSearch;
	
	@Autowired
	TenantRepository repoTenant;
	
	@Autowired
	PropertyRepository repoProperty;

	/*** Methods ***/
	public Boolean createRecentSearch(UUID tenantId, Long propertyId) {
		Tenant tenant = repoTenant.findById(tenantId)
				.orElseThrow(() -> new ResourceNotFoundException());
		Property property = repoProperty.findById(propertyId)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		try {
			RecentSearch recent = new RecentSearch();
			recent.setTenant(tenant);
			recent.setProperty(property);
			
			return true;
		}
		catch (DataIntegrityViolationException e) {
			throw new BadRequestException(e.getMessage());
		}
		catch (Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}
}
