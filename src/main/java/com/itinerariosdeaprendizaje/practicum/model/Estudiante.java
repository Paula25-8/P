package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * Entity que representa un usuario con rol 'estudiante' el cual está matriculado en la asignatura de prácticas de su grado correspondiente.
 *
 * @author patoledo
 */

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table (name = "ESTUDIANTE",schema="USER_UR")
public class Estudiante extends Usuario{
    @ManyToOne
    @JoinColumn(name="TUTOR_UR", nullable = false)
    private TutorUR tutorUR;

    @ManyToMany(mappedBy = "estudiantes")
    private List<Mencion> menciones;

    @ManyToMany(mappedBy = "estudiantes")
    private List<Grado> grados;

    @OneToMany(mappedBy = "estudiante")
    private List<Practicum> practicums;

    public Grado obtenerGradoPorCurso(String curso){
        List<Grado> grados = this.getGrados();
        for(int i=0;i<grados.size();i++){
            if(grados.get(i).getCurso().equals(curso)){
                return grados.get(i);
            }
        }
        return null;
    }

    public Mencion obtenerMencionPorCurso(String curso){
        List<Mencion> menciones = this.getMenciones();
        for(int i=0;i<menciones.size();i++){
            if(menciones.get(i).getCurso().equals(curso)){
                return menciones.get(i);
            }
        }
        return null;
    }

    // Metodo que dice si un estudiante tiene algun itinerario creado en el practicum del curso academico y la convocatoria actuales
    public boolean tieneItinerarioCreado(String curso, Integer conv){
        List<Practicum> practicums = this.getPracticums();
        if(practicums.isEmpty()){return false;}
        for(int i=0;i<practicums.size();i++){
            Practicum practicum = practicums.get(i);
            if(practicum.getCurso().equals(curso) && practicum.getConvocatoria()==conv && practicum.getPrimerItinerario()!=null){
                return true;
            }
        }
        return false;
    }
}
