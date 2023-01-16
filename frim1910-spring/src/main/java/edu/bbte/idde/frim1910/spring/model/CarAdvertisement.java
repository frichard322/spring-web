package edu.bbte.idde.frim1910.spring.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Date;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "caradvertisement")
public class CarAdvertisement extends BaseEntity {
    @Column
    private String title;
    @Column
    private String description;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "modification_date")
    private Date modificationDate;
    @Column
    private Float price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id")
    private Car car;
}
