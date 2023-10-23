package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

/**
 * Entity que representa una Línea de Aprendizaje, es decir, cada uno de los campos dentro del ámbito educativo que se ofrecen para llevar a cabo actividades.
 * Modelados campos en el mismo orden en que aparecen en la tabla LINEA_APREND.
 *
 * @author patoledo
 */

@Entity
@Data
@Table(name="LINEA_APREND", schema="USER_UR")
public class LineaAprend {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LINEA_APREND")
    @SequenceGenerator(name = "LINEA_APREND", sequenceName = "LINEA_APREND_SEQ", allocationSize = 1)
    @Column(name="ID_LINEA")
    private Integer id;
    @NotNull
    @Column(name="numero")
    private Integer numeroLinea;
    @NotNull
    @Size(max = 50)
    private String titulo;

    @NotNull
    @Size(max = 2000)
    private String descripcion;

    @NotNull
    private String curso;

    @ManyToMany(mappedBy = "lineas")
    private List<Grado> grados;
}
