package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Rubrica;
import com.itinerariosdeaprendizaje.practicum.model.SelloCalidad;
import com.itinerariosdeaprendizaje.practicum.repository.RubricaRepository;
import com.itinerariosdeaprendizaje.practicum.repository.SelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RubricaService {
    @Autowired
    private RubricaRepository rubricaRepository;

    public ArrayList<Rubrica> getRubricas (){
        List<Rubrica> rubricas = rubricaRepository.findAll();
        return new ArrayList<Rubrica>(rubricas);
    }

    public Rubrica getRubricaById(Integer id){
        Optional<Rubrica> rubrica = rubricaRepository.findById(id);
        if(rubrica.isPresent()){
            return rubrica.get();
        }else{
            return null;
        }
    }

    public List<Rubrica> getRubricasPorCurso(String curso){
        return rubricaRepository.findByCurso(curso);
    }
}
