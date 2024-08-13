package web_diggers.abc_backend.security.auth;

import web_diggers.abc_backend.security.auth.model.AuthenticationRequest;
import web_diggers.abc_backend.security.auth.model.AuthenticationResponse;
import web_diggers.abc_backend.security.auth.model.RegisterRequest;
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
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;

    public AuthenticationResponse register(RegisterRequest request) throws Exception{
        if(userService.getUser(request.getEmail()).isPresent())
            throw new Exception("Email address is already taken.");

        User user = User.builder().firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        if(!emailValidator.test(user.getEmail())) {
            throw new Exception("Invalid email.");
        }

        userService.addUser(user);
        String confirmationToken = confirmationTokenService.generateToken(user);
        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + confirmationToken;
        emailSenderService.sendMail(user.getEmail(), "Email Confirmation", this.EmailBodyGenerator(user.getFirstName(), link));

        return AuthenticationResponse.builder()
                .status("success")
                .message("Account created.")
                .token(confirmationToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().toString())
                .build();
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
                .message("Email confirmed.")
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
            throw new Exception("Email was not confirmed.");
        }

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .status("success")
                .message("Login successful.")
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().toString())
                .build();
    }

    private String EmailBodyGenerator(String name, String link) {
        return "Hello " + name + ", \n\n" +
                "Please verify your email through this link: \n" + link +
                "\n\n\nKind regards, \nTeam Web Diggers";
    }
}