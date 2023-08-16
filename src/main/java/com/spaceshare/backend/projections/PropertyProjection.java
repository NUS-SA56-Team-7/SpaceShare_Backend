package com.spaceshare.backend.projections;

import java.time.LocalDate;
import java.util.List;

import com.spaceshare.backend.models.PropertyImage;
import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.models.enums.Furnishment;
import com.spaceshare.backend.models.enums.PostType;
import com.spaceshare.backend.models.enums.PropertyType;
import com.spaceshare.backend.models.enums.RoomType;
import com.spaceshare.backend.models.enums.Status;

public interface PropertyProjection {
    
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

    Status getStatus();
    
    List<PropertyImage> getPropertyImages();
    
    Renter getRenter();
    
    LocalDate getCreatedAt();
    
    LocalDate getUpdatedAt();
}
