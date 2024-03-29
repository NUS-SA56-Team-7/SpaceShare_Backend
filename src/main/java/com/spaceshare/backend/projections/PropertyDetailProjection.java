package com.spaceshare.backend.projections;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.models.Amenity;
import com.spaceshare.backend.models.PropertyAmenity;
import com.spaceshare.backend.models.PropertyDoc;
import com.spaceshare.backend.models.PropertyFacility;
import com.spaceshare.backend.models.PropertyImage;
import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.models.enums.ApproveStatus;
import com.spaceshare.backend.models.enums.Furnishment;
import com.spaceshare.backend.models.enums.PostType;
import com.spaceshare.backend.models.enums.PropertyType;
import com.spaceshare.backend.models.enums.RoomType;
import com.spaceshare.backend.models.enums.Status;
import com.spaceshare.backend.models.enums.TenantType;

public interface PropertyDetailProjection {
    
	Long getId();

    String getTitle();
    
    String getDescription();
    
    PropertyType getPropertyType();
	
    RoomType getRoomType();

    Double getRentalFees();

    String getAddress();

    String getPostalCode();

    Integer getRoomArea();
    
    Furnishment getFurnishment();

    Integer getNumBedrooms();

    Integer getNumBathrooms();

    Integer getNumTenants();
	
	String getNearbyDesc();

    PostType getPostType();
    
    TenantType getTenantType();

    Status getStatus();
    
    ApproveStatus getApproveStatus();
    
    LocalDate getAvailableOn();
    
    List<PropertyImage> getPropertyImages();
    
    List<PropertyDoc> getPropertyDocs();
    
    List<PropertyAmenity> getPropertyAmenities();
    
    List<PropertyFacility> getPropertyFacilities();
    
    RenterProjection getRenter();
    interface RenterProjection {
        UUID getId();
        
        String getFirstName();
        
        String getLastName();
        
        String getPhotoUrl();
        
        String getPhone();
    }
    
    TenantProjection getTenant();
    interface TenantProjection {
        UUID getId();
        
        String getFirstName();
        
        String getLastName();
        
        String getPhotoUrl();
        
        String getPhone();
    }
}
