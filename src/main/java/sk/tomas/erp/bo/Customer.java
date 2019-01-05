package sk.tomas.erp.bo;

import lombok.Data;

@Data
public class Customer extends Legal{


    @Override
    public String toString() {
        return "Customer{} " + super.toString();
    }
}
