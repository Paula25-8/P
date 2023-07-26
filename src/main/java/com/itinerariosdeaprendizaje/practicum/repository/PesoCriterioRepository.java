package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.EntregaEstacion;
import com.itinerariosdeaprendizaje.practicum.model.PesoCriterio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PesoCriterioRepository  extends JpaRepository<PesoCriterio, PesoCriterio.IdPesoCriterio> {
}
