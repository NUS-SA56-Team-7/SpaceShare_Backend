package com.spaceshare.backend.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.spaceshare.backend.models.Enum.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@MappedSuperclass
public class User extends Account {
	
	@NotNull
	private String firstName;

	@NotNull
	private String lastName;
	
	@NotNull
	private String identificationNumber;

	private String profileImageUrl;

	private String address;

	@NotNull
	private String phone;

	private LocalDate dateOfBirth;

	@Enumerated(EnumType.ORDINAL)
	@Column(columnDefinition = "TINYINT")
    private Status status = Status.ACTIVE;
}
