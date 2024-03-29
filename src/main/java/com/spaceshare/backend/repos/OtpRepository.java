package com.spaceshare.backend.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spaceshare.backend.models.OTP;

@Repository
public interface OtpRepository extends JpaRepository<OTP, UUID> {

}
