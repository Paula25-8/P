package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Estudiante;
import com.itinerariosdeaprendizaje.practicum.model.PesoCriterio;
import com.itinerariosdeaprendizaje.practicum.model.RelacionEstudianteTutorCentro;
import com.itinerariosdeaprendizaje.practicum.repository.PesoCriterioRepository;
import com.itinerariosdeaprendizaje.practicum.repository.RelacionEstudianteTutorCentroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelacionEstudianteTutorCentroService {
    @Autowired
    private RelacionEstudianteTutorCentroRepository relacionEstudianteTutorCentroRepository;

    public RelacionEstudianteTutorCentro getRelacionEstudianteTutorCentroPorId(RelacionEstudianteTutorCentro.IdRelacionEstudTutorCentro id){
        Optional<RelacionEstudianteTutorCentro> opt = relacionEstudianteTutorCentroRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        else{
            return null;
        }
    }

    public List<RelacionEstudianteTutorCentro> getTutoresPorEstudiante(Estudiante estudiante){
        return relacionEstudianteTutorCentroRepository.findByEstudiante(estudiante);
    }
}
