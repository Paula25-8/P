package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entity que representa un usuario con rol 'coordinador'.
 *
 * @author patoledo
 */

@Entity
@Data
@Table(name = "COORDINADOR",schema="USER_UR")
public class Coordinador extends Usuario{
}
