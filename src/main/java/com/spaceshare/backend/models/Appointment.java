package com.spaceshare.backend.models;

import java.sql.Time;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spaceshare.backend.models.enums.ApproveStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate appointmentDate;

    @NotNull
    private Time startTime;

    @NotNull
    private Time endTime;

	@Column(columnDefinition = "TINYINT")
    private ApproveStatus status;

	/*** Navigation Properties ***/
    @ManyToOne(optional = true)
    @JsonIgnore
    private Tenant tenant;

    @ManyToOne
    @JsonIgnore
    private Property property;
}
