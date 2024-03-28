package ru.pssbd.fonds.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "catigories")
@Data
public class CategoryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(name = "name")
    private String name;

    public CategoryEntity(String name) {
        this.name = name;
    }
}
