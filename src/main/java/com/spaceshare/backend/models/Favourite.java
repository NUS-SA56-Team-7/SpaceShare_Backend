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
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Favourites", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"tenant_id", "property_id"})})
public class Favourite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	/*** Navigation Properties ***/
    @ManyToOne
    @JsonIgnore
    private Tenant tenant;

    @ManyToOne
    @JsonIgnore
    private Property property;
}
