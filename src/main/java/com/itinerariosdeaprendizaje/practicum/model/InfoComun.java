package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name="INFO_COMUN", schema="USER_UR")
public class InfoComun {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INFO_COMUN")
    @SequenceGenerator(name = "INFO_COMUN", sequenceName = "INFO_COMUN_SEQ", allocationSize = 1)
    @Column(name="ID_INFO")
    private Integer id;

    @NotNull
    @Size(max=1000)
    private String observaciones;

    @NotNull
    @Lob
    private String reflexion;

    @NotNull
    @Size(max=300)
    private String evaluacion;

    @NotNull
    private String curso;
}
