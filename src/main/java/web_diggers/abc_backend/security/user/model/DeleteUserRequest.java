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
public class DeleteUserRequest {
    @Schema(
            description = "User email",
            name = "email",
            type = "string",
            example = "text@gmail.com")
    private String email;
}
