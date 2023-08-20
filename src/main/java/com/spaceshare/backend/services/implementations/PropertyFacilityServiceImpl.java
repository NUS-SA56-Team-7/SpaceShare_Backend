package com.spaceshare.backend.services.implementations;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Facility;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.PropertyFacility;
import com.spaceshare.backend.repos.FacilityRepository;
import com.spaceshare.backend.repos.PropertyFacilityRepository;
import com.spaceshare.backend.services.PropertyFacilityService;

@Service
public class PropertyFacilityServiceImpl implements PropertyFacilityService {
    
	/*** Repositories ***/
	@Autowired
	PropertyFacilityRepository repoPropertyFacility;
	
	@Autowired
	FacilityRepository repoFacility;
	
	/*** Methods ***/
	@Transactional
	public List<PropertyFacility> createPropertyFacilities(Property property, List<Long> facilityIDs) {
		
		List<PropertyFacility> propertyFacilities = new ArrayList<PropertyFacility>();
		
		for (Long id: facilityIDs) {
			Facility facility = repoFacility.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException());
			
			PropertyFacility propertyFacility = new PropertyFacility();
			propertyFacility.setProperty(property);
			propertyFacility.setFacility(facility);
			
			propertyFacilities.add(repoPropertyFacility.save(propertyFacility));
		}
		
		return propertyFacilities;
	}
	
	@Transactional
	public List<PropertyFacility> updatePropertyFacilities(Property property, List<Long> facilityIDs) {
		
		List<PropertyFacility> propertyFacilities = new ArrayList<PropertyFacility>();
		
		for (Long id: facilityIDs) {
			Facility facility = repoFacility.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException());
			
			PropertyFacility propertyFacility = new PropertyFacility();
			propertyFacility.setProperty(property);
			propertyFacility.setFacility(facility);
			
			propertyFacilities.add(repoPropertyFacility.save(propertyFacility));
		}
		
		return propertyFacilities;
	}
	
	@Transactional
	public Boolean deletePropertyFacilities(Long propertyId) {
		List<PropertyFacility> propertyFacilities = repoPropertyFacility.findByPropertyId(propertyId);
		for (PropertyFacility facility: propertyFacilities) {
			repoPropertyFacility.deleteById(facility.getId());
		}
		
		return true;
	}
}
