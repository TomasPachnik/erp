package sk.tomas.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<User> all() {
        return userService.all();
    }

    @GetMapping("/{uuid}")
    public User get(@PathVariable UUID uuid) {
        return userService.get(uuid);
    }

    @GetMapping("/delete/{uuid}")
    public boolean delete(@PathVariable UUID uuid) {
        return userService.delete(uuid);
    }

    @PostMapping(path = "/save")
    public UUID save(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping(path = "/changePass")
    public Result changePass(@RequestBody ChangePassword changePassword){
        return userService.changePassword(changePassword);
    }

}
