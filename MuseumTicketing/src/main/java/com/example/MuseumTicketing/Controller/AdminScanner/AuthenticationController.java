package com.example.MuseumTicketing.Controller.AdminScanner;


import com.example.MuseumTicketing.Config.AppConfig;
import com.example.MuseumTicketing.DTO.AdminScanner.CustomResponse;
import com.example.MuseumTicketing.DTO.AdminScanner.JwtAuthenticationResponse;
//import com.example.MuseumTicketing.DTO.AdminScanner.RefreshTokenRequest;
import com.example.MuseumTicketing.DTO.AdminScanner.SignInRequest;
import com.example.MuseumTicketing.DTO.AdminScanner.SignUpRequest;
import com.example.MuseumTicketing.Model.Users;
import com.example.MuseumTicketing.Service.AdminScanner.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

   // @CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/signup")
    public ResponseEntity<Users> signup(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }

    //@CrossOrigin(origins = AppConfig.BASE_URL)
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest){
        try {
            JwtAuthenticationResponse response = authenticationService.signin(signInRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            CustomResponse customResponse = new CustomResponse("Invalid Username or Password", HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(customResponse);
        }
    }


//    @CrossOrigin(origins = AppConfig.BASE_URL)
//    @PostMapping("/refresh")
//    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
//        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
//    }
}
