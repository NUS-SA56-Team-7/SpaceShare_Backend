package com.spaceshare.backend.models;

import java.time.LocalDate;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@MappedSuperclass
public class Account {

	@NotNull
	private String email;
	
	@NotNull
	private String password;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdAt;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate updatedAt;
}
