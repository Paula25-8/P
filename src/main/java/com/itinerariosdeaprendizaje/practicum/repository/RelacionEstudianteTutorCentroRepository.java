package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.Estudiante;
import com.itinerariosdeaprendizaje.practicum.model.RelacionEstudianteTutorCentro;
import com.itinerariosdeaprendizaje.practicum.model.TutorCentro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelacionEstudianteTutorCentroRepository extends JpaRepository<RelacionEstudianteTutorCentro, RelacionEstudianteTutorCentro.IdRelacionEstudTutorCentro> {
    List<RelacionEstudianteTutorCentro> findByEstudiante(Estudiante estudiante);
    List<RelacionEstudianteTutorCentro> findByTutorCentro(TutorCentro tutorCentro);
}
