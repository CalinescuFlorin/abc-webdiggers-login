package web_diggers.abc_backend.Security.auth.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationRequest {
    private String email;
    private String code;

}