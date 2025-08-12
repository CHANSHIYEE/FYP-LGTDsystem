package shiyee_FYP.fullstack_backend.repository;

import org.springframework.stereotype.Repository;
import shiyee_FYP.fullstack_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);


}

