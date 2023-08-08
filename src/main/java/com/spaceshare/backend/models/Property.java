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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
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
import lombok.ToString;

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

    @NotBlank
    private String title;
    
    private String description;

	@Column(columnDefinition = "TINYINT NOT NULL")
    private PropertyType propertyType;
	
	@Column(columnDefinition = "TINYINT NOT NULL")
    private RoomType roomType;

    @NotNull
    private Double rentalFees;

    @NotBlank
    private String address;

    @NotBlank
    private String postalCode;

    private Integer roomArea;
    
    @Column(columnDefinition = "TINYINT NOT NULL")
    private Furnishment furnishment;

    @Column(columnDefinition = "TINYINT")
    private Integer numBedrooms;

    @Column(columnDefinition = "TINYINT")
    private Integer numBathrooms;

    @Column(columnDefinition = "TINYINT")
    private Integer numTenants;
	
	private String nearbyDesc;

	@Column(columnDefinition = "TINYINT NOT NULL")
    private PostType postType;

	@Column(columnDefinition = "TINYINT NOT NULL")
    private Status status = Status.ACTIVE;

	/*** Navigation Properties ***/
    @ManyToOne
    @JsonIgnore
    private Renter renter;

    @ManyToOne
    @JsonIgnore
    private Tenant tenant;
    
    @OneToMany(targetEntity = Favourite.class, mappedBy = "property")
    @ToString.Exclude
    private List<Favourite> favourites;

    @OneToMany(targetEntity = PropertyImage.class, mappedBy = "property")
    @ToString.Exclude
    private List<PropertyImage> propertyImages;
    
    @Transient
    private List<String> propertyImageURLs;

    @OneToMany(targetEntity = PropertyFacility.class, mappedBy = "property")
    @ToString.Exclude
    private List<PropertyFacility> propertyFacilities;

    @OneToMany(targetEntity = PropertyAmenity.class, mappedBy = "property")
    @ToString.Exclude
    private List<PropertyAmenity> propertyAmenities;
    
    @OneToMany(targetEntity = PropertyDoc.class, mappedBy = "property")
    @ToString.Exclude
    private List<PropertyDoc> propertyDocs;
    
    @OneToMany(targetEntity = Appointment.class, mappedBy = "property")
    @ToString.Exclude
    private List<Appointment> appointments;
    
    @OneToMany(targetEntity = Comment.class, mappedBy = "property")
    @ToString.Exclude
    private List<Comment> comments;

    @OneToMany(targetEntity = ScamReport.class, mappedBy = "property")
    @ToString.Exclude
    private List<ScamReport> scamReports;
}
