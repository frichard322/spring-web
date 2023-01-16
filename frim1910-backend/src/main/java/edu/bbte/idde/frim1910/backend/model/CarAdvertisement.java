package edu.bbte.idde.frim1910.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CarAdvertisement extends BaseEntity {
    private String title;
    private String description;
    private UUID carUuid;
    private Date date;
    private Float price;
}
