package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Actividad;
import com.itinerariosdeaprendizaje.practicum.model.EstacionAprend;
import com.itinerariosdeaprendizaje.practicum.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    public List<Actividad> getActividadesPorEstacion(EstacionAprend estacion){
        return actividadRepository.findByEstacionAprend(estacion);
    }
}
