package sk.tomas.erp.service.impl;

import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sk.tomas.erp.bo.Invoice;
import sk.tomas.erp.exception.PdfGenerateException;
import sk.tomas.erp.service.PdfService;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Service
public class PdfServiceImpl implements PdfService {

    private static Logger logger = LoggerFactory.getLogger(PdfServiceImpl.class);

    @Override
    public byte[] generatePdf(Invoice invoice) {
        try {
            return Files.readAllBytes(privateGeneratePdf(invoice, invoice.getInvoiceNumber()).toPath());
        } catch (JRException | IOException e) {
            logger.error(e.getMessage());
            throw new PdfGenerateException("Failed to generate PDF!");
        }
    }

    private File privateGeneratePdf(Invoice invoice, String name) throws JRException, IOException {
        InputStream mainStream = InvoiceServiceImpl.class.getResourceAsStream("/Blank_A4.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(mainStream);
        Map<String, Object> params = map(invoice);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        File pdf = File.createTempFile(name + ".", ".pdf");
        JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
        return pdf;
    }


    private Map<String, Object> map(Invoice invoice) {
        Map<String, Object> params = new HashMap<>();
        params.put("currency", invoice.getCurrency());
        params.put("invoiceNumber", invoice.getInvoiceNumber());

        params.put("supplier.name", invoice.getSupplier().getName());
        params.put("supplier.address.street", invoice.getSupplier().getAddress().getStreet());
        params.put("supplier.address.houseNumber", invoice.getSupplier().getAddress().getHouseNumber());
        params.put("supplier.address.postalCode", invoice.getSupplier().getAddress().getPostalCode());
        params.put("supplier.address.town", invoice.getSupplier().getAddress().getTown());
        params.put("supplier.address.country", invoice.getSupplier().getAddress().getCountry());
        params.put("supplier.companyIdentificationNumber", invoice.getSupplier().getCompanyIdentificationNumber());
        params.put("supplier.taxIdentificationNumber", invoice.getSupplier().getTaxIdentificationNumber());

        params.put("customer.name", invoice.getCustomer().getName());
        params.put("customer.address.street", invoice.getCustomer().getAddress().getStreet());
        params.put("customer.address.houseNumber", invoice.getCustomer().getAddress().getHouseNumber());
        params.put("customer.address.postalCode", invoice.getCustomer().getAddress().getPostalCode());
        params.put("customer.address.town", invoice.getCustomer().getAddress().getTown());
        params.put("customer.address.country", invoice.getCustomer().getAddress().getCountry());
        params.put("customer.companyIdentificationNumber", invoice.getCustomer().getCompanyIdentificationNumber());
        params.put("customer.taxIdentificationNumber", invoice.getCustomer().getTaxIdentificationNumber());

        params.put("customer.supplierBankAccount.bankName", invoice.getSupplierBankAccount().getBankName());
        params.put("customer.supplierBankAccount.iban", invoice.getSupplierBankAccount().getIban());
        params.put("customer.supplierBankAccount.swift", invoice.getSupplierBankAccount().getSwift());
        params.put("customer.supplierVariableSymbol", invoice.getSupplierVariableSymbol());
        params.put("customer.dateOfIssue", invoice.getDateOfIssue());
        params.put("customer.deliveryDate", invoice.getDeliveryDate());
        params.put("customer.dueDate", invoice.getDueDate());

        params.put("customer.asset.name", invoice.getAssets().get(0).getName());
        params.put("customer.asset.count", invoice.getAssets().get(0).getCount());
        params.put("customer.asset.unit", invoice.getAssets().get(0).getUnit());
        params.put("customer.asset.unitPrice", invoice.getAssets().get(0).getUnitPrice());
        params.put("customer.asset.total", invoice.getAssets().get(0).total());

        params.put("customer.total", invoice.total());

        params.put("issuer.name", invoice.getIssuer().getName());
        params.put("issuer.email", invoice.getIssuer().getEmail());
        params.put("issuer.phone", invoice.getIssuer().getPhone());

        return params;
    }

}
