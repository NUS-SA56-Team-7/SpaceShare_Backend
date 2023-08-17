package com.spaceshare.backend.services.implementations;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.InternalServerErrorException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.RecentSearch;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.projections.RecentSearchProjection;
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
	@Transactional
	public Boolean createRecentSearch(UUID tenantId, Long propertyId) {
		Tenant tenant = repoTenant.findById(tenantId)
				.orElseThrow(() -> new ResourceNotFoundException("Tenant cannot be found"));
		Property property = repoProperty.findById(propertyId)
				.orElseThrow(() -> new ResourceNotFoundException("Property cannot be found"));
		
		try {
			
			System.out.println(repoRecentSearch.findFirstByOrderBySearchedAtAsc());
			System.out.println(repoRecentSearch.findByTenantId(tenantId).size());
			System.out.println(repoRecentSearch.findByTenantId(tenantId).size() >= 3);
			
			if (repoRecentSearch.findByTenantId(tenantId).size() >= 3) {
				RecentSearch oldestSearch =
						repoRecentSearch.findFirstByOrderBySearchedAtAsc().get();
				repoRecentSearch.deleteById(oldestSearch.getId());
			}
			
			RecentSearch recent = new RecentSearch();
			recent.setTenant(tenant);
			recent.setProperty(property);
			
			repoRecentSearch.save(recent);
			
			return true;
		}
		catch (DataIntegrityViolationException e) {
			throw new BadRequestException(e.getMessage());
		}
		catch (Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}
	
	public List<RecentSearchProjection> getRecentSearchesByTenantId(UUID tenantId) {
		if (!repoTenant.existsById(tenantId)) {
			throw new ResourceNotFoundException("Tenant does not exist");
		}
		
		return repoRecentSearch.findByTenantId(tenantId);
	}
}
