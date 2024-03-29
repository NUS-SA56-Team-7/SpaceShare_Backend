package com.spaceshare.backend.repos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {

    boolean existsByEmail(String email);

    Optional<Admin> findByEmail(String email);
}
