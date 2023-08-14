package com.spaceshare.backend.services;

import java.util.List;

import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.enums.ApproveStatus;
import com.spaceshare.backend.models.enums.PostType;
import com.spaceshare.backend.models.enums.PropertyType;
import com.spaceshare.backend.models.enums.RoomMateType;
import com.spaceshare.backend.models.enums.RoomType;

import java.util.Map;

public interface PropertyService {

	Boolean createProperty(Property property);

	Property getPropertyById(Long id);

	List<Property> getAllProperties();

	Long countByPropertyTypeAndStatus(PostType postType, ApproveStatus approveStatus);

	Double calculatePropertyTypePercentages(PostType postType, PropertyType propertyType);

	Double calculateRoomTypePercentages(PostType postType, RoomType roomType);

	Double calculateRoomMateTypePercentages(PostType postType, RoomMateType roomMateType);
}
