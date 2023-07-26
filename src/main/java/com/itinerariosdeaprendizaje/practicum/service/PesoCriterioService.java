package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.PesoCriterio;
import com.itinerariosdeaprendizaje.practicum.repository.PesoCriterioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PesoCriterioService {
    @Autowired
    private PesoCriterioRepository pesoCriterioRepository;

    public PesoCriterio getPesoCriterioPorId(PesoCriterio.IdPesoCriterio id){
        Optional<PesoCriterio> opt = pesoCriterioRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        else{
            return null;
        }
    }
}
