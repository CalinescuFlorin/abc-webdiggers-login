package web_diggers.abc_backend.Security.auth;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import web_diggers.abc_backend.Security.TwoFactorAuth.TwoFactorAuthService;
import web_diggers.abc_backend.Security.auth.model.AuthenticationRequest;
import web_diggers.abc_backend.Security.auth.model.AuthenticationResponse;
import web_diggers.abc_backend.Security.auth.model.RegisterRequest;
import web_diggers.abc_backend.Security.auth.model.VerificationRequest;
import web_diggers.abc_backend.Security.jwt.JwtService;
import web_diggers.abc_backend.Security.user.UserService;
import web_diggers.abc_backend.Security.user.model.Role;
import web_diggers.abc_backend.Security.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TwoFactorAuthService tfaService;
    public AuthenticationResponse register(RegisterRequest request) throws Exception{
        if(userService.getUser(request.getEmail()).isPresent())
            throw new Exception("Email address is already taken.");

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled2FA(request.isEnabled2FA())
                .build();

        // if 2FA enabled => Generate code
        if(request.isEnabled2FA()){
            user.setCodeFor2FA(tfaService.generateNewCode());
        }
        userService.addUser(user);
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .status("success")
                .message("Account created.")
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().toString())
                .enabled2FA(user.isEnabled2FA())
                .secretImageUri(tfaService.generateQrCodeImageUrl(user.getCodeFor2FA(),user.getEmail()))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
           request.getEmail(),
           request.getPassword()
        ));

        User user = userService.getUser(request.getEmail())
                .orElseThrow();
        if(user.isEnabled2FA()){
            return AuthenticationResponse.builder()
                    .status("success")
                    .message("Login successful.")
                    .token("")
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole().toString())
                    .enabled2FA(true)
                    .build();
        }
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .status("success")
                .message("Login successful.")
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().toString())
                .enabled2FA(false)
                .build();
    }

    public AuthenticationResponse verifyCode(VerificationRequest verificationRequest) {
        User user = userService.getUser(verificationRequest.getEmail())
                .orElseThrow(()-> new EntityNotFoundException(String.format("No user found with %S", verificationRequest.getEmail()))
                );
        if(tfaService.isOtpNotValid(user.getCodeFor2FA(), verificationRequest.getCode())){
            throw new BadCredentialsException("Code is not correct");
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .enabled2FA(user.isEnabled2FA())
                .build();
    }
}
