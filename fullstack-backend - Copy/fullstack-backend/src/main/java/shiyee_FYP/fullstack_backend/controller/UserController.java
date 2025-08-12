package shiyee_FYP.fullstack_backend.controller;
import shiyee_FYP.fullstack_backend.model.LoginRequest;
import shiyee_FYP.fullstack_backend.Service.UserService;
import shiyee_FYP.fullstack_backend.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/user/{id}")
    public User updateUser(@RequestBody() User user, @PathVariable("id") Long id) {
        return userService.updateUser(user);
    }

//    @PostMapping("/register")
//    public ResponseEntity<User> newUser(@RequestBody() User user) {
//        User newUser = userService.addUser(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
//    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
//        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password cannot be null");
//        }
//
//        try {
//            boolean isAuthenticated = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
//
//            if (isAuthenticated) {
//                session.setAttribute("user", loginRequest.getUsername());
//                return ResponseEntity.ok("Login was successful!");
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unknown error occurred: " + e.getMessage());
//        }
//    }
}


//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
//        try{
//            boolean isAuthenticated = userService.authenticate(loginRequest.getUsername(),loginRequest.getPassword());
//
//            if (isAuthenticated){
//                session.setAttribute("user", loginRequest.getUsername());
//                return ResponseEntity.ok("Login was successful!");
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unknown error occurred");
//        }
//    }

//
//    @PostMapping("/register")
//    public User newUser(@RequestBody User user){
//        // 使用newUser.setPassword，而不是user.setPassword
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//
//    }
//    @PostMapping("/register")
//    public ResponseEntity<User> newUser(@RequestBody User user) {
//
//        // 返回 CREATED 状态和保存的用户对象
//        return ResponseEntity.status(HttpStatus.CREATED).body(user);
//    }
//
//
//    @GetMapping("/users")
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    @GetMapping("/user/{id}")
//    public User getUserById(@PathVariable Long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundException(id));
//    }
//
//    @PutMapping("/user/{id}")
//    public User updateUser(@RequestBody User newUser, @PathVariable Long id) {
//        return userRepository.findById(id)
//                .map(user -> {
//                    user.setUsername(newUser.getUsername());
//                    user.setName(newUser.getName());
//                    user.setEmail(newUser.getEmail());
//                    user.setPassword(newUser.getPassword());
//                    return userRepository.save(user);
//                }).orElseThrow(() -> new UserNotFoundException(id));
//    }
//
//    @DeleteMapping("/user/{id}")
//    public String deleteUser(@PathVariable Long id) {
//        if (!userRepository.existsById(id)) {
//            throw new UserNotFoundException(id);
//        }
//        userRepository.deleteById(id);
//        return "User with id " + id + " has been deleted.";
//    }
//}
