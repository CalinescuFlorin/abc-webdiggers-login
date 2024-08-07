package web_diggers.abc_backend.Security.user;

import org.springframework.beans.factory.annotation.Autowired;
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

    // TODO: Hide the passwords
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(service.getUsers(), HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@RequestBody String role, @PathVariable(name="id")int id){
        try{
            service.updateUser(id, role);

            return new ResponseEntity<>("Updated user.\n", HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping ("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name="id")int id){
        try{
            service.deleteUser(id);

            return new ResponseEntity<>("Deleted user with id " + id, HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest request){
        try{
            service.updateUser(request.getEmail(), request.getRole());

            return new ResponseEntity<>("Updated user.\n", HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping ("/users")
    public ResponseEntity<String> deleteUser(@RequestBody String email){
        try{
            service.deleteUser(email);

            return new ResponseEntity<>("Deleted user with email " + email, HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
