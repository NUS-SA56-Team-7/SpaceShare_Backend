package com.spaceshare.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.RecentSearch;

@Repository
public interface RecentSearchRepository extends JpaRepository<RecentSearch, Long> {

}