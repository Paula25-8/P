package com.itinerariosdeaprendizaje.practicum.model;

import com.itinerariosdeaprendizaje.practicum.utils.TipoActividad;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@Table(name="ACTIVIDAD", schema="USER_UR")
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
