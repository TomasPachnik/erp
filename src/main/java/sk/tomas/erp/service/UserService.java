package sk.tomas.erp.service;

import sk.tomas.erp.bo.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<User> all();

    UUID save(User user);

    User get(UUID uuid);

    boolean delete(UUID uuid);
}
