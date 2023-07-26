package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import oracle.sql.CLOB;

/**
 * Entity que representa un Pregunta General. Modelados campos en el mismo orden
 * en que aparecen en la tabla PREGUNTA_GENERAL.
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
