package sk.tomas.erp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tomas.erp.entity.Last12Months;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InvoiceTest extends BaseTest {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    public void last12MonthsTest() {
        UUID supplier = createSupplier();
        UUID customer = createCustomer();

        Date now = dateService.getActualDate();

        Date first = dateService.addMonths(now, -13);
        Date second = dateService.addMonths(now, -10);
        Date third = dateService.addMonths(now, -3);

        createInvoice(supplier, customer, now);
        createInvoice(supplier, customer, first);
        createInvoice(supplier, customer, second);
        createInvoice(supplier, customer, third);
        Last12Months last12Months = invoiceService.calculateRevenueForLast12Months();

        Assert.assertEquals(BigDecimal.valueOf(441.30).stripTrailingZeros(), last12Months.getAmount().stripTrailingZeros());
        Assert.assertEquals(3, last12Months.getInvoiceCount());
        Assert.assertEquals(sdf.format(dateService.getFirstDayOfThisMonthLastYearDate()), sdf.format(last12Months.getFrom()));
    }

}
