package sk.tomas.erp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "audit")
public class AuditEntity extends OwnerEntity {

    private Date date;
    private String className;
    @Column(length = 4096)
    private String oldValue;
    @Column(length = 4096)
    private String newValue;

}
