package com.itinerariosdeaprendizaje.practicum.utils;

import lombok.RequiredArgsConstructor;

/**
 * Nivel de indicador de los distintos criterios de las rúbricas de evaluacion.
 *
 * @author patoledo
 */

@RequiredArgsConstructor
public enum Nivelndicador {
    SOBRESALIENTE ("Sobresaliente"),
    NOTABLE ("Notable"),
    APROBADO ("Aprobado"),
    SUSPENSO ("Suspenso");

    public final String descripcion;
}

