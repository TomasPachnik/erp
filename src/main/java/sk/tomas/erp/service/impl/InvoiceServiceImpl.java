package sk.tomas.erp.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sk.tomas.erp.exception.ResourceNotFoundException;
import sk.tomas.erp.ShortStringManufacturer;
import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.entity.InvoiceEntity;
import sk.tomas.erp.repository.InvoiceRepository;
import sk.tomas.erp.service.InvoiceService;
import sk.tomas.erp.service.PdfService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private ModelMapper mapper;
    private PdfService pdfService;
    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(ModelMapper mapper, PdfService pdfService, InvoiceRepository invoiceRepository) {
        this.mapper = mapper;
        this.pdfService = pdfService;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice get(UUID uuid) {
        return invoiceRepository.findById(uuid)
                .map(invoiceEntity -> mapper.map(invoiceEntity, Invoice.class))
                .orElseThrow(() -> new ResourceNotFoundException(Invoice.class.getSimpleName() + " not found with id " + uuid));
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

    @Override
    public byte[] generatePdf(UUID uuid) {
        return pdfService.generatePdf(get(uuid));
    }

}
