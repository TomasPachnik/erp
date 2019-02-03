package sk.tomas.erp.util;


import org.springframework.data.domain.Sort;
import sk.tomas.erp.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utils {

    public static List<UUID> entitiesToUuids(List<? extends BaseEntity> entities) {
        List<UUID> uuids = new ArrayList<>();
        for (BaseEntity entity : entities) {
            uuids.add(entity.getUuid());
        }
        return uuids;
    }

    public static String createdUpdated(Object o) {
        if (o == null) {
            return "created";
        }
        return "updated";
    }

    public static Sort.Direction getSortDirection(String sortDirection) {
        if ("desc".equals(sortDirection)) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

}
