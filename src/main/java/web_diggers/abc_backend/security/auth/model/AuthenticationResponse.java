package web_diggers.abc_backend.security.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthenticationResponse {
    private String status;
    private String message;

    private String token;
    private String role;
    private String firstName;
    private String lastName;
    private boolean enabled2FA;
    private String secretImageUri;
}
