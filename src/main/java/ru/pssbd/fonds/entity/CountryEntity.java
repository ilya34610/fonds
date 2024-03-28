package ru.pssbd.fonds.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "countries")
@Data
public class CountryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    public CountryEntity(String name) {
        this.name = name;
    }
}
