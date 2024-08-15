package web_diggers.abc_backend.security.password;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import web_diggers.abc_backend.security.auth.model.RegisterRequest;
import web_diggers.abc_backend.security.email.EmailValidator;

@Service
@RequiredArgsConstructor
public class PasswordChangeRequestValidator {
    protected final EmailValidator emailValidator;

    public String validatePasswordChangeRequestEmail(PasswordChangeRequest request){
        String result = "";

        if(StringUtils.isBlank(request.getEmail()))
            result += "Email is missing;";
        else if(!emailValidator.test(request.getEmail()))
            result += "Invalid email;";

        return result;
    }

    public String validatePasswordChangeRequestNewPassword(PasswordChangeRequest request){
        String result = "";

        if(StringUtils.isBlank(request.getNewPassword()))
            result += "New password is missing;";

        return result;
    }

    public String validatePasswordChangeRequestOldPassword(PasswordChangeRequest request){
        String result = "";

        if(StringUtils.isBlank(request.getOldPassword()))
            result += "Old password is missing;";

        return result;
    }

    public String validatePasswordChangeRequestForgotten(PasswordChangeRequest request){
        String result = "";

        result += validatePasswordChangeRequestEmail(request);

        result += validatePasswordChangeRequestNewPassword(request);

        return result;
    }

    public String validatePasswordChangeRequestManual(PasswordChangeRequest request){
        String result = "";

        result += validatePasswordChangeRequestEmail(request);

        result += validatePasswordChangeRequestOldPassword(request);

        result += validatePasswordChangeRequestNewPassword(request);

        return result;
    }
}
