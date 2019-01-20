package sk.tomas.erp.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Base implements Serializable {

    private UUID uuid;

}
