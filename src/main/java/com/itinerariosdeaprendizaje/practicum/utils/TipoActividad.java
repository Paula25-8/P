package com.itinerariosdeaprendizaje.practicum.utils;

import lombok.RequiredArgsConstructor;

/**
 * Tipo de actividad de cada estación de aprendizaje.
 *
 * @author patoledo
 */
@RequiredArgsConstructor
public enum TipoActividad {
    INICIO ("INICIO"),
    APLICACION ("APLICACION"),
    CIERRE ("CIERRE"),
    COMPLETA ("COMPLETA");

    public final String descripcion;
}