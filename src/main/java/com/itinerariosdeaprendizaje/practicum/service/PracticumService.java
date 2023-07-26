package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Estudiante;
import com.itinerariosdeaprendizaje.practicum.model.Itinerario;
import com.itinerariosdeaprendizaje.practicum.model.Practicum;
import com.itinerariosdeaprendizaje.practicum.model.PreguntaGeneral;
import com.itinerariosdeaprendizaje.practicum.repository.PracticumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PracticumService {

    @Autowired
    private PracticumRepository practicumRepository;

    public Practicum guardarPracticum(Practicum pract){
        return practicumRepository.save(pract);
    }

    public Practicum getPracticum(Integer id) {
        Optional<Practicum> opt = practicumRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }else{
            return null;
        }
    }

    public Practicum getPracticumActivoEstudiante(Estudiante estudiante, String curso, Integer conv){
        return practicumRepository.findByEstudianteAndCursoAndConvocatoria(estudiante, curso, conv);
    }

    public ArrayList<Practicum> getPracticums() {
        return (ArrayList<Practicum>) practicumRepository.findAll();
    }

    public Practicum modificarItinerariosPracticum(Practicum pract, Itinerario primero, Itinerario ultimo){
        Practicum practicum = this.getPracticum(pract.getId());
        if(primero!=null){
            practicum.setPrimerItinerario(primero);
        }
        if(ultimo!=null){
            practicum.setUltimoItinerario(ultimo);
        }
        return practicum;
    }
    
}
