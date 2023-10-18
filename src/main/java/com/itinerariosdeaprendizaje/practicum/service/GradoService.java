package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Grado;
import com.itinerariosdeaprendizaje.practicum.repository.GradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public Grado getGradoPorNombreYCurso(String nombre, String curso){
        return gradoRepository.findByNombreAndCurso(nombre, curso);
    }

    public List<Grado> getGrados(){
        return gradoRepository.findAll();
    }

    public List<Grado> getGradosPorCurso(String curso){
        List<Grado> grados = gradoRepository.findAll();//this.getGrados();
        List<Grado> gradosPorCurso = new ArrayList<>();
        for(int i=0;i<grados.size();i++){
            if(grados.get(i).getCurso().equals(curso)){
                gradosPorCurso.add(grados.get(i));
            }
        }
        return gradosPorCurso;
    }

    public List<String> getNombreGrados(){
        List<Grado> grados = gradoRepository.findAll();//this.getGrados();
        List<String> gradosDistintos = new ArrayList<>();
        String nombre;
        for(int i=0;i<grados.size();i++){
            nombre = grados.get(i).getNombre();
            if(!gradosDistintos.contains(nombre)){
                gradosDistintos.add(nombre);
            }
        }
        return gradosDistintos;
    }

    public Grado guardarGrado(Grado grado) {
        return gradoRepository.save(grado);
    }


}
