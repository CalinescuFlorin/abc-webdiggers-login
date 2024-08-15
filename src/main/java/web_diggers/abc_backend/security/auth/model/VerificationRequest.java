package web_diggers.abc_backend.security.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationRequest {
    @Schema(
            description = "User email",
            name = "email",
            type = "string",
            example = "text@gmail.com")
    private String email;
    @Schema(
            description = "Code for verification at log in",
            name = "code",
            type = "string",
            example = "132465")
    private String code;

}
