package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entity que representa una Pregunta General a través de la cual se transmitirá la información general de la API.
 * Modelados campos en el mismo orden en que aparecen en la tabla PREGUNTA_GENERAL.
 *
 * @author patoledo
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="PREGUNTA_GENERAL", schema="USER_UR")
public class PreguntaGeneral {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PREGUNTA_GENERAL")
    @SequenceGenerator(name = "PREGUNTA_GENERAL", sequenceName = "PREGUNTA_GENERAL_SEQ", allocationSize = 1)
    @Column(name="COD_PREGUNTA")
    private Integer codigo;

    @NotNull
    private String enunciado;

    @NotNull
    @Lob
    private String respuesta;

    @NotNull
    private String curso;

}
