package web_diggers.abc_backend.Security.auth;

import web_diggers.abc_backend.Security.auth.model.AuthenticationRequest;
import web_diggers.abc_backend.Security.auth.model.AuthenticationResponse;
import web_diggers.abc_backend.Security.auth.model.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web_diggers.abc_backend.Security.auth.model.VerificationRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {

        try{
            var response = service.register(request);
            if(request.isEnabled2FA()){
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.accepted().build();
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }

    }

//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) throws Exception {
//
//        var response = service.register(request);
//        if(request.isEnabled2FA()){
//            return ResponseEntity.ok(response);
//        }
//        return ResponseEntity.accepted().build();
//    }

//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request){
//        try{
//            return new ResponseEntity<>(service.register(request), HttpStatus.OK);
//
//        }catch(Exception e){
//            return new ResponseEntity<>(
//                    new AuthenticationResponse("fail", e.getMessage(), "", "", "", "", false, ""),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest request){

        try
        {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }



    }

    @RequestMapping("/logout")
    public void authenticateUser() {
        SecurityContextHolder.clearContext();
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest verificationRequest)
    {
        try{
            AuthenticationResponse response =service.verifyCode(verificationRequest);
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification failed: " + e.getMessage());
        }
    }
}


