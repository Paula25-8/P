package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.PreguntaGeneral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreguntaRepository extends JpaRepository<PreguntaGeneral, Integer> {
    List<PreguntaGeneral> findByCurso(String curso);
}
