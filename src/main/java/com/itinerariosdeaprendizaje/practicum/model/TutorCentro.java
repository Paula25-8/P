package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "TUTOR_CENTRO",schema="USER_UR")
public class TutorCentro extends Usuario{
    /*@ManyToMany(mappedBy = "tutoresCentro")
    private List<Estudiante> tutorizados;*/
}
