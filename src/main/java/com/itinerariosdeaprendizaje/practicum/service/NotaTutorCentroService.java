package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.*;
import com.itinerariosdeaprendizaje.practicum.repository.NotaTutorCentroRepository;
import com.itinerariosdeaprendizaje.practicum.repository.PesoCriterioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotaTutorCentroService {
    @Autowired
    private NotaTutorCentroRepository notaTutorCentroRepository;

    public NotaTutorCentro getNotaTutorCentroPorId(NotaTutorCentro.IdNotaTutorCentro id){
        Optional<NotaTutorCentro> opt = notaTutorCentroRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        else{
            return null;
        }
    }

    public NotaTutorCentro guardarNotaTutorCentro(NotaTutorCentro nota){
        return notaTutorCentroRepository.save(nota);
    }

    public void eliminarNotaTutorCentro(NotaTutorCentro nota){
        notaTutorCentroRepository.delete(nota);
    }

    public List<NotaTutorCentro> getNotasTutoresPorPracticum(Practicum practicum){
        return notaTutorCentroRepository.findByPracticum(practicum);
    }
    public List<Estudiante> getEstudiantesPorTutor(TutorCentro tutorCentro){
        System.out.println("Buscamos los alumnos de: "+tutorCentro.getNombre());
        List<NotaTutorCentro> notas = notaTutorCentroRepository.findByTutorCentro(tutorCentro);
        System.out.println("Tamaño de las notas: "+notas.size());
        List<Estudiante> tutorizados = new ArrayList<>();
        for(int i=0;i<notas.size();i++){
            System.out.println(notas.get(i).getPracticum().getId());
            //tutorizados.add(notas.get(i).getPracticum().getEstudiante());
        }
        return tutorizados;
    }
}
