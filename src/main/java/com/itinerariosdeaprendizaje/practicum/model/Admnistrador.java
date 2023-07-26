package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ADMINISTRADOR",schema="USER_UR")
public class Admnistrador extends Usuario{
}
