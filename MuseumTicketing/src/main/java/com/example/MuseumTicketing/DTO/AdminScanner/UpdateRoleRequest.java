package com.example.MuseumTicketing.DTO.AdminScanner;

import com.example.MuseumTicketing.Model.Role;
import com.example.MuseumTicketing.Model.Role;
import lombok.Data;

@Data
public class UpdateRoleRequest {

    private Role newRole;
    private String newPassword;
}
