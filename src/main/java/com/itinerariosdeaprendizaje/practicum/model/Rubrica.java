package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="RUBRICA", schema="USER_UR")
public class Rubrica {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RUBRICA")
    @SequenceGenerator(name = "RUBRICA", sequenceName = "RUBRICA_SEQ", allocationSize = 1)
    @Column(name="ID_RUBRICA")
    private Integer id;

    @NotNull
    private String curso;

    @ManyToMany
    @JoinTable(name="PESO_CRITERIO",
            joinColumns = {
                    @JoinColumn(name="ID_RUBRICA", referencedColumnName = "ID_RUBRICA")},
            inverseJoinColumns = {
                    @JoinColumn(name="ID_CRITERIO", referencedColumnName = "ID_CRITERIO")})
    private List<Criterio> criterios;

}
