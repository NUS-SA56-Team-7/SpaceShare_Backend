package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.models.Appointment;

public interface AppointmentService {

    List<Appointment> getAllAppointmentsByProperty(Long propertyId);

    Appointment getAppointmentById(Long id);

    Boolean createAppointment(Long propertyId, List<Appointment> appointments);

    Boolean updateAppointment(Long id, Appointment appointment);

    Boolean deleteAppointment(Long id);

    Boolean acceptAppointment(Long id);

    Boolean declineAppointment(Long id);

    List<Appointment> getAllAvailableAppointmentsByProperty(Long propertyId);

    Boolean bookAppointment(Long id, UUID tenantId);

    List<Appointment> getAllAppointmentsBookedByTenant(UUID tenantId);

    Boolean cancelAppointment(Long id, UUID tenantId);
}
