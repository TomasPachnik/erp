package sk.tomas.erp.config;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tomas.erp.controller.User;

import java.util.Optional;

 public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);


}
