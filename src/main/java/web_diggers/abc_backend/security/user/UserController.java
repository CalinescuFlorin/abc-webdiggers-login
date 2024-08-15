package web_diggers.abc_backend.security.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import web_diggers.abc_backend.common.BasicResponse;
import web_diggers.abc_backend.doc.PasswordChangeEmailRequest;
import web_diggers.abc_backend.doc.UpdateUserRoleEmailRequest;
import web_diggers.abc_backend.security.user.model.DeleteUserRequest;
import web_diggers.abc_backend.security.user.model.UpdateUserRequest;
import web_diggers.abc_backend.security.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name="User Management")
public class UserController {
    @Autowired
    UserService service;

    @Operation(
            description = "Returns a list of all accounts and their details",
            summary = "Fetch list of users",
            responses = {
                    @ApiResponse(
                            description = "Fetch list of users",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }

    @Operation(
            description = "Change an user's role, given their Id",
            summary = "Change user role by Id",
            responses = {
                    @ApiResponse(
                            description = "User role changed successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Error occurred while updating role / account not found",
                            responseCode = "400"
                    ),
            }
    )
    @PutMapping("/users/{id}")
    public ResponseEntity<BasicResponse> updateUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(mediaType = "application/json", schema =
            @Schema(implementation = UpdateUserRoleEmailRequest.class))
    }) UpdateUserRequest request, @PathVariable(name="id")int id){
        try{
            service.changeUserRole(id, request.getRole());

            return new ResponseEntity<>(new BasicResponse("success", "Updated user."), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(new BasicResponse("fail", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            description = "Delete an user, given their Id",
            summary = "Delete user by Id",
            responses = {
                    @ApiResponse(
                            description = "User deleted successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Error occurred while deleting user / account not found",
                            responseCode = "400"
                    ),
            }
    )
    @DeleteMapping ("/users/{id}")
    public ResponseEntity<BasicResponse> deleteUser(@PathVariable(name="id")int id){
        try{
            service.deleteUser(id);

            return new ResponseEntity<>(new BasicResponse("success", "Deleted user with id " + id), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(new BasicResponse("fail", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            description = "Change an user's role, given their email address",
            summary = "Change user role by email address",
            responses = {
                    @ApiResponse(
                            description = "User role changed successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Error occurred while updating role / account not found",
                            responseCode = "400"
                    ),
            }
    )
    @PutMapping("/users")
    public ResponseEntity<BasicResponse> updateUser(@RequestBody UpdateUserRequest request){
        try{
            service.changeUserRole(request.getEmail(), request.getRole());

            return new ResponseEntity<>(new BasicResponse("success", "Updated user."), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(new BasicResponse("fail", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            description = "Delete an user, given their email address",
            summary = "Delete user by email",
            responses = {
                    @ApiResponse(
                            description = "User deleted successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Error occurred while deleting user / account not found",
                            responseCode = "400"
                    ),
            }
    )
    @DeleteMapping ("/users")
    public ResponseEntity<BasicResponse> deleteUser(@RequestBody DeleteUserRequest request){
        try{
            String email = request.getEmail();
            service.deleteUser(email);

            return new ResponseEntity<>(new BasicResponse("success", "Deleted user with email " + email), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(new BasicResponse("fail", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
