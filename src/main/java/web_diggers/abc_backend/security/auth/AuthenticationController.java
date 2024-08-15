package web_diggers.abc_backend.security.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import web_diggers.abc_backend.common.BasicResponse;
import web_diggers.abc_backend.doc.EmailConfirmationResponse;
import web_diggers.abc_backend.doc.TFAVerifyResponse;
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
            description = "Creates an account for the platform. If \"enabled2FA\" is true, it will also return a QR image for an authenticator app. The account will be disabled until the user activates it via email.",
            summary = "Endpoint for account registration",
            responses = {
                    @ApiResponse(
                            description = "Account created successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Error occurred while creating account",
                            responseCode = "400",
                            content = {
                                    @Content(mediaType = "application/json", schema =
                                    @Schema(implementation = BasicResponse.class))
                            }
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

    @Operation(
            description = "Confirms the email address and activates the account",
            summary = "Endpoint for account email confirmation",
            responses = {
                    @ApiResponse(
                            description = "Account has been activated",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json", schema =
                                    @Schema(implementation = EmailConfirmationResponse.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Error occurred while confirming account",
                            responseCode = "400",
                            content = {
                                    @Content(mediaType = "application/json", schema =
                                    @Schema(implementation = BasicResponse.class))
                            }
                    ),
            }
    )
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

    @Operation(
            description = "Authenticates an user into their account. Returns details about them, such as their name and role, and also returns an access token. If they have 2FA enabled, no token is returned, as it has to be received from the </auth/verify> endpoint.",
            summary = "Endpoint for login",
            responses = {
                    @ApiResponse(
                            description = "Credentials are correct, user is logged in successfully",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json", schema =
                                    @Schema(implementation = EmailConfirmationResponse.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Error occurred while logging into account",
                            responseCode = "400",
                            content = {
                                    @Content(mediaType = "application/json", schema =
                                    @Schema(implementation = BasicResponse.class))
                            }
                    ),
            }
    )
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

    @Operation(
            description = "Authenticates an user into their account. Returns the token for authentication.",
            summary = "Endpoint for 2FA code verification",
            responses = {
                    @ApiResponse(
                            description = "Code is correct, user is logged in successfully",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json", schema =
                                    @Schema(implementation = TFAVerifyResponse.class))
                            }
                    ),
                    @ApiResponse(
                            description = "Error occurred while verifying 2FA code",
                            responseCode = "400",
                            content = {
                                    @Content(mediaType = "application/json", schema =
                                    @Schema(implementation = BasicResponse.class))
                            }
                    ),
            }
    )
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
