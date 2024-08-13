package web_diggers.abc_backend.Security.user;

import org.springframework.beans.factory.annotation.Autowired;
import web_diggers.abc_backend.common.BasicResponse;
import web_diggers.abc_backend.Security.user.model.DeleteUserRequest;
import web_diggers.abc_backend.Security.user.model.UpdateUserRequest;
import web_diggers.abc_backend.Security.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserService service;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<BasicResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable(name="id")int id){
        try{
            service.updateUser(id, request.getRole());

            return new ResponseEntity<>(new BasicResponse("success", "Updated user."), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(new BasicResponse("fail", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping ("/users/{id}")
    public ResponseEntity<BasicResponse> deleteUser(@PathVariable(name="id")int id){
        try{
            service.deleteUser(id);

            return new ResponseEntity<>(new BasicResponse("success", "Deleted user with id " + id), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(new BasicResponse("fail", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users")
    public ResponseEntity<BasicResponse> updateUser(@RequestBody UpdateUserRequest request){
        try{
            service.updateUser(request.getEmail(), request.getRole());

            return new ResponseEntity<>(new BasicResponse("success", "Updated user."), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(new BasicResponse("fail", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

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
