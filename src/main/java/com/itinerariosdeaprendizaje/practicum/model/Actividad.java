package com.itinerariosdeaprendizaje.practicum.model;

import com.itinerariosdeaprendizaje.practicum.utils.TipoActividad;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entity que representa una Actividad que forma parte de la información referente a una estación de aprendizaje concreta.
 * Modelados campos en el mismo orden en que aparecen en la tabla ACTIVIDAD.
 *
 * @author patoledo
 */

@Entity
@Data
@Table(name = "ACTIVIDAD", schema = "USER_UR")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ACTIVIDAD")
    @SequenceGenerator(name = "ACTIVIDAD", sequenceName = "ACTIVIDAD_SEQ", allocationSize = 1)
    @Column(name="ID_ACTIVIDAD")
    private Integer id;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TipoActividad tipo;

    @NotNull
    @Lob
    @Column(name="HTML_RESULTANTE")
    private String htmlResultante;

    @NotNull
    private String curso;

    @ManyToOne
    @JoinColumn(name="ID_ESTACION", nullable = false)
    private EstacionAprend estacionAprend;
}
