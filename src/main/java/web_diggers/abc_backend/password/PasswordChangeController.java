package web_diggers.abc_backend.password;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/password_change")
@RequiredArgsConstructor
public class PasswordChangeController {
    private final PasswordChangeService passwordChangeService;

    @PostMapping("/change")
    private ResponseEntity<PasswordChangeResponse> changePassword(@RequestBody PasswordChangeRequest request) {
        try{
            return new ResponseEntity<>(passwordChangeService.changePassword(request.getEmail(), request.getNewPassword()), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(new PasswordChangeResponse("fail", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
