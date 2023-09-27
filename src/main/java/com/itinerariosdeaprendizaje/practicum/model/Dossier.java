package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Entity
@Data
/*@Getter
@Setter
@ToString*/
@NoArgsConstructor
@AllArgsConstructor
@Table(name="DOSSIER", schema="USER_UR")
public class Dossier {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOSSIER")
    @SequenceGenerator(name = "DOSSIER", sequenceName = "DOSSIER_SEQ", allocationSize = 1)
    @Column(name="ID_DOSSIER")
    private Integer id;
    @Nullable
    @Column(name="DOCU")
    private String nombreDocu;
    @Temporal(TemporalType.DATE)
    @Column(name="FECH_ENTREGA")
    private Date fechaEntrega;
    @Column(name="FEEDBACK_TUTOR_UR")
    @Size(max=4000)
    private String feedbackTutorUR;
    @Temporal(TemporalType.DATE)
    @Column(name="FECH_FEEDBACK")
    private Date fechaFeedback;
    @OneToOne
    @JoinColumn(name="PRACTICUM", nullable = false)
    private Practicum practicum;
}
