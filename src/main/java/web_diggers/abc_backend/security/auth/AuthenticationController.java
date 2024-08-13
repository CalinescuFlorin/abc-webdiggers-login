package web_diggers.abc_backend.security.auth;

import org.springframework.web.bind.annotation.*;
import web_diggers.abc_backend.security.auth.model.AuthenticationRequest;
import web_diggers.abc_backend.security.auth.model.AuthenticationResponse;
import web_diggers.abc_backend.security.auth.model.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web_diggers.abc_backend.security.auth.model.VerificationRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request){
        try{
            return new ResponseEntity<>(service.register(request), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(
                    new AuthenticationResponse("fail", e.getMessage(), "", "", "", "", false, ""),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
  
    @GetMapping("/confirm")
    public ResponseEntity<AuthenticationResponse> confirmEmail(@RequestParam String token){
        try{
            return new ResponseEntity<>(service.confirmEmail(token), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(
                    new AuthenticationResponse("fail", e.getMessage(), "", "", "", "", false, ""),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request){
        try{
            return new ResponseEntity<>(service.authenticate(request), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(
                    new AuthenticationResponse("fail", e.getMessage(), "", "", "", "", false, ""),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @RequestMapping("/logout")
    public void logoutUser() {
        SecurityContextHolder.clearContext();
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthenticationResponse> verify2FACode(@RequestBody VerificationRequest verificationRequest)
    {
        try{
            return new ResponseEntity<>(service.verifyCode(verificationRequest), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(
                    new AuthenticationResponse("fail", e.getMessage(), "", "", "", "", false, ""),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}


