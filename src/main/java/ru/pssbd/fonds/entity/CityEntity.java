package ru.pssbd.fonds.entity;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "cities")
public class CityEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_country")
    private CountryEntity country;

}
