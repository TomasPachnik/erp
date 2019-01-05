package sk.tomas.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.tomas.erp.bo.ChangePassword;
import sk.tomas.erp.bo.Result;
import sk.tomas.erp.bo.User;
import sk.tomas.erp.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> all() {
        return userService.all();
    }

    @GetMapping("/get/{uuid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User get(@PathVariable UUID uuid) {
        return userService.get(uuid);
    }

    @GetMapping("/getByLogin/{login}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User get(@PathVariable String login) {
        return userService.getByLogin(login);
    }

    @GetMapping("/getByToken")
    public User get() {
        return userService.getByToken();
    }

    @GetMapping("/remove/{uuid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean delete(@PathVariable UUID uuid) {
        return userService.delete(uuid);
    }

    @PostMapping(path = "/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UUID save(@RequestBody User user) {
        return userService.save(user);
    }

    //TODO save user without enabled variable and save only with admin privilegies

    @PostMapping(path = "/changePassword")
    public Result changePass(@RequestBody ChangePassword changePassword) {
        return userService.changePassword(changePassword);
    }

}
