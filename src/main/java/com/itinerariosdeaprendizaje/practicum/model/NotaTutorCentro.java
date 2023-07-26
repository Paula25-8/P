package com.itinerariosdeaprendizaje.practicum.model;

import com.itinerariosdeaprendizaje.practicum.utils.Especialidad;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name="NOTA_TUTOR_CENTRO", schema="USER_UR")
public class NotaTutorCentro {
    @Embeddable
    @Data
    @NoArgsConstructor
    public static class IdNotaTutorCentro implements Serializable {
        @Column(name="TUTOR_CENTRO")
        private Integer codnumTutorCentro;
        @Column(name="PRACTICUM")
        private Integer id_practicum;
    }
    @EmbeddedId
    private IdNotaTutorCentro id=new IdNotaTutorCentro();

    private float nota;

    @Temporal(TemporalType.DATE)
    @Column(name="FECH_NOTA")
    private Date fechaNota;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name="TUTOR_CENTRO", nullable=false, insertable = false, updatable = false)
    private TutorCentro tutorCentro;
    @ManyToOne
    @JoinColumn(name="PRACTICUM", nullable=false, insertable = false, updatable = false)
    private Practicum practicum;
}
