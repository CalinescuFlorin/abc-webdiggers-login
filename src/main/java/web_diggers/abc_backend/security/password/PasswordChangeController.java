package web_diggers.abc_backend.security.password;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/password_change")
@RequiredArgsConstructor
@Tag(name="Password Change")
public class PasswordChangeController {
    private final PasswordChangeService passwordChangeService;

    @Operation(
            summary = "Send a password change email to the user.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Failure",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping("/change")
    private ResponseEntity<PasswordChangeResponse> sendChangePasswordMail(@RequestBody PasswordChangeRequest request) {
        try{
            return new ResponseEntity<>(passwordChangeService.sendChangePasswordMail(request), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(new PasswordChangeResponse("fail", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Change the password if the user forgets it.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Failure",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping("/confirm_forgotten")
    public ResponseEntity<PasswordChangeResponse> confirmForgottenPassword(@RequestBody PasswordChangeRequest request){
        try{
            return new ResponseEntity<>(passwordChangeService.changeForgottenPassword(request), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(
                    new PasswordChangeResponse("fail", e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Operation(
            summary = "Change the password if the user remembers the old password.",
            responses = {
                @ApiResponse(
                        description = "Success",
                        responseCode = "200"
                ),
                @ApiResponse(
                        description = "Failure",
                        responseCode = "400"
                )
        }
    )
    @PostMapping("/confirm_change")
    public ResponseEntity<PasswordChangeResponse> confirmChangedPassword(@RequestBody PasswordChangeRequest request){
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
