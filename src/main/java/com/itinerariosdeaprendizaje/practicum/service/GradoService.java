package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Grado;
import com.itinerariosdeaprendizaje.practicum.repository.GradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GradoService {
    @Autowired
    private GradoRepository gradoRepository;

    public Grado getGradoPorId(Integer id){
        Optional<Grado> opt = gradoRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        else{
            return null;
        }
    }

}
