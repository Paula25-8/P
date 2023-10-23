package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entity que representa un usuario con rol 'administrador'.
 *
 * @author patoledo
 */

@Entity
@Data
@Table(name = "ADMINISTRADOR", schema = "USER_UR")
public class Admnistrador extends Usuario{
}
