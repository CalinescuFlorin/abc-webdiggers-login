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
public class RegisterRequest {
    @Schema(
            description = "First name of the user",
            name = "firstName",
            type = "string",
            example = "Nick")
    private String firstName;
    @Schema(
            description = "Last name of the user",
            name = "lastName",
            type = "string",
            example = "Smith")
    private String lastName;
    @Schema(
            description = "Password chosen by the user",
            name = "password",
            type = "string",
            example = "password123")
    private String password;
    @Schema(
            description = "Email chosen by the user",
            name = "email",
            type = "string",
            example = "test@gmail.com")
    private String email;
    @Schema(
            description = "If the user wants to use 2FA for authentication or not",
            name = "enabled2FA",
            type = "boolean",
            example = "true / false")
    private boolean enabled2FA;
}
