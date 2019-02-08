package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sk.tomas.erp.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT p FROM UserEntity p WHERE LOWER(p.username) = LOWER(:username)")
    List<UserEntity> find(@Param("username") String username);


}
