package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.EstacionAprend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstacionAprendRepository extends JpaRepository<EstacionAprend, Integer> {

    List<EstacionAprend> findByCurso(String curso);
    List<EstacionAprend> findByTitulo(String titulo);

}
