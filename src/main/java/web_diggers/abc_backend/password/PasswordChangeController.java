package web_diggers.abc_backend.password;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web_diggers.abc_backend.security.auth.model.AuthenticationResponse;


@RestController
@RequestMapping("/api/v1/password_change")
@RequiredArgsConstructor
public class PasswordChangeController {
    private final PasswordChangeService passwordChangeService;

    @PostMapping("/change")
    private ResponseEntity<PasswordChangeResponse> sendMail(@RequestBody PasswordChangeRequest request) {
        try{
            return new ResponseEntity<>(passwordChangeService.sendChangePasswordMail(request), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(new PasswordChangeResponse("fail", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/confirm_change")
    public ResponseEntity<PasswordChangeResponse> confirmForgottenPassword(@RequestBody PasswordChangeRequest request){
        try{
            return new ResponseEntity<>(passwordChangeService.changePassword(request), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(
                    new PasswordChangeResponse("fail", e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
