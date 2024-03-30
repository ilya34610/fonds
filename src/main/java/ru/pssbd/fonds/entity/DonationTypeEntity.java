package ru.pssbd.fonds.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "donation_types")
@Data
@NoArgsConstructor
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
