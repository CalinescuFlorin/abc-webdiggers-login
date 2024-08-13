package web_diggers.abc_backend.security.password;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeResponse {
    private String status;
    private String message;
}
