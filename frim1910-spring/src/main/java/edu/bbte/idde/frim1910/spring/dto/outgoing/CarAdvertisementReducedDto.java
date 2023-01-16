package edu.bbte.idde.frim1910.spring.dto.outgoing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CarAdvertisementReducedDto extends BaseEntityDto {
    private String title;
    private CarDto car;
    private Float price;
}

