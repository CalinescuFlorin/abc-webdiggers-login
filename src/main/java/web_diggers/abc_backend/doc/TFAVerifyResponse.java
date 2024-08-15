package web_diggers.abc_backend.doc;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TFAVerifyResponse {
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
            example = "Validated 2FA code. / <error>")
    private String message;

    @Schema(
            description = "JWT Authentication token",
            name = "token",
            type = "string",
            example = "<token> / <error>")
    private String token;

    @Schema(
            description = "User has 2 Factor Authentication enabled",
            name = "enabled2FA",
            type = "boolean",
            example = "true / false")
    private boolean enabled2FA;
}
