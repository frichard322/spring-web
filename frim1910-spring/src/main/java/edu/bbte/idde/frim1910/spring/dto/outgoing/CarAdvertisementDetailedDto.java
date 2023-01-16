package edu.bbte.idde.frim1910.spring.dto.outgoing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CarAdvertisementDetailedDto extends CarAdvertisementReducedDto {
    private Date creationDate;
    private Date modificationDate;
    private String description;
}
