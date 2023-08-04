package com.spaceshare.backend.services;

public interface EmailService {

	void sendEmail(String recipient, String subject, String template);
}
