package com.spaceshare.backend.services.implementations;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.OTP;
import com.spaceshare.backend.repos.OtpRepository;
import com.spaceshare.backend.services.OtpService;

@Service
public class OtpServiceImpl implements OtpService {
	
	/*** Repositories ***/
	@Autowired
	OtpRepository repoOtp;

	public Boolean createOtp(OTP otp) {
		try {
			Optional<OTP> optionalOtp = repoOtp.findById(otp.getId());
			if (optionalOtp.isEmpty()) {
				repoOtp.save(otp);
			}
			else {
				OTP existingOtp = optionalOtp.get();
				existingOtp.setOtp(otp.getOtp());
				
				repoOtp.save(existingOtp);
			}
			return true;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public OTP getOtp(UUID userId) {
		return repoOtp.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
}
