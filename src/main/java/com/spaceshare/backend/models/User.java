package com.spaceshare.backend.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.spaceshare.backend.models.enums.Status;

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
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotNull
	private String identificationNumber;

	@NotNull
	private String phone;

	private String address;

	private LocalDate dateOfBirth;
	
	private String photoUrl;

	@Column(columnDefinition = "TINYINT NOT NULL")
    private Status status = Status.ACTIVE;
}
