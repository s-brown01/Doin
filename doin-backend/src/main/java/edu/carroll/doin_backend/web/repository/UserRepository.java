package edu.carroll.doin_backend.web.repository;

import edu.carroll.doin_backend.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
//    User findByUsername(String userName);
}
