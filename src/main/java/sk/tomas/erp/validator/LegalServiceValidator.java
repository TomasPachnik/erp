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
        AddressServiceValidator.validateAddress(legal.getAddress());

        if (legal.getBankAccount() != null) {
            validateBankAccount(legal.getBankAccount());
        }

    }

    private static void validateBankAccount(BankAccount account) {
        BaseValidator.validateNull(account, "BankAccount");
        if (account.getIban() != null && !account.getIban().isEmpty()) {
            try {
                Iban.valueOf(account.getIban());
            } catch (IbanFormatException | InvalidCheckDigitException | UnsupportedCountryException e) {
                throw new InputValidationException("IBAN is not valid.");
            }
        }
    }

}
