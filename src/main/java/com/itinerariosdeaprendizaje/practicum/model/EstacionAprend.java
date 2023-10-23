package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

/**
 * Entity que representa una Estación de Aprendizaje a través de la cual se propone una actividad que llevar a cabo en el centro educativo.
 * Modelados campos en el mismo orden en que aparecen en la tabla ESTACION_APREND.
 *
 * @author patoledo
 */

@Entity
@Data
@Table(name="ESTACION_APREND", schema="USER_UR")
public class EstacionAprend {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTACION_APREND")
    @SequenceGenerator(name = "ESTACION_APREND", sequenceName = "ESTACION_APREND_SEQ", allocationSize = 1)
    @Column(name="ID_ESTACION")
    private Integer id;

    @NotNull
    @Size(max = 200)
    private String titulo;

    @NotNull
    @Size(max = 3000)
    private String justificacion;

    @NotNull
    private String curso;

    @ManyToOne
    @JoinColumn(name="ID_INFO_COMUN", nullable = false)
    private InfoComun info;

    @ManyToOne
    @JoinColumn(name="ID_LINEA", nullable = false)
    private LineaAprend linea;

    @ManyToMany
    @JoinTable(name="ESTACION_COMPET",
            joinColumns = {
                    @JoinColumn(name="ID_ESTACION", referencedColumnName = "ID_ESTACION")},
            inverseJoinColumns = {
                    @JoinColumn(name="ID_COMP", referencedColumnName = "ID_CE")})
    private List<CompEspecifica> competencias;

    @ManyToMany(mappedBy = "estaciones")
    private List<Mencion> menciones;

}