package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.SelloCalidad;
import com.itinerariosdeaprendizaje.practicum.repository.SelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SelloService {

    @Autowired
    private SelloRepository selloRepository;

    public ArrayList<SelloCalidad> getSellos (){
        List<SelloCalidad> sellos= selloRepository.findAll();
        return new ArrayList<SelloCalidad>(sellos);
    }

    public SelloCalidad getSelloById(Integer cod){
        Optional<SelloCalidad> sello = selloRepository.findById(cod);
        if(sello.isPresent()){
            return sello.get();
        }else{
            return null;
        }
    }

    public SelloCalidad getSelloPorCurso(String curso){
        List<SelloCalidad> sellos=selloRepository.findByCurso(curso);
        if(sellos.size()==1){
            return sellos.get(0);
        }else{
            return null;
        }
    }

}
