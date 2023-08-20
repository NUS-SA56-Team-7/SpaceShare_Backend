package com.spaceshare.backend.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spaceshare.backend.exceptions.DuplicateResourceException;
import com.spaceshare.backend.exceptions.ResourceNotFoundException;
import com.spaceshare.backend.models.Admin;
import com.spaceshare.backend.repos.AdminRepository;
import com.spaceshare.backend.services.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    /*** Repositories ***/
    @Autowired
    AdminRepository adminRepository;

	/*** Miscellaneous ***/
	@Autowired
	PasswordEncoder passwordEncoder;

    /*** Methods ***/
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(UUID id) {
        return adminRepository.findById(id).orElse(null);
    }

    public Boolean createAdmin(Admin admin) {
        if (isEmailExists(admin.getEmail()) && admin.getEmail()!= null )
            throw new DuplicateResourceException();
        try {
            if (admin.getPassword() != null)
                    admin.setPassword(passwordEncoder.encode(admin.getPassword()));
                    
            admin.setCreatedAt(LocalDate.now());
            adminRepository.saveAndFlush(admin);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean updatePassword(UUID id, Admin admin) {
        Admin existingAdmin = adminRepository.findById(id).orElse(null);
        if (existingAdmin != null) {
            existingAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
            existingAdmin.setUpdatedAt(LocalDate.now());
            adminRepository.saveAndFlush(existingAdmin);
            return true;
        }
        return false;
    }

    public Boolean deleteAdmin(UUID id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Boolean isEmailExists(String email) {
        return adminRepository.existsByEmail(email);
    }

    public Admin getAdminbyEmail(String email) {
        return adminRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException());
    }
    
}
