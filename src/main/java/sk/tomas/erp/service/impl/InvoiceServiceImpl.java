package sk.tomas.erp.service.impl;

import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;
import sk.tomas.erp.dto.Invoice;
import sk.tomas.erp.service.InvoiceService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

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
        return null;
    }


}
