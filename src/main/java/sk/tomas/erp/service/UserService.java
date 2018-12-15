package sk.tomas.erp.service;

import sk.tomas.erp.bo.User;

import java.util.List;

public interface UserService {

    User generate();

    List<User> all();

}
