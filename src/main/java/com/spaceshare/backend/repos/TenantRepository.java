package com.spaceshare.backend.repos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID>{

	Optional<Tenant> findByEmail(String email);

    boolean existsByEmail(String email);
}
