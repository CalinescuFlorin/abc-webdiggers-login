package web_diggers.abc_backend.security.auth;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import web_diggers.abc_backend.security.auth.model.AuthenticationRequest;
import web_diggers.abc_backend.security.auth.model.RegisterRequest;
import web_diggers.abc_backend.security.auth.model.VerificationRequest;
import web_diggers.abc_backend.security.email.EmailValidator;

@Service
@RequiredArgsConstructor
public class AuthRequestValidator {
    private final EmailValidator emailValidator;

    public String validateRegisterRequest(RegisterRequest request){
        String result = "";

        if(StringUtils.isBlank(request.getFirstName()))
            result += "First name is missing;";

        if(StringUtils.isBlank(request.getLastName()))
            result += "Last name is missing;";

        if(StringUtils.isBlank(request.getPassword()))
            result += "Password is missing;";

        if(StringUtils.isBlank(request.getEmail()))
            result += "Email is missing;";
        else if(!emailValidator.test(request.getEmail()))
            result += "Invalid email;";

        return result;
    }

    public String validateAuthenticationRequest(AuthenticationRequest request) {
        String result = "";

        if(StringUtils.isBlank(request.getPassword()))
            result += "Password is missing;";

        if(StringUtils.isBlank(request.getEmail()))
            result += "Email is missing;";

        else if(!emailValidator.test(request.getEmail()))
            result += "Invalid email;";

        return result;
    }

    public String validateVerificationRequest(VerificationRequest request) {
        String result = "";

        if(StringUtils.isBlank(request.getCode()))
            result += "Code is missing;";

        if(StringUtils.isBlank(request.getEmail()))
            result += "Email is missing;";

        else if(!emailValidator.test(request.getEmail()))
            result += "Invalid email;";

        return result;
    }
}
