package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="PRACTICUM", schema="USER_UR")
public class Practicum {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "PRACTICUM")
    @SequenceGenerator(name = "PRACTICUM", sequenceName = "PRACTICUM_SEQ", allocationSize = 1)
    @Column(name="ID_PRACT")
    private Integer id;
    @NotNull
    private String curso;
    @Column(name="NOTA_FINAL")
    private float notaFinal;
    @Temporal(TemporalType.DATE)
    @Column(name="FECH_NF")
    private Date fechaNF;
    @Column(name="NOTA_FINAL_MENCION")
    private float notaFinalMencion;
    @Temporal(TemporalType.DATE)
    @Column(name="FECH_NF_MENCION")
    private Date fechaNFMencion;
    @NotNull
    private Integer convocatoria;
    @ManyToOne
    @JoinColumn(name="ESTUDIANTE", nullable = false)
    private Estudiante estudiante;
    @ManyToOne
    @JoinColumn(name="TUTOR_UR", nullable = false)
    private TutorUR tutorUR;
    @Column(name="NOTA_TUTOR_UR")
    private float notaTutorUR;
    @Temporal(TemporalType.DATE)
    @Column(name="FECH_NOTA_TUTOR_UR")
    private Date fechaNotaTutorUR;
    @OneToOne
    @JoinColumn(name="ITINER_PRIMERO", nullable = false)
    private Itinerario primerItinerario;
    @OneToOne
    @JoinColumn(name="ITINER_ULTIMO", nullable = false)
    private Itinerario ultimoItinerario;
    @OneToOne(mappedBy = "practicum")
    private Dossier dossier;


    public boolean tieneItinerario(Integer idItinerario){
        if(this.getPrimerItinerario()!= null && this.getPrimerItinerario().getId()==idItinerario){
            return true;
        }
        else {
            if (this.getUltimoItinerario() != null && this.getUltimoItinerario().getId() == idItinerario) {
                return true;
            } else {
                return false;
            }
        }
    }

    public String caracterItinerario(Integer idItinerario){
        if(this.getPrimerItinerario()!=null && this.getPrimerItinerario().getId()==idItinerario){
            return "Inicial";
        }
        else {
            if(this.getUltimoItinerario()!=null && this.getUltimoItinerario().getId()==idItinerario){
                return "Final";
            }
            else{
                return null;
            }
        }
    }

    public Integer numItinerarios(){
        if(this.getPrimerItinerario()==null){
            return 0;
        }
        else{
            if(this.getPrimerItinerario()!=null && this.getUltimoItinerario()==null){
                return 1;
            }
            else{
                return 2;
            }
        }
    }

    public Itinerario getItinerarioActivo(){
        if(this.getPrimerItinerario()==null){
            return null;
        }
        else{
            if(this.getPrimerItinerario()!=null && this.getUltimoItinerario()==null){
                return this.getPrimerItinerario();
            }
            else{
                return this.getUltimoItinerario();
            }
        }
    }
}
