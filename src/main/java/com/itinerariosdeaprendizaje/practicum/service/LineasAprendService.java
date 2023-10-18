package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Grado;
import com.itinerariosdeaprendizaje.practicum.model.LineaAprend;
import com.itinerariosdeaprendizaje.practicum.model.SelloCalidad;
import com.itinerariosdeaprendizaje.practicum.repository.LineasAprendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LineasAprendService {

    @Autowired
    private LineasAprendRepository lineasAprendRepository;

    public List<LineaAprend> getLineasPorCurso(String curso){
        List<LineaAprend> lineas=lineasAprendRepository.findByCurso(curso);
        if(!lineas.isEmpty()){
            return lineas;
        }else{
            return null;
        }
    }

    public List<LineaAprend> getLineas(){
        return lineasAprendRepository.findAll();
    }

    public List<LineaAprend> getLineasPorCursoYGrado(String curso, Grado grado){
        List<LineaAprend> lineas=this.getLineasPorCurso(curso);
        List<LineaAprend> listaDefinitiva = new ArrayList<>();
        for(int i=0;i<lineas.size();i++){
            if(lineas.get(i).getGrados().contains(grado)){
                listaDefinitiva.add(lineas.get(i));
            }
        }
        return listaDefinitiva;
    }

    public LineaAprend getLineasPorId(Integer id){
        Optional<LineaAprend> opt = lineasAprendRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        else{
            return null;
        }
    }

    public LineaAprend guardarLinea(LineaAprend linea){
        return lineasAprendRepository.save(linea);
    }

    public List<String> getNombreGradosLinea(LineaAprend linea){
        List<Grado> grados = linea.getGrados();
        List<String> nombreGrados = new ArrayList<>();
        for(int i=0;i<grados.size();i++){
            if(!nombreGrados.contains(grados.get(i).getNombre())){
                nombreGrados.add(grados.get(i).getNombre());
            }
        }
        return nombreGrados;
    }

}
