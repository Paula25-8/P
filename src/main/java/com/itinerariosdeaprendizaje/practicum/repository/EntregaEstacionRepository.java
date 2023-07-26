package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.EntregaEstacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntregaEstacionRepository extends JpaRepository<EntregaEstacion, EntregaEstacion.IdEntregaEstacion> {
    List<EntregaEstacion> findByCurso(String curso);
}
