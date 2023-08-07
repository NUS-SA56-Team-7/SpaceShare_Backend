package com.spaceshare.backend.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.Favourite;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    
	List<Favourite> findByTenantId(UUID tenantId);
}
