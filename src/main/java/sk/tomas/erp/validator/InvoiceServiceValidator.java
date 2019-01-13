package sk.tomas.erp.validator;

import sk.tomas.erp.bo.Asset;
import sk.tomas.erp.bo.InvoiceInput;
import sk.tomas.erp.bo.PagingInput;
import sk.tomas.erp.exception.InputValidationException;

public class InvoiceServiceValidator {

    public static void validatePagingInput(PagingInput input) {
        BaseValidator.validateNull(input, "input");
        if (input.getPageIndex() < 0) {
            throw new InputValidationException("Page index can not be negative number.");
        }
        if (input.getPageSize() <= 0) {
            throw new InputValidationException("Page index have to be positive number.");
        }
    }

    public static void validateInvoice(InvoiceInput invoice) {
        BaseValidator.validateNull(invoice, invoice.getClass().getSimpleName());
        BaseValidator.validateNullOrEmpty(invoice.getName(), "Name");
        BaseValidator.validateMaxLength(invoice.getName(), 60, "name");
        BaseValidator.validateNullOrEmpty(invoice.getInvoiceNumber(), "Invoice number");
        BaseValidator.validateMaxLength(invoice.getInvoiceNumber(), 20, "invoice number");
        BaseValidator.validateNullOrEmpty(invoice.getSupplierVariableSymbol(), "Variable symbol");
        BaseValidator.validateMaxLength(invoice.getSupplierVariableSymbol(), 20, "variable symbol");
        BaseValidator.validateNullOrEmpty(invoice.getCurrency(), "Currency");
        BaseValidator.validateMaxLength(invoice.getCurrency(), 10, "currency");
        BaseValidator.validateUuid(invoice.getCustomer(), "customer");
        BaseValidator.validateUuid(invoice.getSupplier(), "supplier");
        BaseValidator.validateNull(invoice.getDateOfIssue(), "Date of issue");
        BaseValidator.validateNull(invoice.getDeliveryDate(), "Delivery date");
        BaseValidator.validateNull(invoice.getDueDate(), "Due date");
        BaseValidator.validateNull(invoice.getAssets(), "Assets");
        for (Asset asset : invoice.getAssets()) {
            BaseValidator.validateNullOrEmpty(asset.getName(), "Asset name");
            BaseValidator.validateMaxLength(asset.getName(), 60, "asset name");
            BaseValidator.validateNullOrEmpty(asset.getUnit(), "Asset unit");
            BaseValidator.validateMaxLength(asset.getUnit(), 10, "asset unit");
        }
    }

}
