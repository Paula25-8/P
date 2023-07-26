package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaTutorCentroRepository extends JpaRepository<NotaTutorCentro, NotaTutorCentro.IdNotaTutorCentro> {
    List<NotaTutorCentro> findByPracticum(Practicum practicum);
    List<NotaTutorCentro> findByTutorCentro(TutorCentro tutorCentro);
}
