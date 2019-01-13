package sk.tomas.erp.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paging {

    private int total;
    private List<? extends Base> content;
    private PagingInput pageable;

}
