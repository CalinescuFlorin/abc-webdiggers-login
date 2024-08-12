package web_diggers.abc_backend.Security.auth.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String status;
    private String message;

    private String token;
    private String role;
    private String firstName;
    private String lastName;
}
