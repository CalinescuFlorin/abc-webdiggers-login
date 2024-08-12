package web_diggers.abc_backend.Security.user;

import web_diggers.abc_backend.Security.user.model.Role;
import web_diggers.abc_backend.Security.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUsers(){
        List<User> users = userRepository.findAll();

        return users.stream().map((user) -> {
            user.setPassword("");
            return user;
        }).toList();
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

      /////////////////////////////////////////////
     // - - - Operations based on user ID - - - //
    /////////////////////////////////////////////

    public Optional<User> getUser(int id){
        return userRepository.findById(id);
    }

    public void changeUserRole(int id, String newRole) throws Exception{
        Optional<User> query = userRepository.findById(id);
        if(query.isEmpty())
            throw new Exception("Cannot update user that does not exist.");

        User user = query.get();
        user.setRole(Role.valueOf(newRole));
        userRepository.save(user);
    }

    public void deleteUser(int id) throws Exception{
        if(userRepository.findById(id).isEmpty())
            throw new Exception("Cannot delete user that does not exist.");

        userRepository.deleteById(id);
    }

      ///////////////////////////////////////////////////
     // - - - Operations based on email address - - - //
    ///////////////////////////////////////////////////

    public Optional<User> getUser(String email){
        return userRepository.findUserByEmail(email);
    }

    public void changeUserRole(String email, String newRole) throws Exception{
        Optional<User> query = userRepository.findUserByEmail(email);
        if(query.isEmpty())
            throw new Exception("Cannot update user that does not exist.");

        User user = query.get();
        user.setRole(Role.valueOf(newRole));
        userRepository.save(user);
    }

    public void deleteUser(String email) throws Exception{
        if(userRepository.findUserByEmail(email).isEmpty())
            throw new Exception("Cannot delete user that does not exist.");

        userRepository.deleteUserByEmail(email);
    }

    public void updateUser(User user) throws Exception{
        Optional<User> query = userRepository.findById(user.getId());
        if(query.isEmpty())
            throw new Exception("Cannot update user that does not exist.");

        User existingUser = query.get();
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setRole(user.getRole());
        existingUser.setEnabled(user.isEnabled());
    }
}
