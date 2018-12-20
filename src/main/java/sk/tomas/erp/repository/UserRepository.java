package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tomas.erp.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByLogin(String login);


}
