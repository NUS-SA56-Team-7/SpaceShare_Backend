package com.spaceshare.backend.services;

import java.util.UUID;

import com.spaceshare.backend.models.OTP;

public interface OtpService {

	Boolean createOtp(OTP otp);
	
	OTP getOtp(UUID userId);
}
