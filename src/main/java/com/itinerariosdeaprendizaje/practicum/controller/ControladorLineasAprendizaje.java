package com.itinerariosdeaprendizaje.practicum.controller;

import com.itinerariosdeaprendizaje.practicum.model.Actividad;
import com.itinerariosdeaprendizaje.practicum.model.CompEspecifica;
import com.itinerariosdeaprendizaje.practicum.model.EstacionAprend;
import com.itinerariosdeaprendizaje.practicum.service.ActividadService;
import com.itinerariosdeaprendizaje.practicum.service.EstacionAprendService;
import com.itinerariosdeaprendizaje.practicum.service.LineasAprendService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ControladorLineasAprendizaje {

    @Autowired
    private LineasAprendService lineasAprendService;
    @Autowired
    private EstacionAprendService estacionAprendService;
    @Autowired
    private ActividadService actividadService;

    @GetMapping("/infoEstacion/{id}")
    public String infoEstacionAprendizaje(@PathVariable Integer id, Model model){
        EstacionAprend estacion = estacionAprendService.getEstacionPorId(id);
        if(estacion==null){
            return "redirect:/lineasAprendizaje";
        }else{
            List<CompEspecifica> competencias = estacion.getCompetencias();
            String html_div_CE = "<h3 class='text-center'>Competencias Específicas</h3>";
            for(int i=0;i<competencias.size();i++){
                html_div_CE += "<p><b>"+competencias.get(i).getCodigo()+". </b>"+competencias.get(i).getDescripcion()+"</p>";
            }
            Actividad actInicio = null;
            Actividad actAplic = null;
            Actividad actCierre = null;
            Actividad actCompleto = null;
            List<Actividad> actividades = actividadService.getActividadesPorEstacion(estacion);
            for(int i=0;i<actividades.size();i++){
                if(actividades.get(i).getTipo().descripcion.equals("INICIO")) actInicio=actividades.get(i);
                if(actividades.get(i).getTipo().descripcion.equals("APLICACION")) actAplic=actividades.get(i);
                if(actividades.get(i).getTipo().descripcion.equals("CIERRE")) actCierre=actividades.get(i);
                if(actividades.get(i).getTipo().descripcion.equals("COMPLETA")) actCompleto=actividades.get(i);
            }
            String html_div_Act;
            if(actCompleto==null) {
                html_div_Act = "<h3 class='text-center'>Secuencia de Actividades</h3><h4>Actividades de inicio: </h4>" + actInicio.getHtmlResultante()+"<h4>Actividades de aplicación: </h4>"+actAplic.getHtmlResultante()+"<h4>Actividades de cierre: </h4>"+actCierre.getHtmlResultante();
            }
            else{
                html_div_Act = "<h3 class='text-center'>Secuencia de Actividades</h3>"+actCompleto.getHtmlResultante();
            }
            model.addAttribute("sentenciaCE", html_div_CE);
            model.addAttribute("sentencia_Actividades", html_div_Act);
            model.addAttribute("estacion", estacion);
            return "estacionAprendizaje";
        }
    }

}
