package shiyee_FYP.fullstack_backend.security;

//import org.springframework.security.core.userdetails.UserDetails;
import shiyee_FYP.fullstack_backend.model.User;
import shiyee_FYP.fullstack_backend.repository.UserRepository;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
//public class MyUserDetailsService implements UserDetailsService {
public class MyUserDetailsService {
    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//
//        if (user == null){
//            throw new UsernameNotFoundException("This user does not exist in the database");
//        }
//
//        return new UserPrincipal(user);
//    }
}