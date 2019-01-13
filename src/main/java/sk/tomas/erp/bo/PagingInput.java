package sk.tomas.erp.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingInput {

    private int pageIndex;
    private int pageSize;

}
