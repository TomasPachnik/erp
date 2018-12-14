package sk.tomas.erp.service.impl;

import net.sf.jasperreports.engine.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.tomas.erp.ResourceNotFoundException;
import sk.tomas.erp.ShortStringManufacturer;
import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.entity.InvoiceEntity;
import sk.tomas.erp.repository.InvoiceRepository;
import sk.tomas.erp.service.InvoiceService;
import uk.co.jemos.podam.api.DataProviderStrategy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Override
    public Invoice get(UUID uuid) {
        return invoiceRepository.findById(uuid)
                .map(invoiceEntity -> mapper.map(invoiceEntity, Invoice.class))
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id " + uuid));
    }

    @Override
    public File generatePdf(InvoiceEntity invoice, String name) throws JRException, IOException {
        InputStream mainStream = InvoiceServiceImpl.class.getResourceAsStream("/Invoice.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(mainStream);
        Map<String, Object> params = new HashMap<>();
        params.put("name", "nejaky text");
        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        File pdf = File.createTempFile(name + ".", ".pdf");
        JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
        return pdf;
    }

    @Override
    public Invoice generate() {
        PodamFactory factory = new PodamFactoryImpl();
        factory.getStrategy().addOrReplaceTypeManufacturer(String.class, new ShortStringManufacturer());
        Invoice invoice = factory.manufacturePojo(Invoice.class);
        InvoiceEntity invoiceEntity = invoiceRepository.save(mapper.map(invoice, InvoiceEntity.class));
        return mapper.map(invoiceEntity, Invoice.class);
    }

    @Override
    public List<Invoice> all() {
        List<InvoiceEntity> all = invoiceRepository.findAll();
        Type listType = new TypeToken<List<Invoice>>() {
        }.getType();
        return mapper.map(all, listType);
    }

}
