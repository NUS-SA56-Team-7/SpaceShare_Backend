package com.spaceshare.backend.models;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spaceshare.backend.models.enums.Furnishment;
import com.spaceshare.backend.models.enums.PostType;
import com.spaceshare.backend.models.enums.PropertyType;
import com.spaceshare.backend.models.enums.RoomType;
import com.spaceshare.backend.models.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "Properties")
public class Property extends Common {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

	@Column(columnDefinition = "TINYINT NOT NULL")
    private PropertyType propertyType;
	
	@Column(columnDefinition = "TINYINT NOT NULL")
    private RoomType roomType;

    @NotNull
    private Double rentalFees;

    private String description;

    @NotNull
    private String address;

    @NotNull
    private Integer postalCode;

    private String nearbyDesc;

    private Integer roomArea;

    @Column(columnDefinition = "TINYINT")
    private Integer numBedrooms;

    @Column(columnDefinition = "TINYINT")
    private Integer numBathrooms;

    @Column(columnDefinition = "TINYINT")
    private Integer numTenants;

	@Column(columnDefinition = "TINYINT NOT NULL")
    private Furnishment furnishment;

	@Column(columnDefinition = "TINYINT NOT NULL")
    private PostType postType;

	@Column(columnDefinition = "TINYINT NOT NULL")
    private Status status = Status.ACTIVE;

	/*** Navigation Properties ***/
    @ManyToOne(optional = true)
    @JsonIgnore
    private Renter renter;

    @ManyToOne(optional = true)
    @JsonIgnore
    private Tenant tenant;
    
    @OneToMany(targetEntity = WishList.class, mappedBy = "property")
    @JsonIgnore
    private List<WishList> wishLists;

    @OneToMany(targetEntity = PropertyImage.class, mappedBy = "property")
    @JsonIgnore
    private List<PropertyImage> propertyImages;

    @OneToMany(targetEntity = PropertyFacility.class, mappedBy = "property")
    @JsonIgnore
    private List<PropertyFacility> propertyFacilities;

    @OneToMany(targetEntity = PropertyAmenity.class, mappedBy = "property")
    @JsonIgnore
    private List<PropertyAmenity> propertyAmenities;
    
    @OneToMany(targetEntity = PropertyDoc.class, mappedBy = "property")
    @JsonIgnore
    private List<PropertyDoc> propertyDocs;
    
    @OneToMany(targetEntity = Appointment.class, mappedBy = "property")
    @JsonIgnore
    private List<Appointment> appointments;
    
    @OneToMany(targetEntity = Comment.class, mappedBy = "property")
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(targetEntity = ScamReport.class, mappedBy = "property")
    @JsonIgnore
    private List<ScamReport> scamReports;
}
