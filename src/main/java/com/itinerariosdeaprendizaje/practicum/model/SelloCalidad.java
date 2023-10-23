package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

/**
 * Entity que representa el Sello de Calidad, elemento que recoge las características a seguir para realizar una buena entrega de trabajo.
 * Modelados campos en el mismo orden en que aparecen en la tabla SELLO_CALIDAD.
 *
 * @author patoledo
 */

@Entity
@Data
@Table(name="SELLO_CALIDAD", schema="USER_UR")
public class SelloCalidad {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SELLO_CALIDAD")
    @SequenceGenerator(name = "SELLO_CALIDAD", sequenceName = "SELLO_CALIDAD_SEQ", allocationSize = 1)
    @Column(name="COD_SELLO")
    private Integer codigo;

    @NotNull
    private String curso;

    @ManyToMany
    @JoinTable(name="SELLO_CARACTERISTICA",
    joinColumns = {
            @JoinColumn(name="COD_SELLO", referencedColumnName = "COD_SELLO")},
    inverseJoinColumns = {
            @JoinColumn(name="ID_CARACT", referencedColumnName = "ID_CARACT")})
    private List<Caracteristica> caracteristicas;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class ItinerarioForm {
        @NotNull(message = "Debe seleccionar una estación de la línea de aprendizaje 1")
        private String estacionLinea1;
        @NotNull(message = "Debe seleccionar una estación de la línea de aprendizaje 2")
        private String estacionLinea2;
        @NotNull(message = "Debe seleccionar una estación de la línea de aprendizaje 3")
        private String estacionLinea3;
        @NotNull(message = "Debe seleccionar una estación de la línea de aprendizaje 4")
        private String estacionLinea4;

    }
}
