package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.Actividad;
import com.itinerariosdeaprendizaje.practicum.model.EstacionAprend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActividadRepository extends JpaRepository<Actividad, Integer> {
    List<Actividad> findByEstacionAprend(EstacionAprend estacion);
}
