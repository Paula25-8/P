package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Estudiante;
import com.itinerariosdeaprendizaje.practicum.model.TutorUR;
import com.itinerariosdeaprendizaje.practicum.repository.TutorURRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TutorURService {
    @Autowired
    private TutorURRepository tutorURRepository;
    public TutorUR getTutorURPorCodnum(Integer codnum){
        Optional<TutorUR> opt = tutorURRepository.findById(codnum);
        if(opt.isPresent()){
            return opt.get();
        }else {
            return null;
        }
    }

}
