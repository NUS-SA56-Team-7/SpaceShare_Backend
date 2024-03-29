package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Comment;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.projections.CommentProjection;
import com.spaceshare.backend.repos.CommentRepository;
import com.spaceshare.backend.repos.PropertyRepository;
import com.spaceshare.backend.repos.TenantRepository;
import com.spaceshare.backend.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    
	/*** Repositories ***/
	@Autowired
	CommentRepository repoComment;
	
	@Autowired
	TenantRepository repoTenant;
	
	@Autowired
	PropertyRepository repoProperty;
	
	/*** Methods ***/
	public Boolean createComment(UUID tenantId, Long propertyId, String comment) {
		Tenant tenant = repoTenant.findById(tenantId)
				.orElseThrow(() -> new ResourceNotFoundException("Tenant does not exist"));
		Property property = repoProperty.findById(propertyId)
				.orElseThrow(() -> new ResourceNotFoundException("Property does not exist"));
		
		try {			
			Comment baseComment = new Comment();
			
			baseComment.setTenant(tenant);
			baseComment.setProperty(property);
			baseComment.setComment(comment);
			
			repoComment.save(baseComment);
			return true;
		}
		catch (DataIntegrityViolationException e) {
			throw new BadRequestException();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public Boolean createReply(UUID tenantId, Long commentId, String reply) {
		Tenant tenant = repoTenant.findById(tenantId)
				.orElseThrow(() -> new ResourceNotFoundException());
		Comment baseComment = repoComment.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		try {			
			Comment replyComment = new Comment();
//			replyComment.setCreatedAt(LocalDate.now());
//			replyComment.setUpdatedAt(LocalDate.now());
			
			replyComment.setTenant(tenant);
			replyComment.setBaseComment(baseComment);
			replyComment.setProperty(baseComment.getProperty());
			replyComment.setComment(reply);
			
			repoComment.save(replyComment);
			return true;
		}
		catch (DataIntegrityViolationException e) {
			throw new BadRequestException();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public List<CommentProjection> getBaseComments(Long propertyId) {
		if (repoProperty.findById(propertyId).isEmpty()) {
			throw new ResourceNotFoundException();
		}
		List<CommentProjection> baseComments =
				repoComment.findByBaseCommentIsNullAndPropertyId(
						propertyId,
						Sort.by(Sort.Direction.DESC, "commentedAt"));
		
		return baseComments;
	}
	
	@Transactional
	public Boolean deleteCommentAndReplies(Long commentId) {
		Comment baseComment = repoComment.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		try {
			for (Comment reply: baseComment.getReplies()) {
				repoComment.delete(reply);
			}
			repoComment.delete(baseComment);
			return true;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public Boolean deleteReply(Long replyId) {
		Comment reply = repoComment.findById(replyId)
				.orElseThrow(() -> new ResourceNotFoundException());
		
		try {
			repoComment.delete(reply);
			return true;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
