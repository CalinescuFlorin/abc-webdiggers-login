package web_diggers.abc_backend.Security.auth;

import web_diggers.abc_backend.Security.auth.model.AuthenticationRequest;
import web_diggers.abc_backend.Security.auth.model.AuthenticationResponse;
import web_diggers.abc_backend.Security.auth.model.RegisterRequest;
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

    public AuthenticationResponse register(RegisterRequest request) throws Exception{
        if(userService.getUser(request.getEmail()).isPresent())
            throw new Exception("Email address is already taken.");

        User user = User.builder().firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userService.addUser(user);
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().toString())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
           request.getEmail(),
           request.getPassword()
        ));

        User user = userService.getUser(request.getEmail())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().toString())
                .build();
    }
}