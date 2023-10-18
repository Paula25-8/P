package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@Table(name = "CARACTERISTICA", schema = "USER_UR")
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARACTERISTICA")
    @SequenceGenerator(name = "CARACTERISTICA", sequenceName = "CARACTERISTICA_SEQ", allocationSize = 1)
    @Column(name="ID_CARACT")
    private Integer id;

    @NotNull
    private String nombre;

    @NotNull
    @Size(max=300)
    private String descripcion;

    @NotNull
    private String curso;

}
