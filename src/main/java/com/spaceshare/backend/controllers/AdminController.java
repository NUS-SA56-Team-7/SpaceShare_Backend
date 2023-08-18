package com.spaceshare.backend.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.spaceshare.backend.models.Admin;
import com.spaceshare.backend.models.JasperUser;
import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.models.enums.Status;
import com.spaceshare.backend.services.AdminService;
import com.spaceshare.backend.services.RenterService;
import com.spaceshare.backend.services.TenantService;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    /*** Services ***/
    @Autowired
    AdminService adminService;

    @Autowired
	RenterService svcRenter;

	@Autowired
	TenantService svcTenant;

    /*** API Methods ***/
    /**
     * find all admins
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getAllAdmins() {
        try {
            List<Admin> admins = adminService.getAllAdmins();
            return new ResponseEntity<>(admins, HttpStatus.OK);
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    /**
     * find by id
     * 
     * @pathvariable id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable UUID id) {
        try {
            Admin admin = adminService.getAdminById(id);
            return new ResponseEntity<>(admin, HttpStatus.FOUND);
        }
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    /**
     * create new admin
     * 
     * @requestbody admin
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@RequestBody @Validated Admin admin) {
        try {
            Boolean success = adminService.createAdmin(admin);
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
     * update password by id
     * 
     * @pathvariable id
     * @param password
     * @return
     */
    @PutMapping("/update_password/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable UUID id, @RequestParam String password) {
        try {
            Boolean success = adminService.updatePassword(id, password);
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
     * delete
     * 
     * @pathvariable id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable UUID id) {
        try {
            Boolean deleted = adminService.deleteAdmin(id);
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

    @GetMapping("/export/report")
	public ResponseEntity<?> exportToPDF(HttpServletResponse response) throws JRException {
		ClassPathResource resource = new ClassPathResource("src/main/resources/templates/SpaceShare_Report.jrxml");
		String filePath = resource.getPath();
		System.out.println(filePath);

		File file = Paths.get(filePath).toFile();
		String absolutePath = file.getAbsolutePath();
		System.out.println(absolutePath);

		ClassPathResource resource2 = new ClassPathResource("src/main/resources/templates/SpaceShare_Report.pdf");
		String filePath2 = resource2.getPath();
		System.out.println(filePath);

		File file2 = Paths.get(filePath2).toFile();
		String absolutePath2 = file2.getAbsolutePath();
		System.out.println(absolutePath2 + "\\SpaceShare_Report.pdf");

		List<Renter> renters = svcRenter.getAllRenters();
		List<Tenant> tenants = svcTenant.getAllTenants();

		// For Table
		JRBeanCollectionDataSource renterDataSource = new JRBeanCollectionDataSource(renters);
		JRBeanCollectionDataSource tenantDataSource = new JRBeanCollectionDataSource(tenants);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("renterDataset", renterDataSource);
		parameters.put("tenantDataset", tenantDataSource);

		// Pie chart
		Long activeRenters = svcRenter.getAllRenters().stream().filter(r -> r.getStatus() == Status.ACTIVE).count();
		Long inactiveRenters = svcRenter.getAllRenters().stream().filter(r -> r.getStatus() == Status.INACTIVE).count();
		Long activeTenants = svcTenant.getAllTenants().stream().filter(t -> t.getStatus() == Status.ACTIVE).count();
		Long inactiveTenants = svcTenant.getAllTenants().stream().filter(t -> t.getStatus() == Status.INACTIVE).count();

		List<JasperUser> userActivityDataList = new ArrayList<>();
		JasperUser activeR = new JasperUser("Active Renters", activeRenters.intValue());
		JasperUser inactiveR = new JasperUser("Inactive Renters", inactiveRenters.intValue());
		JasperUser activeT = new JasperUser("Active Tenants", activeTenants.intValue());
		JasperUser inactiveT = new JasperUser("Inactive Tenants", inactiveTenants.intValue());
		// Total Users
		Integer totalUsers = activeRenters.intValue() + inactiveRenters.intValue() + activeTenants.intValue() + inactiveTenants.intValue();
		parameters.put("totalUsers", totalUsers);

		userActivityDataList.add(activeR);
		userActivityDataList.add(inactiveR);
		userActivityDataList.add(activeT);
		userActivityDataList.add(inactiveT);
		JRBeanCollectionDataSource userActivityDataSource = new JRBeanCollectionDataSource(userActivityDataList);
		parameters.put("userActivityDataset", userActivityDataSource);

		JasperReport report = JasperCompileManager.compileReport(absolutePath);
		JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

		// Get pdf report from rest endpoint
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JRPdfExporter exporter = new JRPdfExporter();
		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		configuration.setCompressed(true);
		exporter.setConfiguration(configuration);
		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		exporter.exportReport();
		// return byteArrayOutputStream;

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_PDF);

		return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), httpHeaders, HttpStatus.OK);
	}
}
