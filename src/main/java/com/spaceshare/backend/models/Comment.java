package com.spaceshare.backend.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "Comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "NVARCHAR(500) NOT NULL")
    private String comment;

    /*** Navigation Properties ***/
    @ManyToOne
    @JsonIgnore
    private Tenant tenant;

    @ManyToOne
    @JsonIgnore
    private Property property;
    
    @ManyToOne
    @JsonIgnore
    private Comment baseComment;

    @OneToMany(mappedBy = "baseComment")
    @JsonIgnore
    private List<Comment> replies;
}
