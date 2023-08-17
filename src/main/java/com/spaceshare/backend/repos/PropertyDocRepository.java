package com.spaceshare.backend.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.PropertyDoc;

@Repository
public interface PropertyDocRepository extends JpaRepository<PropertyDoc, Long> {
 
	List<PropertyDoc> findByPropertyId(Long propertyId);
}
