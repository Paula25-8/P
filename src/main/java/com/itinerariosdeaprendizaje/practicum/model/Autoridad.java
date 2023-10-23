package com.itinerariosdeaprendizaje.practicum.model;

import com.itinerariosdeaprendizaje.practicum.utils.TipoPerfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Entity que representa una Autoridad, la cual hace referencia a un rol que puede poseer un usuario para entrar en la API.
 * Modelados campos en el mismo orden en que aparecen en la tabla AUTORIDAD.
 *
 * @author patoledo
 */

@Entity
@Data
@Table(name = "AUTORIDAD", schema = "USER_UR")
public class Autoridad {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "AUTORIDAD")
    @SequenceGenerator(name = "AUTORIDAD", sequenceName = "AUTORIDAD_SEQ", allocationSize = 1)
    private String id;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TipoPerfil rol;
}
