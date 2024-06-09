package ru.pssbd.fonds.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "citizens_fonds")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizensFondsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_citizen")
    private CitizenEntity citizen;

    @ManyToOne
    @JoinColumn(name = "id_fond")
    private FondEntity fond;
}

