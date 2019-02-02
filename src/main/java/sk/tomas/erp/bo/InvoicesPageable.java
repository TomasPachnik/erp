package sk.tomas.erp.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
public class InvoicesPageable implements Pageable {

    private int pageIndex;
    private int pageSize;

    @Override
    public int getPageNumber() {
        return pageIndex / pageSize;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getOffset() {
        return pageIndex;
    }

    @Override
    public Sort getSort() {
        return new Sort(Sort.Direction.ASC, "dueDate");
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}
