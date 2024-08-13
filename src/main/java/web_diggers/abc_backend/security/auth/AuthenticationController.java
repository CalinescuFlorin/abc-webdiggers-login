package web_diggers.abc_backend.security.auth;

import org.springframework.web.bind.annotation.*;
import web_diggers.abc_backend.security.auth.model.AuthenticationRequest;
import web_diggers.abc_backend.security.auth.model.AuthenticationResponse;
import web_diggers.abc_backend.security.auth.model.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

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
                    new AuthenticationResponse("fail", e.getMessage(), "", "", "", ""),
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
                    new AuthenticationResponse("fail", e.getMessage(), "", "", "", ""),
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
                    new AuthenticationResponse("fail", e.getMessage(), "", "", "", ""),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @RequestMapping("/logout")
    public void authenticateUser() {
        SecurityContextHolder.clearContext();
    }
}
