package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Entity que representa una Competencia Específica.
 * Modelados campos en el mismo orden en que aparecen en la tabla COMPET_ESPEC.
 *
 * @author patoledo
 */

@Entity
@Data
@Table(name = "COMPET_ESPEC", schema = "USER_UR")
public class CompEspecifica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPET_ESPEC")
    @SequenceGenerator(name = "COMPET_ESPEC", sequenceName = "COMPET_ESPEC_SEQ", allocationSize = 1)
    @Column(name="ID_CE")
    private Integer id;

    @Column(name="CODIGO_CE")
    private String codigo;

    @NotNull
    @Size(max=50)
    private String descripcion;

    @NotNull
    private String curso;

}
