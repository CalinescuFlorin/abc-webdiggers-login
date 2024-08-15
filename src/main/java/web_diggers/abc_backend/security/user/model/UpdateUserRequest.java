package web_diggers.abc_backend.security.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    @Schema(
            description = "User email",
            name = "email",
            type = "string",
            example = "text@gmail.com")
    private String email;
    @Schema(
            description = "User role",
            name = "role",
            type = "string",
            example = "ADMIN / USER / ARCHAEOLOGIST / BIOLOGIST")
    private String role;
}
