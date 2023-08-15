package com.spaceshare.backend.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Appointment;
import com.spaceshare.backend.services.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    /*** Services ***/
    @Autowired
    AppointmentService appointmentService;

    /*** API Methods for Renter ***/
    /**
     * find all appointments by a Property
     * 
     * @pathvariable propertyId
     * @return
     */
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<?> getAllAppointmentsByProperty(@PathVariable Long propertyId) {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointmentsByProperty(propertyId);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    /**
     * find by appointment id and appointment date
     * 
     * @pathvariable id
     * @param appointment date
     * @return
     */
    @GetMapping("/property/{propertyId}/appointment_date")
    public ResponseEntity<?> getAppointmentByDate(@PathVariable("propertyId") Long propertyId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentByDate(propertyId, appointmentDate);
            return new ResponseEntity<>(appointments, HttpStatus.FOUND);
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    /**
     * find by appointment id
     * 
     * @pathvariable id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            return new ResponseEntity<>(appointment, HttpStatus.FOUND);
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    /**
     * create new appointment on that Property
     * 
     * @pathvariable propertyId
     * @requestbody appointments
     * @return
     */
    @PostMapping("/property/{propertyId}/create")
    public ResponseEntity<?> createAppointment(@PathVariable Long propertyId, @RequestBody @Validated List<Appointment> appointments) {
        try {
            Boolean success = appointmentService.createAppointment(propertyId, appointments);
            if (success) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * update appointment by id
     * 
     * @pathvariable id
     * @requestbody appointment
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody @Validated Appointment appointment) {
        try {
            Boolean success = appointmentService.updateAppointment(id, appointment);
            if (success) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * delete appointment
     * 
     * @pathvariable id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        try {
            Boolean deleted = appointmentService.deleteAppointment(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * accept the tenant booking on that appointment
     * 
     * @pathvariable id
     * @return
     */
    @PutMapping("/accept/{id}")
    public ResponseEntity<?> acceptAppointment(@PathVariable Long id) {
        try {
            Boolean accepted = appointmentService.acceptAppointment(id);
            if (accepted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * decline the tenant booking on that appointment
     * 
     * @pathvariable id
     * @return
     */
    @PutMapping("/decline/{id}")
    public ResponseEntity<?> declineAppointment(@PathVariable Long id) {
        try {
            Boolean declined = appointmentService.declineAppointment(id);
            if (declined) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*** API Methods for Tenant ***/
    /**
     * find all available appointments from a Property
     * 
     * @pathvariable propertyId
     * @return
     */
    @GetMapping("/property/{propertyId}/available")
    public ResponseEntity<?> getAllAvailableAppointmentsByProperty(@PathVariable Long propertyId) {
        try {
            List<Appointment> appointments = appointmentService.getAllAvailableAppointmentsByProperty(propertyId);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    /**
     * book appointment by a Tenant
     * 
     * @pathvariable id, tenantId
     * @return
     */
    @PutMapping("/tenant/{tenantId}/book/{id}")
    public ResponseEntity<?> bookAppointment(@PathVariable("id") Long id, @PathVariable("tenantId") UUID tenantId) {
        try {
            Boolean success = appointmentService.bookAppointment(id, tenantId);
            if (success) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * find all appointments that a tenant had booked
     * 
     * @pathvariable tenantId
     * @return
     */
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<?> getAllAppointmentsBookedByTenant(@PathVariable("tenantId") UUID tenantId) {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointmentsBookedByTenant(tenantId);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    /**
     * cancel appointment by a tenant itself
     * 
     * @pathvariable tenant_id
     * @return
     */
    @PutMapping("/tenant/{tenantId}/cancel/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable("id") Long id, @PathVariable("tenantId") UUID tenantId) {
        try {
            Boolean canceled = appointmentService.cancelAppointment(id, tenantId);
            if (canceled) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
