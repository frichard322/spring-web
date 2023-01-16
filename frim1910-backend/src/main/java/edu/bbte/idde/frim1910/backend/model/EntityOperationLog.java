package edu.bbte.idde.frim1910.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EntityOperationLog extends BaseEntity {
    private Timestamp date;
    private String operation; // create, update, delete
    private String entityName;
    private UUID entityId;
}
