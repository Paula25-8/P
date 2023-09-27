package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.Estudiante;
import com.itinerariosdeaprendizaje.practicum.model.Practicum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.ls.LSInput;

import java.util.List;
import java.util.Optional;

public interface PracticumRepository extends JpaRepository<Practicum, Integer> {

     List<Practicum> findByEstudiante(Estudiante estudiante);
     Practicum findByEstudianteAndCursoAndConvocatoria(Estudiante estudiante, String curso, Integer convocatoria);

}
