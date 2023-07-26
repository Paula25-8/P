package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name="ENTREGA_ESTACION", schema="USER_UR")
public class EntregaEstacion{
    @Embeddable
    @Data
    @NoArgsConstructor
    @ToString
    public static class IdEntregaEstacion implements Serializable {
        @Column(name="ITINERARIO")
        private Integer id_Itinerario;
        @Column(name="ESTACION")
        private Integer id_Estacion;
    }
    @EmbeddedId
    private IdEntregaEstacion id=new IdEntregaEstacion();
    @Nullable
    @Column(name="DOCU")
    private String nombreDocu;
    @Nullable
    @Temporal(TemporalType.DATE)
    @Column(name="FECH_ENTREGA")
    private Date fechaEntrega;
    @Nullable
    @Column(name="FEEDBACK_TUTOR_UR")
    @Size(max=4000)
    private String feedbackTutorUR;
    @Nullable
    @Temporal(TemporalType.DATE)
    @Column(name="FECH_FEEDBACK")
    private Date fechaFeedback;
    @NotNull
    private String curso;
    @ManyToOne
    @JoinColumn(name="ITINERARIO", nullable=false, insertable = false, updatable = false)
    private Itinerario itinerario;
    @ManyToOne
    @JoinColumn(name="ESTACION", nullable=false, insertable = false, updatable = false)
    private EstacionAprend estacion;

}
