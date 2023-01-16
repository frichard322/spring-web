package edu.bbte.idde.frim1910.spring.dto.incoming;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.Date;

@Data
public class CarAdvertisementCreationDto implements Serializable {
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @PastOrPresent
    private Date creationDate = new Date();
    @NotNull
    @PositiveOrZero
    private Float price;
}
