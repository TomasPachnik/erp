package sk.tomas.erp;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import sk.tomas.erp.service.InvoiceService;

@SpringBootApplication
public class ErpApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(ErpApplication.class, args);
        InvoiceService invoiceService = app.getBean(InvoiceService.class);
        invoiceService.getById(null);
    }


}
