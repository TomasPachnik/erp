package sk.tomas.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.tomas.erp.bo.*;
import sk.tomas.erp.service.UserService;

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

    @PostMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Paging all(@RequestBody PagingInput input) {
        return userService.allUsers(input);
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

    @PostMapping(path = "/saveCurrent")
    public UUID saveCurrent(@RequestBody ChangeUser changeUser) {
        return userService.saveCurrent(changeUser);
    }

    @PostMapping(path = "/changePassword")
    public Result changePass(@RequestBody ChangePassword changePassword) {
        return userService.changePassword(changePassword);
    }

}
