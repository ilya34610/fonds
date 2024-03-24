package ru.pssbd.fonds.entity;

import javax.persistence.*;

@Entity
@Table(name = "donation_types")
public class DonationTypeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = "name")
    private String name;

}
