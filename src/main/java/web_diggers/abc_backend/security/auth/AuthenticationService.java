package web_diggers.abc_backend.security.auth;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import web_diggers.abc_backend.security.TwoFactorAuth.TwoFactorAuthService;
import web_diggers.abc_backend.security.auth.model.AuthenticationRequest;
import web_diggers.abc_backend.security.auth.model.AuthenticationResponse;
import web_diggers.abc_backend.security.auth.model.RegisterRequest;
import web_diggers.abc_backend.security.auth.model.VerificationRequest;
import web_diggers.abc_backend.security.email.ConfirmationTokenService;
import web_diggers.abc_backend.security.email.EmailSenderService;
import web_diggers.abc_backend.security.email.EmailValidator;
import web_diggers.abc_backend.security.jwt.JwtService;
import web_diggers.abc_backend.security.user.UserService;
import web_diggers.abc_backend.security.user.model.Role;
import web_diggers.abc_backend.security.user.model.User;
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
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    private final TwoFactorAuthService tfaService;
    private final AuthRequestValidator requestValidator;
    public AuthenticationResponse register(RegisterRequest request) throws Exception{
        String requestValidation = requestValidator.validateRegisterRequest(request);
        if(!requestValidation.isEmpty())
            throw new Exception(requestValidation);

        if(userService.getUser(request.getEmail()).isPresent())
            throw new Exception("Email address is already taken");

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled2FA(request.isEnabled2FA())
                .build();

        if(request.isEnabled2FA()){
            user.setCodeFor2FA(tfaService.generateNewCode());
        }

        userService.addUser(user);
        String confirmationToken = confirmationTokenService.generateToken(user);
        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + confirmationToken;
        emailSenderService.sendMail(user.getEmail(), "Email Confirmation", this.EmailBodyGenerator(user.getFirstName(), link));

        AuthenticationResponse response = AuthenticationResponse.builder()
                .status("success")
                .message("Account created")
                .token(confirmationToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().toString())
                .enabled2FA(user.isEnabled2FA())
                .build();

        if(request.isEnabled2FA())
            response.setSecretImageUri(tfaService.generateQrCodeImageUrl(user.getCodeFor2FA(), user.getEmail()));

        return response;
    }

    public AuthenticationResponse confirmEmail(String confirmationToken) throws Exception{
        String email = confirmationTokenService.extractEmail(confirmationToken);

        if(confirmationTokenService.isTokenExpired(confirmationToken))
            throw new Exception("Email confirmation time expired");

        User user = userService.getUser(email)
                .orElseThrow(() -> new Exception("No such account with this email"));

        user.setEnabled(true);
        userService.updateUser(user);

        return AuthenticationResponse.builder()
                .status("success")
                .message("Email confirmed")
                .token(confirmationToken)
                .firstName("")
                .lastName("")
                .role("")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
           request.getEmail(),
           request.getPassword()
        ));

        User user = userService.getUser(request.getEmail())
                .orElseThrow();

        if(!user.isEnabled()) {
            throw new Exception("Email was not confirmed");
        }

        if(user.isEnabled2FA()){
            return AuthenticationResponse.builder()
                    .status("2fa_pending")
                    .message("Login successful")
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
                .message("Login successful")
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
            throw new BadCredentialsException("Code is not correct.");
        }

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .status("success")
                .message("Validated 2FA code.")
                .token(jwtToken)
                .enabled2FA(user.isEnabled2FA())
                .build();
    }

    private String EmailBodyGenerator(String name, String link) {
        return "Hello " + name + ", \n\n" +
                "Please verify your email through this link: \n" + link +
                "\n\n\nKind regards, \nTeam Web Diggers";
    }
}
