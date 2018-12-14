package sk.tomas.erp;

import uk.co.jemos.podam.api.AttributeMetadata;
import uk.co.jemos.podam.api.DataProviderStrategy;
import uk.co.jemos.podam.typeManufacturers.StringTypeManufacturerImpl;

import java.lang.reflect.Type;
import java.util.Map;

public class ShortStringManufacturer extends StringTypeManufacturerImpl {

    @Override
    public String getType(DataProviderStrategy strategy,
                          AttributeMetadata attributeMetadata,
                          Map<String, Type> genericTypesArgumentsMap) {

        /*if (Pojo.class.isAssignableFrom(attributeMetadata.getPojoClass())) {

            if ("url".equals(attributeMetadata.getAttributeName())) {
                return "http://wikipedia.org";
            } else if ("postCode".equals(attributeMetadata.getAttributeName())) {
                return "00100";
            }
        }
        return super.getType(strategy, attributeMetadata, genericTypesArgumentsMap);*/

        return "emptyString";
    }

}

