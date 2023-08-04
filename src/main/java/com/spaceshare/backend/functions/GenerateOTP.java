package com.spaceshare.backend.functions;

import java.util.Random;

public class GenerateOTP {

	// Attribute
	private int length;
	
	// Constructor
	public GenerateOTP(int length) {
		this.length = length;
	}
	
	// Methods
	public String get() {
		Random rand = new Random();
		String otp = "";
		
		for (int i = 0; i < this.length; i++) {
			otp += String.valueOf(rand.nextInt(10));
		}
		
		return otp;
	}
}
