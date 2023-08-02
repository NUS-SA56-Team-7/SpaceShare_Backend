package com.spaceshare.backend.models;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spaceshare.backend.models.Enum.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Facilities")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String facilityName;

    @Enumerated(EnumType.ORDINAL)
	@Column(columnDefinition = "TINYINT NOT NULL")
    private Status status = Status.ACTIVE;

    @NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdAt;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate updatedAt;

    @OneToMany(targetEntity = PropertyFacility.class, mappedBy = "facility")
    @JsonIgnore
    private List<PropertyFacility> propertyFacilities;
}
