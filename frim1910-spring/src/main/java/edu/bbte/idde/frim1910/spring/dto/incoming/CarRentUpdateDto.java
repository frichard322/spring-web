package edu.bbte.idde.frim1910.spring.dto.incoming;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class CarRentUpdateDto implements Serializable {
    @NotEmpty
    private String brand;
    @NotEmpty
    private String model;
}
