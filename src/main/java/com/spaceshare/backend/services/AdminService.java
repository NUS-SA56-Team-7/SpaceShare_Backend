package com.spaceshare.backend.services;

import java.util.List;
import java.util.UUID;

import com.spaceshare.backend.models.Admin;

public interface AdminService {

        List<Admin> getAllAdmins();

        Admin getAdminById(UUID id);

        Boolean createAdmin(Admin admin);

        Boolean updatePassword(UUID id, String password);

        Boolean deleteAdmin(UUID id);

        Boolean isEmailExists(String email);
    
}
