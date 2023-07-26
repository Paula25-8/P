package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.LineaAprend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineasAprendRepository extends JpaRepository<LineaAprend, Integer> {
    List<LineaAprend> findByCurso(String curso);
}
