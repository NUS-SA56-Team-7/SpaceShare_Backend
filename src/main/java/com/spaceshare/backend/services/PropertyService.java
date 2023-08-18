package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

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

	PropertyDetailProjection getPropertyById(Long id);

	List<PropertyProjection> getAllProperties();

	List<Property> getAllReportProperties();

	Long increaseViewCount(Long id);

	List<PropertyProjection> getPropertiesByRenterId(UUID renterId);

	Long countByPropertyTypeAndStatus(PostType postType, ApproveStatus approveStatus);

	Double calculatePropertyTypePercentages(PostType postType, PropertyType propertyType);

	Double calculateRoomTypePercentages(PostType postType, RoomType roomType);

	Double calculateTenantTypePercentages(PostType postType, TenantType tenantType);
}
