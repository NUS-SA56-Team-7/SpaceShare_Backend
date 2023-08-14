package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Appointment;
import com.spaceshare.backend.models.Property;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.models.enums.ApproveStatus;
import com.spaceshare.backend.repos.AppointmentRepository;
import com.spaceshare.backend.repos.PropertyRepository;
import com.spaceshare.backend.repos.TenantRepository;
import com.spaceshare.backend.services.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    /*** Repositories ***/
    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    TenantRepository tenantRepository;

    /*** Methods ***/
    @Override
    public List<Appointment> getAllAppointmentsByProperty(Long propertyId) {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .filter(appointment -> 
                    appointment.getProperty().getId().equals(propertyId)
                ).collect(Collectors.toList());
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    @Transactional
    public Boolean createAppointment(Long propertyId, List<Appointment> appointments) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new ResourceNotFoundException());
        try {
            for (Appointment appointment : appointments) {
                appointment.setProperty(property);
                appointmentRepository.saveAndFlush(appointment);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean updateAppointment(Long id, Appointment appointment) {
        Appointment updateAppointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        try {
            updateAppointment.setAppointmentDate(appointment.getAppointmentDate());
            updateAppointment.setStartTime(appointment.getStartTime());
            updateAppointment.setEndTime(appointment.getEndTime());

            appointmentRepository.saveAndFlush(updateAppointment);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        try {
            appointmentRepository.delete(appointment);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
			return false;
        }
    }

    @Override
    public Boolean acceptAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        try {
            appointment.setStatus(ApproveStatus.APPROVED);
            appointmentRepository.saveAndFlush(appointment);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
			return false;
        }
    }

    @Override
    public Boolean declineAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        try {
            appointment.setStatus(ApproveStatus.DECLINED);
            appointmentRepository.saveAndFlush(appointment);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
			return false;
        }
    }

    @Override
    public List<Appointment> getAllAvailableAppointmentsByProperty(Long propertyId) {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .filter(appointment -> 
                    appointment.getProperty().getId().equals(propertyId) &&
                    appointment.getTenant() == null
                ).collect(Collectors.toList());
    }

    @Override
    public Boolean bookAppointment(Long id, UUID tenantId) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new ResourceNotFoundException());
        try {
            if (!checkAlreadyBooked(appointment.getProperty().getId(), appointment.getAppointmentDate(), tenantId)) {
                appointment.setTenant(tenant);
                appointment.setStatus(ApproveStatus.PENDING);
                appointmentRepository.saveAndFlush(appointment);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
			return false;
        }
    }

    @Override
    public List<Appointment> getAllAppointmentsBookedByTenant(UUID tenantId) {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .filter(appointment ->
                    appointment.getTenant() != null &&
                    appointment.getTenant().getId().equals(tenantId)
                ).collect(Collectors.toList());
    }

    @Override
    public Boolean cancelAppointment(Long id, UUID tenantId) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        try {
            appointment.setTenant(null);
            appointment.setStatus(null);
            appointmentRepository.saveAndFlush(appointment);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
			return false;
        }
    }

    private Boolean checkAlreadyBooked(Long propertyId, LocalDate appointmentDate, UUID tenantId) {
        List<Appointment> appointments = appointmentRepository.findAll().stream()
                                            .filter(appointment -> 
                                                        appointment.getProperty().getId().equals(propertyId) &&
                                                        appointment.getAppointmentDate().equals(appointmentDate) &&
                                                        appointment.getTenant() != null
                                                    ).collect(Collectors.toList());
        for (Appointment appointment : appointments) {
            if (appointment.getTenant().getId().equals(tenantId)) {
                return true;
            }
        }
        return false;
    }
}
