package sk.tomas.erp;

import org.junit.Assert;
import org.junit.Test;
import sk.tomas.erp.dto.Asset;
import sk.tomas.erp.dto.Invoice;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.math.BigDecimal;

public class InvoiceTest {

    @Test
    public void testInvoiceTotalSum() {
        Invoice invoice = new Invoice();
        Asset asset1 = new Asset();
        Asset asset2 = new Asset();
        asset1.setCount(new BigDecimal("2"));
        asset1.setUnitPrice(new BigDecimal("0.3"));
        asset2.setCount(new BigDecimal("3"));
        asset2.setUnitPrice(new BigDecimal("0.2"));
        //invoice.setAssets(new ArrayList<>());
        //invoice.getAssets().add(asset1);
        //invoice.getAssets().add(asset2);
        //Assert.assertEquals(invoice.total(), new BigDecimal("1.2"));
    }

    @Test
    public void testInvoiceObject() {
        PodamFactory factory = new PodamFactoryImpl();
        Invoice invoice = factory.manufacturePojo(Invoice.class);
        Assert.assertNotNull(invoice);
    }
}
