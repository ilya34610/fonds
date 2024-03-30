package ru.pssbd.fonds.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column(name = "fio")
    private String fio;

    @ManyToOne
    @JoinColumn(name = "id_city")
    private CityEntity city;

    @Column(name = "birth_date")
    private LocalDate birth_date;

    @ManyToMany
    @JoinTable(name = "citizens_donations",
            joinColumns = @JoinColumn(name = "id_citizen"),
            inverseJoinColumns = @JoinColumn(name = "id_donation")
    )
    private List<DonationEntity> donations;

    @ManyToMany
    @JoinTable(name = "citizens_categories",
            joinColumns = @JoinColumn(name = "id_citizen"),
            inverseJoinColumns = @JoinColumn(name = "id_category"))
    private List<CategoryEntity> categories;

    @ManyToMany
    @JoinTable(name = "citizens_fonds",
            joinColumns = @JoinColumn(name = "id_citizen"),
            inverseJoinColumns = @JoinColumn(name = "id_fonds"))
    private List<FondEntity> fonds;

    public CitizenEntity(String fio, CityEntity city, LocalDate birthDate) {
        this.fio = fio;
        this.city = city;
        this.birth_date = birthDate;
    }
}
