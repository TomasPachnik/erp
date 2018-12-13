package sk.tomas.erp.service.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.id.IdentifierGenerationException;
import org.springframework.stereotype.Service;
import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.service.InvoiceService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public Invoice getById(UUID uuid) {

        PodamFactory factory = new PodamFactoryImpl();
        Invoice invoice = factory.manufacturePojo(Invoice.class);
        invoice.setUuid(null);
        create(invoice);
        return null;
    }

    @Override
    public File generatePdf(Invoice invoice, String name) throws JRException, IOException {
        InputStream mainStream = InvoiceServiceImpl.class.getResourceAsStream("/Invoice.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(mainStream);
        Map<String, Object> params = new HashMap<>();
        params.put("name", "nejaky text");
        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        File pdf = File.createTempFile(name + ".", ".pdf");
        JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
        return pdf;
    }

    public UUID create(Invoice invoice) {
        try {
            Transaction transaction = getCurrentSession().beginTransaction();
            getCurrentSession().save(invoice);
            transaction.commit();
        } catch (IdentifierGenerationException e) {
            throw new IllegalArgumentException("some error message");
        }
        return invoice.getUuid();
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
