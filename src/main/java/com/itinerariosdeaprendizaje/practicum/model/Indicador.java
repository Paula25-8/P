package com.itinerariosdeaprendizaje.practicum.model;

import com.itinerariosdeaprendizaje.practicum.utils.Nivelndicador;
import com.itinerariosdeaprendizaje.practicum.utils.TipoActividad;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
