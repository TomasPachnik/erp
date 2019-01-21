package sk.tomas.erp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tomas.erp.bo.ChangePassword;
import sk.tomas.erp.bo.ChangeUser;
import sk.tomas.erp.bo.User;
import sk.tomas.erp.service.UserService;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PositiveFlowTest {

    @Autowired
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    public void positiveFlowTest() {
        updateUser();
        changePassword();
        UUID user = createUserByAdmin();
        updateUserByAdmin(user);
//        createSupplier();
//        updateSupplier();
//        createCustomer();
//        updateCustomer();
//        createInvoice();
//        updateInvoice();
//        deleteInvoice();
//        deleteSupplier();
//        deleteCustomer();
//        deleteUserByAdmin();

    }

    private void updateUser() {
        User user = userService.getByToken();
        user.setPhone("phone number");
        ChangeUser changeUser = new ChangeUser();
        changeUser.setEmail("new@new");
        changeUser.setName("new");
        changeUser.setPhone("new_phone");
        userService.saveCurrent(changeUser);
        User changed = userService.getByToken();
        Assert.assertEquals(changeUser.getName(), changed.getName());
        Assert.assertEquals(changeUser.getEmail(), changed.getEmail());
        Assert.assertEquals(changeUser.getPhone(), changed.getPhone());
    }

    private void changePassword() {
        ChangePassword changePassword = new ChangePassword();
        changePassword.setOldPassword("password");
        changePassword.setNewPassword("Password1");
        userService.changePassword(changePassword);
    }

    private UUID createUserByAdmin() {
        User user = new User();
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@email");
        user.setPhone("phone");
        UUID uuid = userService.save(user);
        User changed = userService.get(uuid);
        Assert.assertEquals(user, changed);
        return uuid;
    }

    private void updateUserByAdmin(UUID uuid) {
        User user = userService.get(uuid);
        user.setPhone("new_phone");
        userService.save(user);
        User changed = userService.get(uuid);
        Assert.assertEquals(user.getPhone(), changed.getPhone());
    }

}
