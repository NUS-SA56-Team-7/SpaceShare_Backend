package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.enums.ApproveStatus;
import com.spaceshare.backend.models.enums.PostType;

import com.spaceshare.backend.models.enums.PropertyType;
import com.spaceshare.backend.models.enums.RoomMateType;
import com.spaceshare.backend.models.enums.RoomType;

import com.spaceshare.backend.repos.PropertyRepository;
import com.spaceshare.backend.services.PropertyImageService;
import com.spaceshare.backend.services.PropertyService;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {

	/*** Repositories ***/
	@Autowired
	PropertyRepository repoProperty;

	/*** Services ***/
	@Autowired
	PropertyImageService svcPropertyImage;

	/*** Methods ***/
	@Transactional
	public Boolean createProperty(Property property) {
		try {
			property.setCreatedAt(LocalDate.now());
			property.setUpdatedAt(LocalDate.now());

			Property savedProperty = repoProperty.save(property);
			System.out.println(property.getPropertyImageURLs());
			svcPropertyImage.createPropertyImages(savedProperty, property.getPropertyImageURLs());

			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public Property getPropertyById(Long id) {
		return repoProperty.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
	}

	public List<Property> getAllProperties() {
		return repoProperty.findAll();
	}

	@Override
	public Long countByPropertyTypeAndStatus(PostType postType, ApproveStatus status) {
		List<Property> properties = repoProperty.findAll();

		return properties.stream()
				.filter(p -> p.getPostType().equals(postType) && p.getApproveStatus().equals(status))
				.count();
	}

	@Override
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

	@Override
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

	@Override
	public Double calculateRoomMateTypePercentages(PostType postType, RoomMateType roomMateType) {
		Long totalProperties = repoProperty.findAll().stream()
				.filter(p -> p.getPostType().equals(postType))
				.count();

		Long count = repoProperty.findAll().stream()
				.filter(p -> p.getPostType().equals(postType) && p.getRoomMateType().equals(roomMateType))
				.count();

		Double percentage = ((double) count / totalProperties) * 100;

		return percentage;
	}
}
