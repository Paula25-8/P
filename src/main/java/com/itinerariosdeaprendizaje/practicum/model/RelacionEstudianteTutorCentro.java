package com.itinerariosdeaprendizaje.practicum.model;

import com.itinerariosdeaprendizaje.practicum.utils.Especialidad;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name="ESTUD_TUTOR_CENTRO", schema="USER_UR")
public class RelacionEstudianteTutorCentro {
    @Embeddable
    @Data
    @NoArgsConstructor
    public static class IdRelacionEstudTutorCentro implements Serializable {
        @Column(name="ESTUD")
        private Integer codnum_Estudiante;
        @Column(name="TUTOR_CENTRO")
        private Integer codnum_tutorCentro;
    }
    @EmbeddedId
    private IdRelacionEstudTutorCentro id = new IdRelacionEstudTutorCentro();

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name="ESTUD", nullable=false, insertable = false, updatable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name="TUTOR_CENTRO", nullable=false, insertable = false, updatable = false)
    private TutorCentro tutorCentro;
}
