package sk.tomas.erp.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingInput {

    private String filter;
    private String sort;
    private String sortDirection;
    private int pageIndex;
    private int pageSize;

}
