package sk.tomas.erp.validator;

import sk.tomas.erp.bo.Address;

public class AddressServiceValidator {

    public static void validateAddress(Address address) {
        BaseValidator.validateNull(address, "Address");
        BaseValidator.validateNullOrEmpty(address.getStreet(), "Street");
        BaseValidator.validateMaxLength(address.getStreet(), 60, "street");
        BaseValidator.validateNullOrEmpty(address.getHouseNumber(), "House number");
        BaseValidator.validateMaxLength(address.getHouseNumber(), 20, "House number");
        BaseValidator.validateNullOrEmpty(address.getPostalCode(), "Postal code");
        BaseValidator.validateMaxLength(address.getPostalCode(), 10, "postal code");
        BaseValidator.validateNullOrEmpty(address.getTown(), "Town");
        BaseValidator.validateMaxLength(address.getTown(), 60, "town");
        BaseValidator.validateNullOrEmpty(address.getCountry(), "Country");
        BaseValidator.validateMaxLength(address.getCountry(), 60, "country");
    }

}
