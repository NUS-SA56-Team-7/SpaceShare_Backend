package com.spaceshare.backend.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.PostComment;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, UUID> {
    
}
