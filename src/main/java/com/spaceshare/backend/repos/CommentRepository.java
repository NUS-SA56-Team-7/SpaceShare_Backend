package com.spaceshare.backend.repos;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.Comment;
import com.spaceshare.backend.projections.CommentProjection;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
	List<CommentProjection> findByBaseCommentIsNullAndPropertyId(Long propertyId, Sort sort);
}
