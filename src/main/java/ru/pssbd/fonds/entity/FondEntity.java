package ru.pssbd.fonds.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "fonds")
@Data
@NoArgsConstructor
public class FondEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_city")
    private CityEntity city;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @Column(name = "sum")
    private BigDecimal sum;

    @ManyToMany(mappedBy = "fonds")
    private List<CapitalSourceEntity> capitalSources;

    @ManyToMany
    @JoinTable(name = "citizens_fonds",
            joinColumns = @JoinColumn(name = "id_citizen"),
            inverseJoinColumns = @JoinColumn(name = "id_fond"))
    private List<CitizenEntity> citizens;

    @ManyToMany
    @JoinTable(name = "fonds_fond_expenses",
            joinColumns = @JoinColumn(name = "id_fonds"),
            inverseJoinColumns = @JoinColumn(name = "id_fond_expenses"))
    private List<FondExpenseEntity> fondExpenses;



    public FondEntity(String name, CityEntity city, LocalDate creationDate, String phone) {
        this.name = name;
        this.city = city;
        this.creationDate = creationDate;
        this.phone = phone;
    }
}
