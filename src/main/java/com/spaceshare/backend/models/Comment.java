package com.spaceshare.backend.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "Comments")
public class Comment extends Common {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "NVARCHAR(500) NOT NULL")
    private String comment;

    /*** Navigation Properties ***/
    @ManyToOne(optional = true)
    @JsonIgnore
    private Renter renter;

    @ManyToOne(optional = true)
    @JsonIgnore
    private Tenant tenant;

    @ManyToOne
    @JsonIgnore
    private Property property;
}