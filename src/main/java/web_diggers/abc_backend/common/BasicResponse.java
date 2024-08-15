package web_diggers.abc_backend.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicResponse {
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
            example = "Response message")
    private String message;
}
