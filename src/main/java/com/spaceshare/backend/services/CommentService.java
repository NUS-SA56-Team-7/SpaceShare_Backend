package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.projections.CommentProjection;

public interface CommentService {
    
	Boolean createComment(UUID tenantId, Long propertyId, String comment);
	
	Boolean createReply(UUID tenantId, Long commentId, String reply);
	
	List<CommentProjection> getBaseComments(Long propertyId);
	
	Boolean deleteCommentAndReplies(Long commentId);
	
	Boolean deleteReply(Long replyId);
}
