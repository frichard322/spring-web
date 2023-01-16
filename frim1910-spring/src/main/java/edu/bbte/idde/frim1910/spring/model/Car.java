package edu.bbte.idde.frim1910.spring.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "car")
public class Car extends BaseEntity {
    @Column
    private String brand;
    @Column
    private String model;
    @Column
    private String type;
    @Column
    private Integer year;
    @Column
    private Float engine;
    @Column
    private Timestamp lastRent;

    @OneToMany(
            targetEntity = CarAdvertisement.class,
            mappedBy = "car",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CarAdvertisement> carAdvertisements = new ArrayList<>();
}
