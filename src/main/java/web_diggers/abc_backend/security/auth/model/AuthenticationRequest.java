package web_diggers.abc_backend.security.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Schema(
            description = "User email used at log in",
            name = "email",
            type = "string",
            example = "text@gmail.com")
    private String email;
    @Schema(
            description = "User password used at log in",
            name = "password",
            type = "string",
            example = "password12345")
    private String password;
}
