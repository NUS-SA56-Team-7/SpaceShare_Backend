package com.spaceshare.backend.models;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "Tenants")
public class Tenant extends User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @OneToMany(targetEntity = WishList.class, mappedBy = "tenant")
    @JsonIgnore
    private List<WishList> wishLists;

    @OneToMany(targetEntity = Property.class, mappedBy = "tenant")
    @JsonIgnore
    private List<Property> posts;

    @OneToMany(targetEntity = Appointment.class, mappedBy = "tenant")
    @JsonIgnore
    private List<Appointment> appointments;
}
