package sk.tomas.erp.bo;

import lombok.Data;

@Data
public class Legal extends Base {

    private String name;
    private String companyIdentificationNumber;
    private String taxIdentificationNumber;
    private Address address;
    private BankAccount bankAccount;

    @Override
    public String toString() {
        return "Legal{" +
                "name='" + name + '\'' +
                ", companyIdentificationNumber='" + companyIdentificationNumber + '\'' +
                ", taxIdentificationNumber='" + taxIdentificationNumber + '\'' +
                ", address=" + address +
                ", bankAccount=" + bankAccount +
                "} " + super.toString();
    }
}
