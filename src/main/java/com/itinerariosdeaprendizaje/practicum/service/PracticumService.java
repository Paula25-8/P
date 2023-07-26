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
        System.out.println(opt);
        if(opt.isPresent()){
            System.out.println("El practicum no es nulo");
            return opt.get();
        }else{
            System.out.println("El practicum sí es nulo");
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
        Practicum practicum = practicumRepository.getReferenceById(pract.getId());
        if(primero!=null){
            System.out.println("caso primero != null"+": "+primero);
            practicum.setPrimerItinerario(primero);}
        System.out.println("pasamos a comprobar valor de ultimo itinerario");
        if(ultimo!=null){
            System.out.println("caso primero != null");
            practicum.setUltimoItinerario(ultimo);}
        System.out.println("guardamos praticum modificado en BD");
        return practicum;
    }
    
}
