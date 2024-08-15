package web_diggers.abc_backend.security.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import web_diggers.abc_backend.security.auth.model.AuthenticationRequest;
import web_diggers.abc_backend.security.auth.model.AuthenticationResponse;
import web_diggers.abc_backend.security.auth.model.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import web_diggers.abc_backend.security.auth.model.VerificationRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService service;

    @Operation(
            description = "Creates an account for the platform. If \"enabled2FA\" is true, it will also return a QR image for an authenticator app.",
            summary = "Endpoint for account registration",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
            }
    )
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
                    HttpStatus.BAD_REQUEST
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
