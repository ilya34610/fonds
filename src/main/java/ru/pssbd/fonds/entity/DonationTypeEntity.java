package ru.pssbd.fonds.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "donation_types")
@Data
public class DonationTypeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = "name")
    private String name;

    public DonationTypeEntity(String name) {
        this.name = name;
    }
}
