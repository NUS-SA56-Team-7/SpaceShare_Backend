package com.spaceshare.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    
}
