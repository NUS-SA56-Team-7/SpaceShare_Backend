package com.spaceshare.backend.projections;

import java.time.LocalDateTime;
import java.util.UUID;

import com.spaceshare.backend.models.Comment;
import com.spaceshare.backend.models.Tenant;

public interface CommentProjection {
    
    String getComment();
    
    TenantProjection getTenant();
    interface TenantProjection {
        UUID getId();
        
        String getFirstName();
        
        String getLastName();
        
        String getPhotoUrl();
    }
    
    Comment getReplies();
    
    LocalDateTime getCommentedAt();
}

