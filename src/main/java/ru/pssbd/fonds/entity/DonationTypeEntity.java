package ru.pssbd.fonds.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany(mappedBy = "donationTypes")
    private List<CapitalSourceEntity> capitalSources;



    public DonationTypeEntity(String name) {
        this.name = name;
    }
}
