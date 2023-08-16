package com.spaceshare.backend.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spaceshare.backend.models.enums.ApproveStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "ScamReports")
public class ScamReport extends Common {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(columnDefinition = "TINYINT NOT NULL")
    private ApproveStatus status = ApproveStatus.PENDING;

	/*** Navigation Properties ***/
    @ManyToOne
    @JsonIgnore
    private Property property;

    @Transient
    private Integer pendingCount;

    @Transient
    private Integer approvedCount;
}
