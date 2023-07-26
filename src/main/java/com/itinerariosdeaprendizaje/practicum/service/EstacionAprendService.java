package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.EstacionAprend;
import com.itinerariosdeaprendizaje.practicum.model.PreguntaGeneral;
import com.itinerariosdeaprendizaje.practicum.repository.EstacionAprendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstacionAprendService {

    @Autowired
    EstacionAprendRepository estacionAprendRepository;

    public List<EstacionAprend> getEstacionesPorCurso (String curso){
        List<EstacionAprend> estaciones=estacionAprendRepository.findByCurso(curso);
        if(!estaciones.isEmpty()){
            return estaciones;
        }else{
            return null;
        }
    }
    public EstacionAprend getEstacionPorTitulo (String titulo){
        List<EstacionAprend> estaciones=estacionAprendRepository.findByTitulo(titulo);
        if(!estaciones.isEmpty() && estaciones.size()==1){
            return estaciones.get(0);
        }else{
            return null;
        }
    }

    public EstacionAprend getEstacionPorId(Integer id){
        Optional<EstacionAprend> opt=estacionAprendRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }else{
            return null;
        }
    }
}
