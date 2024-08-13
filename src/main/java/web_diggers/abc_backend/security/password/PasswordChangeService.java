package web_diggers.abc_backend.security.password;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web_diggers.abc_backend.security.email.EmailSenderService;
import web_diggers.abc_backend.security.user.UserRepository;
import web_diggers.abc_backend.security.user.model.User;
import web_diggers.abc_backend.security.user.UserService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordChangeService {
    private final PasswordChangeTokenManager passwordChangeTokenManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

    public PasswordChangeResponse sendChangePasswordMail(PasswordChangeRequest request) throws Exception {
        String email = request.getEmail();

        Optional<User> userOptional = userService.getUser(email);

        if (userOptional.isEmpty()) {
            return new PasswordChangeResponse("fail", "User not found");
        }

        User user = userOptional.get();

        String confirmationToken = passwordChangeTokenManager.generateToken(user);
        emailSenderService.sendMail(user.getEmail(), "Change password", "Token: " + confirmationToken);

        return new PasswordChangeResponse("success", "Email sent");
    }

    public PasswordChangeResponse changeForgottenPassword(PasswordChangeRequest request) throws Exception {

        String email = request.getEmail();
        String newPassword = request.getNewPassword();
        String token = request.getToken();

        if(passwordChangeTokenManager.isTokenExpired(token)){
            return new PasswordChangeResponse("fail", "Token expired");
        }

        Optional<User> userOptional = userService.getUser(email);

        if (userOptional.isEmpty()) {
            return new PasswordChangeResponse("fail", "User not found");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        return new PasswordChangeResponse("success", "Password changed successfully");
    }

    public PasswordChangeResponse changePassword(PasswordChangeRequest request) throws Exception {

        String email = request.getEmail();
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();
        String token = request.getToken();

        if(passwordChangeTokenManager.isTokenExpired(token)){
            return new PasswordChangeResponse("fail", "Token expired");
        }

        Optional<User> userOptional = userService.getUser(email);

        if (userOptional.isEmpty()) {
            return new PasswordChangeResponse("fail", "User not found");
        }

        User user = userOptional.get();



        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return new PasswordChangeResponse("fail", "Old password does not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        return new PasswordChangeResponse("success", "Password changed successfully");
    }
}
