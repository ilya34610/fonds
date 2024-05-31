package ru.pssbd.fonds.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "citizens")
@Data
@NoArgsConstructor
public class CitizenEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "id_city")
    private CityEntity city;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @Column(name = "sum")
    private BigDecimal sum;

    @ManyToMany(mappedBy = "citizens")
    private List<FondEntity> fonds;

    @ManyToMany
    @JoinTable(name = "citizens_categories",
            joinColumns = @JoinColumn(name = "id_citizen"),
            inverseJoinColumns = @JoinColumn(name = "id_category"))
    private List<CategoryEntity> categories;

    public CitizenEntity(CityEntity city, LocalDate birthDate) {
        this.city = city;
        this.birthDate = birthDate;
    }
}
