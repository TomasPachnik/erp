package sk.tomas.erp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tomas.erp.bo.Address;
import sk.tomas.erp.bo.BankAccount;
import sk.tomas.erp.bo.Customer;
import sk.tomas.erp.service.LegalService;

import javax.transaction.Transactional;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LegalTest {

    @Autowired
    private LegalService legalService;

    @Test
    //@Transactional
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    public void auditTest(){

        Customer customer = new Customer();
        customer.setName("customer");
        customer.setCompanyIdentificationNumber("ico");
        customer.setTaxIdentificationNumber("dic");

        Address address = new Address();
        address.setCountry("country");
        address.setHouseNumber("house number");
        address.setPostalCode("post");
        address.setStreet("street");
        address.setTown("town");

        BankAccount account = new BankAccount();
        account.setBankName("bank name");
        account.setIban("EE471000001020145685");

        customer.setAddress(address);
        customer.setBankAccount(account);

        UUID uuid = legalService.saveCustomer(customer);
        legalService.deleteCustomerByUuid(uuid);


    }

}
