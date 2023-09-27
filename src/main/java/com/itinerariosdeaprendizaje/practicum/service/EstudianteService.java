package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Estudiante;
import com.itinerariosdeaprendizaje.practicum.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Estudiante> getEstudiantes(){
        return estudianteRepository.findAll();
    }

}
