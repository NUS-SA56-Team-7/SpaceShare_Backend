package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.models.Admin;

public interface AdminService {

	List<Admin> getAllAdmins();

	Admin getAdminById(UUID id);

	Admin getAdminbyEmail(String email);

	Boolean createAdmin(Admin admin);

	Boolean updatePassword(UUID id, Admin admin);

	Boolean deleteAdmin(UUID id);

	Boolean isEmailExists(String email);
    
}
