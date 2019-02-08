package sk.tomas.erp.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import sk.tomas.erp.entity.BaseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utils {

    private static ObjectMapper mapper;
    private static Logger log = LoggerFactory.getLogger(Utils.class);

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

    public static String toJson(Object input, Class clazz) {
        initializeMapper();
        if (input != null) {
            try {
                return mapper.writeValueAsString(input);
            } catch (JsonProcessingException e) {
                log.warn("Cant toJson object " + clazz.getName() + " to JSON", e);
            }
        }
        return null;
    }

    public static Object fromJson(String input, Class clazz) {
        initializeMapper();
        if (input != null) {
            try {
                return mapper.readValue(input, clazz);
            } catch (IOException e) {
                log.warn("Cant toJson JSON " + input + " to Object", e);
            }
        }
        return null;
    }

    private static void initializeMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
    }

}
