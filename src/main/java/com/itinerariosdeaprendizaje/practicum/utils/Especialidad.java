package com.itinerariosdeaprendizaje.practicum.utils;

import lombok.RequiredArgsConstructor;

/**
 * Indica si la calificacion que ponga el tutor de centro es la correspondiente a la parte de generalista o de mencion.
 *
 * @author patoledo
 */

@RequiredArgsConstructor
public enum Especialidad {
    GENERALISTA ("GENERALISTA"),
    MENCION ("MENCION");

    public final String descripcion;
}
