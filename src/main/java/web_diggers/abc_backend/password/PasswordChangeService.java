package web_diggers.abc_backend.password;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import web_diggers.abc_backend.security.user.model.User;
import web_diggers.abc_backend.security.user.UserService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordChangeService {
    private final PasswordChangeTokenManager passwordChangeTokenManager;
    private final UserService userService;

    public PasswordChangeResponse changePassword(String email, String newPassword) throws Exception {
        Optional<web_diggers.abc_backend.security.user.model.User> userOptional = userService.getUser(email);


        if (userOptional.isEmpty()) {
            return new PasswordChangeResponse("fail", "User not found");
        }

        User user = userOptional.get();
        user.setPassword(newPassword);
        userService.updateUser(user);
        return new PasswordChangeResponse("success", "Password changed successfully");
    }
}
