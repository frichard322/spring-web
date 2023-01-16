package edu.bbte.idde.frim1910.spring.dto.incoming;

import lombok.Data;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
public class CarAdvertisementUpdateDto implements Serializable {
    private String title;
    private String description;
    private UUID carId;
    @PastOrPresent
    private Date modificationDate = new Date();
    @PositiveOrZero
    private Float price;
}
