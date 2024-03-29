package com.spaceshare.backend.repos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.Renter;

@Repository
public interface RenterRepository extends JpaRepository<Renter, UUID>{

	Optional<Renter> findByEmail(String email);

    boolean existsByEmail(String email);
}
