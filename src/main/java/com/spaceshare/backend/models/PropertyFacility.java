package com.spaceshare.backend.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PropertyFacilities", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"property_id", "facility_id"})})
public class PropertyFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

	/*** Navigation Properties ***/
    @ManyToOne
    @JsonIgnore
    private Property property;

    @ManyToOne
    private Facility facility;
}
