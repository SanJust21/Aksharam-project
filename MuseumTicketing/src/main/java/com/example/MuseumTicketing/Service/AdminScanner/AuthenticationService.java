package com.example.MuseumTicketing.Service.AdminScanner;

import com.example.MuseumTicketing.DTO.AdminScanner.JwtAuthenticationResponse;
//import com.example.MuseumTicketing.DTO.AdminScanner.RefreshTokenRequest;
import com.example.MuseumTicketing.DTO.AdminScanner.SignInRequest;
import com.example.MuseumTicketing.DTO.AdminScanner.SignUpRequest;
import com.example.MuseumTicketing.Model.Role;
import com.example.MuseumTicketing.Model.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AuthenticationService {

    ResponseEntity<?> signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SignInRequest signInRequest);

    //JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    ResponseEntity<?> updateEmployee(String employeeId, SignUpRequest signUpRequest);

    String deleteEmployee(String employeeId);

    String updateEmployeeRole(String employeeId, Role newRole, String newPassword);

    String updateScannerPassword(String employeeId, String newPassword);

    List<Users> getAllUsersByRole(Role role);

    String uploadImageToFileSystem(MultipartFile file, String employeeId) throws IOException;

    byte[] downloadImageFromFileSystem(String employeeId) throws IOException;

    List<Object> getAllTickets();

    List<Users> getAllUsersByRoles(List<Role> list);

    String deleteEmployeeByName(String name);

    JwtAuthenticationResponse signinScanner(SignInRequest signInRequest);
}
