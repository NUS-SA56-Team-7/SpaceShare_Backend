package com.spaceshare.backend.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.RecentSearch;
import com.spaceshare.backend.projections.RecentSearchProjection;

@Repository
public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {

	List<RecentSearchProjection> findByTenantId(UUID tenantId);
	
	Optional<RecentSearch> findFirstByOrderBySearchedAtAsc();
}