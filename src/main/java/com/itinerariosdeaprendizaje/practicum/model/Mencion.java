package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="MENCION", schema="USER_UR")
public class Mencion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MENCION")
    @SequenceGenerator(name = "MENCION", sequenceName = "MENCION_SEQ", allocationSize = 1)
    @Column(name="ID_MENCION")
    private Integer id;

    @NotNull
    private String nombre;

    @NotNull
    private String curso;

    @ManyToMany
    @JoinTable(name="MENCION_ESTACION",
            joinColumns = {
                    @JoinColumn(name="MENCION", referencedColumnName = "ID_MENCION")},
            inverseJoinColumns = {
                    @JoinColumn(name="ESTACION", referencedColumnName = "ID_ESTACION")})
    private List<EstacionAprend> estaciones;

    @ManyToMany
    @JoinTable(name="ESTUD_MENCION",
            joinColumns = {
                    @JoinColumn(name="MENCION", referencedColumnName = "ID_MENCION")},
            inverseJoinColumns = {
                    @JoinColumn(name="ESTUD", referencedColumnName = "CODNUM")})
    private List<Estudiante> estudiantes;

}
