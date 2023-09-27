package com.itinerariosdeaprendizaje.practicum.model;

import lombok.*;

import java.util.Date;

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
    //private Dossier dossier; // lo cojo del PRACTICUM
    //private Integer convocatoria; // lo cojo del PRACTICUM
    private boolean evaluado;
}