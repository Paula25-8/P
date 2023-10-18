package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.PreguntaGeneral;
import com.itinerariosdeaprendizaje.practicum.repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    public List<PreguntaGeneral> getPreguntasPorCurso (String curso){
        List<PreguntaGeneral> estaciones=preguntaRepository.findByCurso(curso);
        if(!estaciones.isEmpty()){
            return estaciones;
        }else{
            return null;
        }
    }

    public PreguntaGeneral getPregunta(Integer cod) {
        Optional<PreguntaGeneral> opt=preguntaRepository.findById(cod);
        if(opt.isPresent()){
            return opt.get();
        }else{
            return null;
        }
    }

    public PreguntaGeneral guardarPregunta(PreguntaGeneral pregunta) {
        return preguntaRepository.save(pregunta);
    }

    public ArrayList<PreguntaGeneral> getPreguntas() {
        return (ArrayList<PreguntaGeneral>) preguntaRepository.findAll();
    }

}
