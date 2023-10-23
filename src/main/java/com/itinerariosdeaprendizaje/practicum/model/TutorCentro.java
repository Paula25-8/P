package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity que representa un usuario con rol de 'tutor de prácticas de un centro educativo'.
 *
 * @author patoledo
 */

@Entity
@Data
@Table(name = "TUTOR_CENTRO",schema="USER_UR")
public class TutorCentro extends Usuario{
}
