package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.CompEspecifica;
import com.itinerariosdeaprendizaje.practicum.repository.CompEspecificaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompEspecificaService {
    @Autowired
    private CompEspecificaRepository compEspecificaRepository;

    public List<CompEspecifica> getCompetencias(){
        return compEspecificaRepository.findAll();
    }
    /*public Map<Integer, String> getMapaCompetencias(){
        List<CompEspecifica> competencias = this.getCompetencias();
        Map<Integer, String> mapa = new HashMap<>();
        for(int i=0;i<lineas.size();i++){
            mapa.put(lineas.get(i).getId(), i+" - "+lineas.get(i).getTitulo());
        }
        return mapa;
    }*/
}
