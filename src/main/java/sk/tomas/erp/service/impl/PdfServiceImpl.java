package sk.tomas.erp.service.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.iban4j.Iban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    @Autowired
    private Environment env;

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
        InputStream invoiceStream = InvoiceServiceImpl.class.getResourceAsStream("/invoice.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(invoiceStream);
        Map<String, Object> params = map(invoice);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        File pdf = File.createTempFile(name + ".", ".pdf");
        JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
        return pdf;
    }

    private Map<String, Object> map(Invoice invoice) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> translations = new HashMap<>();
        JRBeanCollectionDataSource assets = new JRBeanCollectionDataSource(invoice.getAssets());

        Iban iban = Iban.valueOf(invoice.getSupplierBankAccount().getIban());

        translations.put("invoice", env.getProperty("invoice"));
        translations.put("supplier", env.getProperty("supplier"));
        translations.put("customer", env.getProperty("customer"));
        translations.put("bankName", env.getProperty("bankName"));
        translations.put("iban", env.getProperty("iban"));
        translations.put("variableSymbol", env.getProperty("variableSymbol"));
        translations.put("crn", env.getProperty("crn"));
        translations.put("vat", env.getProperty("vat"));
        translations.put("dateOfIssue", env.getProperty("dateOfIssue"));
        translations.put("deliveryDate", env.getProperty("deliveryDate"));
        translations.put("dueDate", env.getProperty("dueDate"));
        translations.put("nameAndDesc", env.getProperty("nameAndDesc"));
        translations.put("count", env.getProperty("count"));
        translations.put("unit", env.getProperty("unit"));
        translations.put("unitCount", env.getProperty("unitCount"));
        translations.put("total", env.getProperty("total"));
        translations.put("note", env.getProperty("note"));
        translations.put("totalPrice", env.getProperty("totalPrice"));
        translations.put("signatureAndStamp", env.getProperty("signatureAndStamp"));
        translations.put("issuer", env.getProperty("issuer"));
        translations.put("generatedBy", env.getProperty("generatedBy"));
        translations.put("page", env.getProperty("page"));

        params.put("currency", invoice.getCurrency());
        params.put("invoiceNumber", translations.get("invoice") + " " + invoice.getInvoiceNumber());

        params.put("supplier", translations.get("supplier"));
        params.put("customer", translations.get("customer"));

        params.put("supplier.name", invoice.getSupplier().getName());
        params.put("supplier.address.street", invoice.getSupplier().getAddress().getStreet());
        params.put("supplier.address.houseNumber", invoice.getSupplier().getAddress().getHouseNumber());
        params.put("supplier.address.postalCode", invoice.getSupplier().getAddress().getPostalCode());
        params.put("supplier.address.town", invoice.getSupplier().getAddress().getTown());
        params.put("supplier.address.country", invoice.getSupplier().getAddress().getCountry());
        params.put("supplier.companyIdentificationNumber", translations.get("crn") + " " + invoice.getSupplier().getCompanyIdentificationNumber());
        params.put("supplier.taxIdentificationNumber", translations.get("vat") + " " + invoice.getSupplier().getTaxIdentificationNumber());

        params.put("customer.name", invoice.getCustomer().getName());
        params.put("customer.address.street", invoice.getCustomer().getAddress().getStreet());
        params.put("customer.address.houseNumber", invoice.getCustomer().getAddress().getHouseNumber());
        params.put("customer.address.postalCode", invoice.getCustomer().getAddress().getPostalCode());
        params.put("customer.address.town", invoice.getCustomer().getAddress().getTown());
        params.put("customer.address.country", invoice.getCustomer().getAddress().getCountry());
        params.put("customer.companyIdentificationNumber", translations.get("crn") + " " + invoice.getCustomer().getCompanyIdentificationNumber());
        params.put("customer.taxIdentificationNumber", translations.get("vat") + " " + invoice.getCustomer().getTaxIdentificationNumber());

        params.put("supplierBankAccount.bankName", translations.get("bankName") + " " + invoice.getSupplierBankAccount().getBankName());
        params.put("supplierBankAccount.iban", translations.get("iban") + " " + iban.toFormattedString());

        params.put("supplierVariableSymbol", translations.get("variableSymbol") + " " + invoice.getSupplierVariableSymbol());

        params.put("dateOfIssue", translations.get("dateOfIssue"));
        params.put("deliveryDate", translations.get("deliveryDate"));
        params.put("dueDate", translations.get("dueDate"));

        params.put("dateOfIssueValue", new java.sql.Date(invoice.getDateOfIssue().getTime()));
        params.put("deliveryDateValue", new java.sql.Date(invoice.getDeliveryDate().getTime()));
        params.put("dueDateValue", new java.sql.Date(invoice.getDueDate().getTime()));

        params.put("assets", assets);
        params.put("customer.total", invoice.getTotal());
        params.put("noteValue", invoice.getNote());

        params.put("issuer.name", invoice.getIssuer().getName());
        params.put("issuer.email", invoice.getIssuer().getEmail());
        params.put("issuer.phone", invoice.getIssuer().getPhone());

        params.put("nameAndDesc", translations.get("nameAndDesc"));
        params.put("count", translations.get("count"));
        params.put("unit", translations.get("unit"));
        params.put("unitPrice", translations.get("unitCount"));
        params.put("total", translations.get("total"));
        params.put("note", translations.get("note"));
        params.put("totalPrice", translations.get("totalPrice"));
        params.put("signatureAndStamp", translations.get("signatureAndStamp"));
        params.put("issuer", translations.get("issuer"));
        params.put("generatedBy", translations.get("generatedBy"));
        params.put("page", translations.get("page"));

        return params;
    }

}
