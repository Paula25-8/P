package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="GRADO", schema="USER_UR")
public class Grado {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "GRADO")
    @SequenceGenerator(name = "GRADO", sequenceName = "GRADO_SEQ", allocationSize = 1)
    @Column(name="ID_GRADO")
    private Integer id;

    @NotNull
    private String codigo;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @NotNull
    private String curso;

    @ManyToOne
    @JoinColumn(name="RUB_TUTOR_UR", nullable = false)
    private Rubrica rubricaTutorUR;

    @ManyToOne
    @JoinColumn(name="RUB_TUTOR_CENTRO", nullable = false)
    private Rubrica rubricaTutorCentro;

    @ManyToOne
    @JoinColumn(name="SELLO", nullable=false)
    private SelloCalidad sello;

    @ManyToMany
    @JoinTable(name="GRADO_MENCION",
            joinColumns = {
                    @JoinColumn(name="GRADO", referencedColumnName = "ID_GRADO")},
            inverseJoinColumns = {
                    @JoinColumn(name="MENCION", referencedColumnName = "ID_MENCION")})
    private List<Mencion> menciones;

    @ManyToMany
    @JoinTable(name="GRADO_LINEA_APREND",
            joinColumns = {
                    @JoinColumn(name="GRADO", referencedColumnName = "ID_GRADO")},
            inverseJoinColumns = {
                    @JoinColumn(name="LINEA", referencedColumnName = "ID_LINEA")})
    private List<LineaAprend> lineas;

    @ManyToMany
    @JoinTable(name="ESTUD_GRADO",
            joinColumns = {
                    @JoinColumn(name="GRADO", referencedColumnName = "ID_GRADO")},
            inverseJoinColumns = {
                    @JoinColumn(name="ESTUD", referencedColumnName = "CODNUM")})
    private List<Estudiante> estudiantes;
}
