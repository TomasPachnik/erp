package sk.tomas.erp.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerMessage {

    private String type;
    private String message;

}
