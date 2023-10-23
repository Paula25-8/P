package com.itinerariosdeaprendizaje.practicum.model;

import lombok.*;

/**
 * Clase creada para facilitar la impresión del listado de alumnos matriculados en la asignatura del prácticum.
 *
 * @author patoledo
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EstudianteTabla {
    private Integer id;
    private String nombre;
    private Practicum practicum;
    private Integer numItinerarios;
    private Grado gradoActual;
    private Itinerario itinerarioActivo;
    private boolean evaluado;
}