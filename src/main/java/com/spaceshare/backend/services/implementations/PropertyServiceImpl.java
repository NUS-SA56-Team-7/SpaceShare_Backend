package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.enums.ApproveStatus;
import com.spaceshare.backend.models.enums.PostType;

import com.spaceshare.backend.models.enums.PropertyType;
import com.spaceshare.backend.models.enums.RoomType;
import com.spaceshare.backend.models.enums.Status;
import com.spaceshare.backend.models.enums.TenantType;
import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.projections.PropertyDetailProjection;
import com.spaceshare.backend.projections.PropertyProjection;
import com.spaceshare.backend.repos.PropertyRepository;
import com.spaceshare.backend.repos.RenterRepository;
import com.spaceshare.backend.repos.TenantRepository;
import com.spaceshare.backend.services.PropertyAmenityService;
import com.spaceshare.backend.services.PropertyDocService;
import com.spaceshare.backend.services.PropertyFacilityService;
import com.spaceshare.backend.services.PropertyImageService;
import com.spaceshare.backend.services.PropertyService;
import com.spaceshare.backend.services.RecentSearchService;

@Service
public class PropertyServiceImpl implements PropertyService {

	/*** Repositories ***/
	@Autowired
	PropertyRepository repoProperty;

	@Autowired
	RenterRepository repoRenter;
	
	@Autowired
	TenantRepository repoTenant;

	/*** Services ***/
	@Autowired
	PropertyImageService svcPropertyImage;

	@Autowired
	PropertyDocService svcPropertyDoc;

	@Autowired
	PropertyAmenityService svcPropertyAmenity;

	@Autowired
	PropertyFacilityService svcPropertyFacility;
	
	@Autowired
	RecentSearchService svcRecentSearch;

	/*** Methods ***/
	/* Renter Properties */
	@Transactional
	public Boolean createRenterProperty(UUID renterId, Property property) {
		Renter renter = repoRenter.findById(renterId)
				.orElseThrow(() -> new BadRequestException("Renter does not exist"));
		
		property.setCreatedAt(LocalDate.now());
		property.setUpdatedAt(LocalDate.now());
		property.setRenter(renter);

		Property savedProperty = repoProperty.save(property);
		svcPropertyImage.createPropertyImages(savedProperty, property.getPropertyImageURLs());
		svcPropertyDoc.createPropertyDocs(savedProperty, property.getPropertyDocURLs());
		svcPropertyAmenity.createPropertyAmenities(savedProperty, property.getPropertyAmenityIDs());
		svcPropertyFacility.createPropertyFacilities(savedProperty, property.getPropertyFacilityIDs());

		return true;
	}

	@Transactional
	public Boolean updateRenterProperty(UUID renterId, Long propertyId, Property property) {
		if (!repoRenter.existsById(renterId)) {
			throw new BadRequestException("Renter does not exist");
		}
		
		Property existingProperty = repoProperty.findById(propertyId)
				.orElseThrow(() -> new ResourceNotFoundException());

		existingProperty.setTitle(property.getTitle());
		existingProperty.setDescription(property.getTitle());
		existingProperty.setPropertyType(property.getPropertyType());
		existingProperty.setRoomType(property.getRoomType());
		existingProperty.setRentalFees(property.getRentalFees());
		existingProperty.setAddress(property.getAddress());
		existingProperty.setPostalCode(property.getPostalCode());
		existingProperty.setFurnishment(property.getFurnishment());

		Property savedProperty = repoProperty.save(existingProperty);
		
		svcPropertyImage.updatePropertyImages(savedProperty, property.getPropertyImageURLs());
		svcPropertyAmenity.updatePropertyAmenities(savedProperty, property.getPropertyAmenityIDs());
		svcPropertyFacility.updatePropertyFacilities(savedProperty, property.getPropertyFacilityIDs());
		
		return true;
	}
	
	@Transactional
	public Boolean deleteRenterProperty(UUID renterId, Long propertyId) {
		if (!repoRenter.existsById(renterId)) {
			throw new BadRequestException();
		}
		if (!repoProperty.existsById(propertyId)) {
			throw new ResourceNotFoundException();
		}
		
		svcPropertyImage.deletePropertyImages(propertyId);
		svcPropertyDoc.deletePropertyDocs(propertyId);
		svcPropertyAmenity.deletePropertyAmenities(propertyId);
		svcPropertyFacility.deletePropertyFacilities(propertyId);
		svcRecentSearch.deletePropertyRecentSearches(propertyId);
		
		repoProperty.deleteById(propertyId);
		
		return true;
	}
	
	public List<PropertyProjection> getPropertiesByRenterId(UUID renterId) {
		Renter renter = repoRenter.findById(renterId)
				.orElseThrow(() -> new ResourceNotFoundException());

		return repoProperty.findPropertyByRenterId(renter.getId());
	}
	
	/* Tenant Properties */
	@Transactional
	public Boolean createTenantProperty(UUID tenantId, Property property) {
		Tenant tenant = repoTenant.findById(tenantId)
				.orElseThrow(() -> new BadRequestException("Tenant does not exist"));
		
		property.setCreatedAt(LocalDate.now());
		property.setUpdatedAt(LocalDate.now());
		property.setTenant(tenant);

		Property savedProperty = repoProperty.save(property);
		svcPropertyImage.createPropertyImages(savedProperty, property.getPropertyImageURLs());
		svcPropertyDoc.createPropertyDocs(savedProperty, property.getPropertyDocURLs());
		svcPropertyAmenity.createPropertyAmenities(savedProperty, property.getPropertyAmenityIDs());
		svcPropertyFacility.createPropertyFacilities(savedProperty, property.getPropertyFacilityIDs());

		return true;
	}

	@Transactional
	public Boolean updateTenantProperty(UUID tenantId, Long propertyId, Property property) {
		if (!repoTenant.existsById(tenantId)) {
			throw new BadRequestException("Tenant does not exist");
		}
		
		Property existingProperty = repoProperty.findById(propertyId)
				.orElseThrow(() -> new ResourceNotFoundException());

		existingProperty.setTitle(property.getTitle());
		existingProperty.setDescription(property.getTitle());
		existingProperty.setPropertyType(property.getPropertyType());
		existingProperty.setRoomType(property.getRoomType());
		existingProperty.setRentalFees(property.getRentalFees());
		existingProperty.setAddress(property.getAddress());
		existingProperty.setPostalCode(property.getPostalCode());
		existingProperty.setFurnishment(property.getFurnishment());

		Property savedProperty = repoProperty.save(existingProperty);
		
		svcPropertyImage.updatePropertyImages(savedProperty, property.getPropertyImageURLs());
		svcPropertyAmenity.updatePropertyAmenities(savedProperty, property.getPropertyAmenityIDs());
		svcPropertyFacility.updatePropertyFacilities(savedProperty, property.getPropertyFacilityIDs());

		return true;
	}
	
	@Transactional
	public Boolean deleteTenantProperty(UUID tenantId, Long propertyId) {
		if (!repoTenant.existsById(tenantId)) {
			throw new BadRequestException();
		}
		if (!repoProperty.existsById(propertyId)) {
			throw new ResourceNotFoundException();
		}
		
		svcPropertyImage.deletePropertyImages(propertyId);
		svcPropertyDoc.deletePropertyDocs(propertyId);
		svcPropertyAmenity.deletePropertyAmenities(propertyId);
		svcPropertyFacility.deletePropertyFacilities(propertyId);
		svcRecentSearch.deletePropertyRecentSearches(propertyId);
		
		repoProperty.deleteById(propertyId);
		
		return true;
	}
	
	public List<PropertyProjection> getPropertiesByTenantId(UUID tenantId) {
		Tenant tenant = repoTenant.findById(tenantId)
				.orElseThrow(() -> new ResourceNotFoundException());

		return repoProperty.findPropertyByTenantId(tenant.getId());
	}

	/* Properties */
	public PropertyDetailProjection getPropertyById(Long id) {
		return repoProperty.findPropertyById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
	}

	public Page<PropertyProjection> getAllProperties(int pageNumber, int pageSize, String sortBy) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
		return repoProperty.findAllBy(pageable);
	}
	
	public Page<PropertyProjection> getSearchedProperties(
			PostType postType, String keyword,
			int pageNumber, int pageSize, String sortBy) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
		
		if (keyword == null || keyword.isEmpty()) {			
			return repoProperty.findByPostType(postType, pageable);
		}
		else {
			return repoProperty.findSearchedProperties(postType, keyword, keyword, keyword, keyword, pageable);
		}
	}

	@Transactional
	public Long increaseViewCount(Long id) {
		Property existingProperty = repoProperty.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());

		Long currentViewCount = existingProperty.getViewCount() + 1;
		repoProperty.updateViewCountById(id, currentViewCount);
		return currentViewCount;
	}
	
	public Long getTotalCount() {
		return repoProperty.count();
	}

	public Long countByPropertyTypeAndStatus(PostType postType, ApproveStatus status) {
		List<Property> properties = repoProperty.findAll();

		return properties.stream()
				.filter(p -> p.getPostType().equals(postType) && p.getApproveStatus().equals(status))
				.count();
	}

	public Double calculatePropertyTypePercentages(PostType postType, PropertyType propertyType) {
		Long totalProperties = repoProperty.findAll().stream()
				.filter(p -> p.getPostType().equals(postType))
				.count();

		Long count = repoProperty.findAll().stream()
				.filter(p -> p.getPostType().equals(postType) && p.getPropertyType().equals(propertyType))
				.count();

		Double percentage = ((double) count / totalProperties) * 100;

		return percentage;
	}

	public Double calculateRoomTypePercentages(PostType postType, RoomType roomType) {
		Long totalProperties = repoProperty.findAll().stream()
				.filter(p -> p.getPostType().equals(postType))
				.count();

		Long count = repoProperty.findAll().stream()
				.filter(p -> p.getPostType().equals(postType) && p.getRoomType().equals(roomType))
				.count();

		Double percentage = ((double) count / totalProperties) * 100;

		return percentage;

	}

	public Double calculateTenantTypePercentages(PostType postType, TenantType tenantType) {
		Long totalProperties = repoProperty.findAll().stream()
				.filter(p -> p.getPostType().equals(postType))
				.count();

		Long count = repoProperty.findAll().stream()
				.filter(p -> p.getPostType().equals(postType) && p.getTenantType().equals(tenantType))
				.count();

		Double percentage = ((double) count / totalProperties) * 100;

		return percentage;
	}

	public List<Property> getAllReportProperties() {
		return repoProperty.findAll();
	}

	public Boolean approveProperty(Long id) {
		Property property = repoProperty.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		try {
			property.setApproveStatus(ApproveStatus.APPROVED);
			property.setUpdatedAt(LocalDate.now());
			repoProperty.saveAndFlush(property);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean declineProperty(Long id) {
		Property property = repoProperty.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
		try {
			property.setApproveStatus(ApproveStatus.DECLINED);
			property.setStatus(Status.INACTIVE);
			property.setUpdatedAt(LocalDate.now());
			repoProperty.saveAndFlush(property);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
