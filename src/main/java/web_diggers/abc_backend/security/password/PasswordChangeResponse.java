package web_diggers.abc_backend.security.password;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeResponse {
    @Schema(
            description = "Status of the response",
            name = "status",
            type = "string",
            example = "success / fail")
    private String status;

    @Schema(
            description = "Detailed message explaining what happened",
            name = "message",
            type = "string",
            example = "success / fail")
    private String message;
}
