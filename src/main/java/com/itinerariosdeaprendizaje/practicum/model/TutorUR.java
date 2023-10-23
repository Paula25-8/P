package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import java.util.List;

/**
 * Entity que representa un usuario con rol de 'tutor de prácticas de la UR'.
 * En él se recogen las principales características de dicho rol.
 *
 * @author patoledo
 */

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "TUTOR_UR",schema="USER_UR")
public class TutorUR extends Usuario{
    @OneToMany(mappedBy = "tutorUR")
    private List<Estudiante> tutorizados;

    @OneToMany(mappedBy = "tutorUR")
    private List<Practicum> practicums;

    public boolean tieneEstudiante(Estudiante estudiante){
        return this.getTutorizados().contains(estudiante);
    }
}
