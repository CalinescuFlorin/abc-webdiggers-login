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
