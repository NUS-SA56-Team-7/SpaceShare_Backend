package com.spaceshare.backend.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.spaceshare.backend.exceptions.BadRequestException;
import com.spaceshare.backend.exceptions.DuplicateResourceException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.functions.GenerateOTP;
import com.spaceshare.backend.models.Account;
import com.spaceshare.backend.models.Admin;
import com.spaceshare.backend.models.OTP;
import com.spaceshare.backend.models.Renter;
import com.spaceshare.backend.models.Tenant;
import com.spaceshare.backend.services.AdminService;
import com.spaceshare.backend.services.EmailService;
import com.spaceshare.backend.services.OtpService;
import com.spaceshare.backend.services.RenterService;
import com.spaceshare.backend.services.TenantService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	/*** Services ***/
	@Autowired
	RenterService svcRenter;
	
	@Autowired
	TenantService svcTenant;
	
	@Autowired
	EmailService svcEmail;
	
	@Autowired
	OtpService svcOtp;

	@Autowired
	AdminService svcAdmin;
	
	/*** Miscellaneous ***/
	@Autowired
	PasswordEncoder passwordEncoder;

	@Value("${MASTER_ID}")
	private String masterId;

	@Value("${MASTER_EMAIL}")
	private String masterEmail;

	@Value("${MASTER_PASSWORD}")
	private String masterPassword;
	
	@Value("${GOOGLE_RECAPTCHA_SECRET_KEY}")
	private String GOOGLE_RECAPTCHA_SECRET_KEY;

	/*** API Methods ***/
	/* RENTER */
	@PostMapping("/renter/register")
	public ResponseEntity<Renter> createRenter(@RequestBody Renter renter) {
		try {
			Boolean success = svcRenter.createRenter(renter);

			if (success) {
				return new ResponseEntity<Renter>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (DuplicateResourceException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PostMapping("/login/renter")
	public ResponseEntity<?> postLoginRenter(
			@RequestBody Account account,
			HttpSession session) {
		
		if (account.getEmail() == null || account.getPassword() == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		try {
			Renter existingRenter = svcRenter.getRenterByEmail(account.getEmail());
			if (passwordEncoder.matches(account.getPassword(), existingRenter.getPassword())) {
				session.setAttribute("userId", existingRenter.getId());
				
				return new ResponseEntity<>(existingRenter, HttpStatus.OK);
			}
			else {
				Map<String, String> resBody = Map.of("password", "Wrong password");
				return new ResponseEntity<>(resBody, HttpStatus.UNAUTHORIZED);
			}
		}
		catch (ResourceNotFoundException e) {
			Map<String, String> resBody = Map.of("email", "Account does not exist");
			return new ResponseEntity<>(resBody, HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			Map<String, String> resBody = Map.of("error", e.getMessage());
			return new ResponseEntity<>(resBody, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/resetpassword/renter")
	public ResponseEntity<?> getCheckRenterExists(
			@RequestBody Map<String, String> reqBody) {
		try {
			if (verifyRecaptcha(reqBody.get("recaptchaToken"))) {
				Renter existingRenter = svcRenter.getRenterByEmail(reqBody.get("email"));
				
				Map<String, String> resBody = new HashMap<String, String>();
				resBody.put("id", existingRenter.getId().toString());
				resBody.put("email", existingRenter.getEmail());
				
				return new ResponseEntity<>(resBody, HttpStatus.OK);	
			}
			else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/resetpassword/renter/{id}/otp")
	public ResponseEntity<?> postResetPasswordRenterOTP(
			@PathVariable UUID id) {
		try {
			Renter existingRenter = svcRenter.getRenterById(id);
			
			GenerateOTP generator = new GenerateOTP(6);
			String otp = generator.get();
			OTP newOtp = new OTP(existingRenter.getId(), otp);
			
			if (svcOtp.createOtp(newOtp)) {
				svcEmail.sendEmail(
						existingRenter.getEmail(),
						"Reset Password (SpaceShare)", otp);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/resetpassword/renter/{id}/otp/verify")
	public ResponseEntity<?> postResetPasswordRenterOTPVerify(
			@PathVariable UUID id,
			@RequestBody Map<String, String> reqBody) {
		
		try {
			OTP otp = svcOtp.getOtp(id);
			if (otp.getOtp().equals(reqBody.get("otp"))) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/* TENANT */
	@PostMapping("/register/tenant")
	public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
		try {

			Boolean success = svcTenant.saveTenant(tenant);
			if (success) {
				return new ResponseEntity<Tenant>(HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}catch (DuplicateResourceException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (BadRequestException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}
	
	@PostMapping("/login/tenant")
	public ResponseEntity<?> postLoginTenant(
			@RequestBody Account account,
			HttpSession session) {
		
		if (account.getEmail() == null || account.getPassword() == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		try {
			Tenant existingTenant = svcTenant.getTenantByEmail(account.getEmail());
			if (passwordEncoder.matches(account.getPassword(), existingTenant.getPassword())) {
				session.setAttribute("userId", existingTenant.getId());
				
				return new ResponseEntity<>(existingTenant, HttpStatus.OK);
			}
			else {
				Map<String, String> resBody = Map.of("password", "Wrong password");
				return new ResponseEntity<>(resBody, HttpStatus.UNAUTHORIZED);
			}
		}
		catch (ResourceNotFoundException e) {
			Map<String, String> resBody = Map.of("email", "Account does not exist");
			return new ResponseEntity<>(resBody, HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			Map<String, String> resBody = Map.of("error", e.getMessage());
			return new ResponseEntity<>(resBody, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/resetpassword/tenant")
	public ResponseEntity<?> getCheckTenantExists(
			@RequestBody Map<String, String> reqBody) {
		try {
			if (verifyRecaptcha(reqBody.get("recaptchaToken"))) {
				Tenant existingTenant = svcTenant.getTenantByEmail(reqBody.get("email"));
				
				Map<String, String> resBody = new HashMap<String, String>();
				resBody.put("id", existingTenant.getId().toString());
				resBody.put("email", existingTenant.getEmail());
				
				return new ResponseEntity<>(resBody, HttpStatus.OK);	
			}
			else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/resetpassword/tenant/{id}/otp")
	public ResponseEntity<?> postResetPasswordTenantOTP(
			@PathVariable UUID id) {
		try {
			Tenant existingTenant = svcTenant.getTenantById(id);
			
			GenerateOTP generator = new GenerateOTP(6);
			String otp = generator.get();
			OTP newOtp = new OTP(existingTenant.getId(), otp);
			
			if (svcOtp.createOtp(newOtp)) {
				svcEmail.sendEmail(
						existingTenant.getEmail(),
						"Reset Password (SpaceShare)", otp);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/resetpassword/tenant/{id}/otp/verify")
	public ResponseEntity<?> postResetPasswordTenantOTPVerify(
			@PathVariable UUID id,
			@RequestBody Map<String, String> reqBody) {
		
		try {
			OTP otp = svcOtp.getOtp(id);
			if (otp.getOtp().equals(reqBody.get("otp"))) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		}
		catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*** Private Methods ***/
	private Boolean verifyRecaptcha(String token) {
		RestTemplate restTemplate = new RestTemplate();
		
		String recaptchaVerifyBaseUrl = "https://www.google.com/recaptcha/api/siteverify";		
		String recaptchaVerifyQueryUrl = String.format(
				"?secret=%s&response=%s",
				GOOGLE_RECAPTCHA_SECRET_KEY, token);
		String recaptchaVerifyUrl = recaptchaVerifyBaseUrl + recaptchaVerifyQueryUrl;
		
		// Specify the expected type of the response
		ParameterizedTypeReference<Map<String, Object>> responseType =
		        new ParameterizedTypeReference<Map<String, Object>>() {};

		// Make the GET request and get the response as ResponseEntity
		ResponseEntity<Map<String, Object>> responseEntity =
				restTemplate.exchange(recaptchaVerifyUrl, HttpMethod.POST, null, responseType);

		// Get the body from the ResponseEntity
		Map<String, Object> response = responseEntity.getBody();
        
        return (Boolean)response.get("success");
	}

	/*** API Methods ***/
	/* Admin */
	@PostMapping("/login/master")
	public ResponseEntity<?> postLoginMaster(
			@RequestBody Account account,
			HttpSession session) {
		try {
			if (account.getEmail().equals(masterEmail) && account.getPassword().equals(masterPassword)) {
				session.setAttribute("userId", masterId);
				
				Map<String, String> resBody = new HashMap<String, String>();
				resBody.put("id", masterId);
				resBody.put("email", masterEmail);
				return new ResponseEntity<>(resBody, HttpStatus.OK);
			}
			else {
				Map<String, String> resBody = Map.of("password", "Wrong password");
				return new ResponseEntity<>(resBody, HttpStatus.UNAUTHORIZED);
			}
		}
		catch (ResourceNotFoundException e) {
			Map<String, String> resBody = Map.of("email", "Account does not exist");
			return new ResponseEntity<>(resBody, HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			Map<String, String> resBody = Map.of("error", e.getMessage());
			return new ResponseEntity<>(resBody, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/login/admin")
	public ResponseEntity<?> postLoginAdmin(
			@RequestBody Account account,
			HttpSession session) {
		try {
			Admin admin = svcAdmin.getAdminbyEmail(account.getEmail());
			if (passwordEncoder.matches(account.getPassword(), admin.getPassword())) {
				session.setAttribute("userId", admin.getId());
				
				Map<String, String> resBody = new HashMap<String, String>();
				resBody.put("id", admin.getId().toString());
				resBody.put("email", admin.getEmail());
				return new ResponseEntity<>(resBody, HttpStatus.OK);
			}
			else {
				Map<String, String> resBody = Map.of("password", "Wrong password");
				return new ResponseEntity<>(resBody, HttpStatus.UNAUTHORIZED);
			}
		}
		catch (ResourceNotFoundException e) {
			Map<String, String> resBody = Map.of("email", "Account does not exist");
			return new ResponseEntity<>(resBody, HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			Map<String, String> resBody = Map.of("error", e.getMessage());
			return new ResponseEntity<>(resBody, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
