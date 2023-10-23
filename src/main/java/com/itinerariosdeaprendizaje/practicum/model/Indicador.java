package com.itinerariosdeaprendizaje.practicum.model;

import com.itinerariosdeaprendizaje.practicum.utils.Nivelndicador;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Entity que representa un Indicador de los cuatro que componen un criterio de una rúbrica de evaluación concreta.
 * Modelados campos en el mismo orden en que aparecen en la tabla INDICADOR.
 *
 * @author patoledo
 */

@Entity
@Data
@Table(name="INDICADOR", schema="USER_UR")
public class Indicador {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "INDICADOR")
    @SequenceGenerator(name = "INDICADOR", sequenceName = "INDICADOR_SEQ", allocationSize = 1)
    @Column(name="ID_INDICADOR")
    private Integer id;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Nivelndicador nivel;

    @NotNull
    private String descripcion;

    @NotNull
    private String curso;

}
