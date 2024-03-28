package ru.pssbd.fonds.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "currency_type")
@Data
public class CurrencyTypeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = "name")
    private String name;

    public CurrencyTypeEntity(String name) {
        this.name = name;
    }
}
