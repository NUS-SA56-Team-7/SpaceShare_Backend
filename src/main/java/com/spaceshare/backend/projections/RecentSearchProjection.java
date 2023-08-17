package com.spaceshare.backend.projections;

public interface RecentSearchProjection {
	
    Long getId();
    
    PropertyProjection getProperty();
    
    interface PropertyProjection {
        Long getId();
    }
}
