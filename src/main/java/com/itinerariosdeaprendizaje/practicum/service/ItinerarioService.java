package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Itinerario;
import com.itinerariosdeaprendizaje.practicum.model.Practicum;
import com.itinerariosdeaprendizaje.practicum.model.PreguntaGeneral;
import com.itinerariosdeaprendizaje.practicum.repository.ItinerarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItinerarioService {
    @Autowired
    private ItinerarioRepository itinerarioRepository;

    public Itinerario guardarItinerario(Itinerario itinerario){
        return itinerarioRepository.save(itinerario);
    }

    public Itinerario getItinerarioPorCod(Integer cod) {
        Optional<Itinerario> opt=itinerarioRepository.findById(cod);
        if(opt.isPresent()){
            return opt.get();
        }else{
            return null;
        }
    }

}
