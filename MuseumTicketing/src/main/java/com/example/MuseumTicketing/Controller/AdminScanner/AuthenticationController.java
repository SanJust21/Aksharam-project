package com.example.MuseumTicketing.Controller.AdminScanner;


import com.example.MuseumTicketing.DTO.AdminScanner.CustomResponse;
import com.example.MuseumTicketing.DTO.AdminScanner.JwtAuthenticationResponse;
//import com.example.MuseumTicketing.DTO.AdminScanner.RefreshTokenRequest;
import com.example.MuseumTicketing.DTO.AdminScanner.SignInRequest;
import com.example.MuseumTicketing.DTO.AdminScanner.SignUpRequest;
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
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest){
        try {
            ResponseEntity<?> users = authenticationService.signup(signUpRequest);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException ex) {
            CustomResponse customResponse = new CustomResponse("Name and email are required", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customResponse);
        } catch (Exception ex) {
            CustomResponse customResponse = new CustomResponse("Error occurred during signup", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customResponse);
        }
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

    @PostMapping("/signin/scanner")
    public ResponseEntity<?> signinScanner(@RequestBody SignInRequest signInRequest){
        try {
            JwtAuthenticationResponse response = authenticationService.signinScanner(signInRequest);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException ex){
            CustomResponse customResponse = new CustomResponse("User is not a scanner!",HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(customResponse);
        }
    }
}
