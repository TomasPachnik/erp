package sk.tomas.erp.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import sk.tomas.erp.bo.Paging;
import sk.tomas.erp.bo.PagingOutput;
import sk.tomas.erp.entity.BaseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

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
        return o == null ? "created" : "updated";
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

    @SuppressWarnings("unchecked")
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

    public static Paging mapPaging(Page<? extends BaseEntity> page, ModelMapper mapper, Type listType) {
        Paging paging = new Paging();
        paging.setTotal((int) page.getTotalElements());
        paging.setPageable(new PagingOutput(page.getPageable().getPageNumber(), page.getPageable().getPageSize()));
        paging.setContent(mapper.map(page.getContent(), listType));
        return paging;
    }

    public static byte[] zip(Object o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(new GZIPOutputStream(baos));
        objectOut.writeObject(o);
        objectOut.close();
        return baos.toByteArray();
    }

}
