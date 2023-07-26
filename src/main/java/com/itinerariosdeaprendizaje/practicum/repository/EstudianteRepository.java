package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.Estudiante;
import com.itinerariosdeaprendizaje.practicum.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
}
