package ru.pssbd.fonds.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fonds")
@Data
public class FondEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_city")
    private CityEntity city;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "phone")
    private String phone;

    public FondEntity(String name, CityEntity city, LocalDate creationDate, String phone) {
        this.name = name;
        this.city = city;
        this.creationDate = creationDate;
        this.phone = phone;
    }
}
