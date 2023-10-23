package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Entity que representa un Itinerario realizado por un alumno.
 * Modelados campos en el mismo orden en que aparecen en la tabla ITINERARIO.
 *
 * @author patoledo
 */

@Entity
@Data
@NoArgsConstructor
@Table(name="ITINERARIO", schema="USER_UR")
public class Itinerario {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "ITINERARIO")
    @SequenceGenerator(name = "ITINERARIO", sequenceName = "ITINERARIO_SEQ", allocationSize = 1)
    @Column(name="ID_ITINERARIO")
    private Integer id;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "FECH_CREACION")
    private Date fechaCreacion;

}
