package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.Rubrica;
import com.itinerariosdeaprendizaje.practicum.model.SelloCalidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RubricaRepository extends JpaRepository<Rubrica, Integer> {
    List<Rubrica> findByCurso(String curso);
}
