package sk.tomas.erp.service;

import sk.tomas.erp.bo.ChangePassword;
import sk.tomas.erp.bo.Result;
import sk.tomas.erp.bo.User;
import sk.tomas.erp.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<User> all();

    UUID save(User user);

    User get(UUID uuid);

    boolean delete(UUID uuid);

    UserEntity getLoggedUser();

    Result changePassword(ChangePassword changePassword);

    User getByLogin(String login);

    User getByToken();

}
