package shiyee_FYP.fullstack_backend.Service;

//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shiyee_FYP.fullstack_backend.model.User;
import shiyee_FYP.fullstack_backend.repository.UserRepository;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

//    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        this.userRepository = userRepository;
//    }


    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUser(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User addUser(User user){
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public  User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

//    public boolean authenticate(String username, String password) {
//        User user = userRepository.findByUsername(username);
//
//        if(!user.getUsername().equals(username)){
//            throw new UsernameNotFoundException("User does not exist in the database");
//        }
//
//        if (bCryptPasswordEncoder.matches(password, user.getpasswordHash())) {
//            throw  new BadCredentialsException("The password is incorrect");
//        }
//
//        return  true;
//    }


}