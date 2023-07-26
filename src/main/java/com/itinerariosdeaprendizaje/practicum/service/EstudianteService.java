package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Estudiante;
import com.itinerariosdeaprendizaje.practicum.model.Practicum;
import com.itinerariosdeaprendizaje.practicum.model.PreguntaGeneral;
import com.itinerariosdeaprendizaje.practicum.repository.EstudianteRepository;
import com.itinerariosdeaprendizaje.practicum.utils.MetodosGenerales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;
    public Estudiante getEstudiantePorCodnum(Integer codnum){
        Optional<Estudiante> opt=estudianteRepository.findById(codnum);
        if(opt.isPresent()){
            return opt.get();
        }else {
            return null;
        }
    }


}
