package edu.bbte.idde.frim1910.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Car extends BaseEntity {
    private String brand;
    private String model;
    private String type;
    private Integer year;
    private Float engine;
}
