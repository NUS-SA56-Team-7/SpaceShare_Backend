package com.spaceshare.backend.models;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@Table(name = "PropertyImages")
public class PropertyImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String imageUrl;

    @Enumerated(EnumType.ORDINAL)
	@Column(columnDefinition = "TINYINT NOT NULL")
    private Status status = Status.ACTIVE;

    @NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdAt;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate updatedAt;

    @ManyToOne
    @JsonIgnore
    private Post post;
}
