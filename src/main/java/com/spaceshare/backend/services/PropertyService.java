package com.spaceshare.backend.services;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.enums.ApproveStatus;
import com.spaceshare.backend.models.enums.PostType;
import com.spaceshare.backend.models.enums.PropertyType;
import com.spaceshare.backend.models.enums.RoomType;
import com.spaceshare.backend.models.enums.TenantType;
import com.spaceshare.backend.projections.PropertyDetailProjection;
import com.spaceshare.backend.projections.PropertyProjection;

public interface PropertyService {

	Boolean createRenterProperty(UUID renterId, Property property);

	Boolean updateRenterProperty(UUID renterId, Long propertyId, Property property);
	
	Boolean deleteRenterProperty(UUID renterId, Long propertyId);
	
	Boolean createTenantProperty(UUID tenantId, Property property);

	Boolean updateTenantProperty(UUID tenantId, Long propertyId, Property property);
	
	Boolean deleteTenantProperty(UUID tenantId, Long propertyId);

	PropertyDetailProjection getPropertyById(Long id);

	Page<PropertyProjection> getAllProperties(int pageNumber, int pageSize, String sortBy);
	
	Page<PropertyProjection> getSearchedProperties(PostType postType, String title, int pageNumber, int pageSize, String sortBy);

	List<Property> getAllReportProperties();

	Long increaseViewCount(Long id);

	List<PropertyProjection> getPropertiesByRenterId(UUID renterId);
	
	List<PropertyProjection> getPropertiesByTenantId(UUID tenantId);
	
	Long getTotalCount();

	Long countByPropertyTypeAndStatus(PostType postType, ApproveStatus approveStatus);

	Double calculatePropertyTypePercentages(PostType postType, PropertyType propertyType);

	Double calculateRoomTypePercentages(PostType postType, RoomType roomType);

	Double calculateTenantTypePercentages(PostType postType, TenantType tenantType);

	Boolean approveProperty(Long id);

	Boolean declineProperty(Long id);
}
