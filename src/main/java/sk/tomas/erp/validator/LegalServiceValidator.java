package sk.tomas.erp.validator;

import org.iban4j.Iban;
import org.iban4j.IbanFormatException;
import org.iban4j.InvalidCheckDigitException;
import org.iban4j.UnsupportedCountryException;
import sk.tomas.erp.bo.BankAccount;
import sk.tomas.erp.bo.Legal;
import sk.tomas.erp.exception.InputValidationException;

public class LegalServiceValidator {

    public static void validateLegal(Legal legal) {
        BaseValidator.validateNull(legal, "Legal");
        BaseValidator.validateNullOrEmpty(legal.getName(), "Name");
        BaseValidator.validateMaxLength(legal.getName(), 60, "name");
        BaseValidator.validateNullOrEmpty(legal.getCompanyIdentificationNumber(), "Company identification number");
        BaseValidator.validateMaxLength(legal.getCompanyIdentificationNumber(), 20, "company identification number");
        BaseValidator.validateNullOrEmpty(legal.getTaxIdentificationNumber(), "Tax identification number");
        BaseValidator.validateMaxLength(legal.getTaxIdentificationNumber(), 20, "tax identification number");
        AddressServiceValidator.validateAddress(legal.getAddress());
        validateBankAccount(legal.getBankAccount());
    }


    public static void validateBankAccount(BankAccount account) {
        BaseValidator.validateNull(account, "BankAccount");
        BaseValidator.validateNullOrEmpty(account.getBankName(), "Bank name");
        BaseValidator.validateMaxLength(account.getBankName(), 30, "bank name");
        try {
            Iban.valueOf(account.getIban());
        } catch (IbanFormatException | InvalidCheckDigitException | UnsupportedCountryException e) {
            throw new InputValidationException("IBAN is not valid.");
        }
    }

}
