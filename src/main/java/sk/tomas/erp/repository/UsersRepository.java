package sk.tomas.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sk.tomas.erp.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<UserEntity, UUID> {

    @Query("SELECT p FROM UserEntity p WHERE LOWER(p.login) = LOWER(:login)")
    List<UserEntity> find(@Param("login") String login);

}
