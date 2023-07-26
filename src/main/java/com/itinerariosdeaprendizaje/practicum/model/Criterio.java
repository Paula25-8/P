package com.itinerariosdeaprendizaje.practicum.model;

import com.itinerariosdeaprendizaje.practicum.utils.TipoActividad;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="CRITERIO", schema="USER_UR")
public class Criterio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CRITERIO")
    @SequenceGenerator(name = "CRITERIO", sequenceName = "CRITERIO_SEQ", allocationSize = 1)
    @Column(name="ID_CRITERIO")
    private Integer id;

    @NotNull
    private String nombre;

    @NotNull
    private String curso;

    @ManyToMany
    @JoinTable(name="INDICADOR_CRITERIO",
            joinColumns = {
                    @JoinColumn(name="ID_CRITERIO", referencedColumnName = "ID_CRITERIO")},
            inverseJoinColumns = {
                    @JoinColumn(name="ID_INDICADOR", referencedColumnName = "ID_INDICADOR")})
    private List<Indicador> indicadores;

}
