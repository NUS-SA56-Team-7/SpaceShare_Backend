package com.spaceshare.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
