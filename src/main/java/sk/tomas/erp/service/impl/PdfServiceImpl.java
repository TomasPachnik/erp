package sk.tomas.erp.service.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
        InputStream mainStream = InvoiceServiceImpl.class.getResourceAsStream("/invoice.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(mainStream);
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

        translations.put("invoice", "Faktúra");
        translations.put("supplier", "Dodávateľ:");
        translations.put("customer", "Odberateľ:");
        translations.put("bankName", "Banka:");
        translations.put("iban", "IBAN:");
        translations.put("variableSymbol", "Variabilný symbol:");
        translations.put("crn", "IČO:");
        translations.put("vat", "DIČ:");
        translations.put("dateOfIssue", "Dátum vystavenia:");
        translations.put("deliveryDate", "Dátum dodania:");
        translations.put("dueDate", "Dátum splatnosti:");
        translations.put("nameAndDesc", "Názov a popis položky:");
        translations.put("count", "Počet");
        translations.put("unit", "Jednotka");
        translations.put("unitCount", "Jednotková cena");
        translations.put("total", "Celkom");
        translations.put("note", "Poznámka:");
        translations.put("totalPrice", "Celková suma:");
        translations.put("signatureAndStamp", "Podpis a pečiatka:");
        translations.put("issuer", "Vystavil:");
        translations.put("generatedBy", "Vytvorené pomocou ERP");
        translations.put("page", "Strana:");

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
        params.put("supplierBankAccount.iban", translations.get("iban") + " " + invoice.getSupplierBankAccount().getIban());

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
