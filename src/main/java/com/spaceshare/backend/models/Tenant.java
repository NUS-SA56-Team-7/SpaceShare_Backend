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
import lombok.ToString;

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

	/*** Navigation Properties ***/
	@OneToMany(targetEntity = Property.class, mappedBy = "tenant")
	@ToString.Exclude
    @JsonIgnore
    private List<Property> properties;
	
    @OneToMany(targetEntity = Favourite.class, mappedBy = "tenant")
    @ToString.Exclude
    @JsonIgnore
    private List<Favourite> favourites;
    
    @OneToMany(targetEntity = Comment.class, mappedBy = "tenant")
    @ToString.Exclude
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(targetEntity = Appointment.class, mappedBy = "tenant")
    @ToString.Exclude
    @JsonIgnore
    private List<Appointment> appointments;
    
    @OneToMany(targetEntity = RecentSearch.class, mappedBy = "tenant")
    @ToString.Exclude
    @JsonIgnore
    private List<RecentSearch> recents;
}
