package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Data
@MappedSuperclass
public class OwnerEntity extends BaseEntity {

    protected UUID owner;

}
