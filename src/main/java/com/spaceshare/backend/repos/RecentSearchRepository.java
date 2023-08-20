package com.spaceshare.backend.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.RecentSearch;

@Repository
public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {

	List<RecentSearch> findByTenantId(UUID tenantId);
	
	List<RecentSearch> findByPropertyId(Long id);
	
	Optional<RecentSearch> findFirstByOrderBySearchedAtAsc();
}