package web_diggers.abc_backend.security.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthenticationResponse {
    @Schema(
            description = "Status of the response",
            name = "status",
            type = "string",
            example = "success / fail")
    private String status;
    @Schema(
            description = "Message to send as response to authentication request",
            name = "message",
            type = "string",
            example = "Logged in successfully")
    private String message;
    @Schema(
            description = "Token generated for the user if it's a successful log in",
            name = "token",
            type = "string",
            example = "ilhgrueiwjdifehjow")
    private String token;
    @Schema(
            description = "Role of the logged in user",
            name = "role",
            type = "string",
            example = "ADMIN / USER / ARCHAEOLOGIST / BIOLOGIST")
    private String role;
    @Schema(
            description = "First name of the logged in user",
            name = "firstName",
            type = "string",
            example = "Nick")
    private String firstName;
    @Schema(
            description = "Last name of the logged in user",
            name = "lastName",
            type = "string",
            example = "Smith")
    private String lastName;
    @Schema(
            description = "If the user wants to use 2FA for authentication",
            name = "enabled2FA",
            type = "boolean",
            example = "true / false")
    private boolean enabled2FA;
    @Schema(
            description = "A QR image generated for authentication",
            name = "secretImageUri",
            type = "string",
            example = "data: image/png: ... ")
    private String secretImageUri;
}