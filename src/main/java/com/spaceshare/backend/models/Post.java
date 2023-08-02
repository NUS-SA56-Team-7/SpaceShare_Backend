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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spaceshare.backend.models.Enum.Furnishment;
import com.spaceshare.backend.models.Enum.PostType;
import com.spaceshare.backend.models.Enum.PropertyType;
import com.spaceshare.backend.models.Enum.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String postName;

    @Enumerated(EnumType.ORDINAL)
	@Column(columnDefinition = "TINYINT NOT NULL")
    private PropertyType propertyType;

    @NotNull
    private Double rentalFees;

    private String propertyDescription;

    @NotNull
    private String address;

    @NotNull
    private Integer postalCode;

    private String nearbyDesc;

    private Integer roomArea;

    @Column(columnDefinition = "TINYINT")
    private Integer bedroom;

    @Column(columnDefinition = "TINYINT")
    private Integer bathroom;

    @Column(columnDefinition = "TINYINT")
    private Integer numberOfTenants;

    @Enumerated(EnumType.ORDINAL)
	@Column(columnDefinition = "TINYINT NOT NULL")
    private Furnishment furnishment;

    @Enumerated(EnumType.ORDINAL)
	@Column(columnDefinition = "TINYINT NOT NULL")
    private PostType postType;

    @Enumerated(EnumType.ORDINAL)
	@Column(columnDefinition = "TINYINT NOT NULL")
    private Status status = Status.ACTIVE;

    @NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdAt;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate updatedAt;

    @ManyToOne(optional = true)
    @JsonIgnore
    private Renter renter;

    @ManyToOne(optional = true)
    @JsonIgnore
    private Tenant tenant;

    @OneToMany(targetEntity = WishList.class, mappedBy = "post")
    @JsonIgnore
    private List<WishList> wishLists;

    @OneToMany(targetEntity = PropertyImage.class, mappedBy = "post")
    @JsonIgnore
    private List<PropertyImage> propertyImages;

    @OneToMany(targetEntity = PropertyDoc.class, mappedBy = "post")
    @JsonIgnore
    private List<PropertyDoc> propertyDocs;

    @OneToMany(targetEntity = Appointment.class, mappedBy = "post")
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(targetEntity = PropertyFacility.class, mappedBy = "post")
    @JsonIgnore
    private List<PropertyFacility> propertyFacilities;

    @OneToMany(targetEntity = PropertyAmenity.class, mappedBy = "post")
    @JsonIgnore
    private List<PropertyAmenity> propertyAmenities;

    @OneToMany(targetEntity = ScamReport.class, mappedBy = "post")
    @JsonIgnore
    private List<ScamReport> scamReports;

    @OneToMany(targetEntity = PostComment.class, mappedBy = "post")
    @JsonIgnore
    private List<PostComment> postComments;
}
