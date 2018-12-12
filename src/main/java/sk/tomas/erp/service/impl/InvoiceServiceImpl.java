package sk.tomas.erp.service.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.service.InvoiceService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public Invoice getById(UUID uuid) {
        return null;
    }

    @Override
    public File generatePdf(Invoice invoice, String name) throws JRException, IOException {
        InputStream inputStream = null;
        JasperReport report = (JasperReport) JRLoader.loadObject(inputStream);
        JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>());
        File pdf = File.createTempFile(name + ".", ".pdf");
        JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
        return pdf;
    }

}
