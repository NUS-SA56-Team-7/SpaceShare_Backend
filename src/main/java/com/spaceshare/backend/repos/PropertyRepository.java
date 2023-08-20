package com.spaceshare.backend.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.projections.PropertyDetailProjection;
import com.spaceshare.backend.projections.PropertyProjection;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    
	Page<PropertyProjection> findAllBy(Pageable pageable);
	
	@Modifying
	@Query("UPDATE Property p SET p.viewCount = :viewCount WHERE p.id = :id")
	void updateViewCountById(@Param("id") Long id, @Param("viewCount") Long viewCount);
	
	@Query("SELECT p FROM Property p WHERE p.id = :id")
	Optional<PropertyDetailProjection> findPropertyById(@Param("id") Long id);
	
	List<PropertyProjection> findPropertyByRenterId(UUID renterId);

//	List<PropertyProjection> findByNameContainingAndCategoryContaining(
//			String nameKeyword, String categoryKeyword);
}
