package ru.pssbd.fonds.entity;

import javax.persistence.*;

@Entity
@Table(name = "currency_type")
public class CurrencyTypeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = "name")
    private String name;

}
