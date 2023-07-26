package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.TutorCentro;
import com.itinerariosdeaprendizaje.practicum.repository.TutorCentroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TutorCentroService {
    @Autowired
    private TutorCentroRepository tutorCentroRepository;
    public TutorCentro getTutorCentroPorCodnum(Integer codnum){
        Optional<TutorCentro> opt = tutorCentroRepository.findById(codnum);
        if(opt.isPresent()){
            return opt.get();
        }else {
            return null;
        }
    }
}
