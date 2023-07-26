package com.itinerariosdeaprendizaje.practicum.utils;

import lombok.RequiredArgsConstructor;

/**
 * Tipo de perfiles que pueden acceder a la aplicación de itinerarios.
 *
 * @author patoledo
 */

@RequiredArgsConstructor
public enum TipoPerfil {
    ROL_ESTUDIANTE ("ROL_ESTUDIANTE"),
    ROL_TUTOR_UR ("ROL_TUTOR_UR"),
    ROL_TUTOR_CENTRO ("ROL_TUTOR_CENTRO"),
    ROL_COORDINADOR ("ROL_COORDINADOR"),
    ROL_ADMINISTRADOR ("ROL_ADMINISTRADOR");

    public final String descripcion;
}
