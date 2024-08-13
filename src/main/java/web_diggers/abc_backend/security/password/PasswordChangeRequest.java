package web_diggers.abc_backend.security.password;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {
    private String token;
    private String email;
    private String oldPassword;
    private String newPassword;
}
