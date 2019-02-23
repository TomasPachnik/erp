package sk.tomas.erp;

import org.junit.Assert;
import org.junit.Test;
import sk.tomas.erp.bo.User;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static sk.tomas.erp.util.Utils.unzip;
import static sk.tomas.erp.util.Utils.zip;

public class ZipTest {

    @Test
    public void zipUnzipTest() {
        PodamFactory factory = new PodamFactoryImpl();
        User user = factory.manufacturePojo(User.class);
        byte[] bytes = zip(user);
        User unzipped = (User) unzip(bytes);
        Assert.assertEquals(user, unzipped);
    }

}
