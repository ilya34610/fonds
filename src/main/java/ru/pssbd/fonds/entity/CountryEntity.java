package ru.pssbd.fonds.entity;

import javax.persistence.*;

@Entity
@Table(name = "countries")
public class CountryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

}
