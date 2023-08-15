package com.spaceshare.backend.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin getAdminById(UUID id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean createAdmin(Admin admin) {
        try {
            if (!isEmailExists(admin.getEmail()) && admin.getPassword() != null)
                    admin.setPassword(passwordEncoder.encode(admin.getPassword()));
                
                adminRepository.saveAndFlush(admin);
                return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean updatePassword(UUID id, String password) {
        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin != null) {
            admin.setPassword(passwordEncoder.encode(password));
            adminRepository.saveAndFlush(admin);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteAdmin(UUID id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean isEmailExists(String email) {
        return adminRepository.existsByEmail(email);
    }

    @Override
    public Admin getAdminbyEmail(String email) {
        return adminRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException());
    }
    
}
