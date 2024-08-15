package web_diggers.abc_backend.doc;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeEmailRequest {
    @Schema(
            description = "User's email associated with the request",
            name = "email",
            type = "string"
    )
    private String email;
}
