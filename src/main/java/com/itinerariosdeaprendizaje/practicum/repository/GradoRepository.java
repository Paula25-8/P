package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.Grado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradoRepository extends JpaRepository<Grado, Integer> {
    Grado findByNombreAndCurso(String nombre, String curso);
}
