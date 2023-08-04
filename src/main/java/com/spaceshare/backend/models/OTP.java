package com.spaceshare.backend.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OTPs")
public class OTP {

	@Id
	@Type(type = "uuid-char")
	@Column(name = "user_id")
    private UUID id;
	
	private String otp;
}
