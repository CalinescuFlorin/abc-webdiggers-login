package web_diggers.abc_backend.Security.auth;

import web_diggers.abc_backend.Security.auth.model.AuthenticationRequest;
import web_diggers.abc_backend.Security.auth.model.AuthenticationResponse;
import web_diggers.abc_backend.Security.auth.model.RegisterRequest;
import web_diggers.abc_backend.Security.email.ConfirmationTokenService;
import web_diggers.abc_backend.Security.email.EmailValidator;
import web_diggers.abc_backend.Security.jwt.JwtService;
import web_diggers.abc_backend.Security.user.UserService;
import web_diggers.abc_backend.Security.user.model.Role;
import web_diggers.abc_backend.Security.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

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
}
