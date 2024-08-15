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
public class UpdateUserRoleEmailRequest {
    @Schema(
            description = "User's role",
            name = "role",
            type = "string"
    )
    private String role;

}
