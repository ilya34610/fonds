package ru.pssbd.fonds.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fonds")
public class FondEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_city")
    private CityEntity city;

    @Column(name = "creation_date")
    private LocalDate creation_date;

    @Column(name = "phone")
    private String phone;

}
