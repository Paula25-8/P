package com.itinerariosdeaprendizaje.practicum.controller;

import com.itinerariosdeaprendizaje.practicum.model.*;
import com.itinerariosdeaprendizaje.practicum.service.*;
import com.itinerariosdeaprendizaje.practicum.utils.Especialidad;
import com.itinerariosdeaprendizaje.practicum.utils.MetodosGenerales;
import com.itinerariosdeaprendizaje.practicum.utils.Nivelndicador;
import com.itinerariosdeaprendizaje.practicum.utils.TipoPerfil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorEvaluacion {

    private MetodosGenerales metodosGenerales = new MetodosGenerales();
    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private TutorURService tutorURService;
    @Autowired
    private PracticumService practicumService;
    @Autowired
    private PesoCriterioService pesoCriterioService;
    @Autowired
    private NotaTutorCentroService notaTutorCentroService;

    @GetMapping("rubricaTutorUR")
    public String consultaRubricaTutorUR(Model model){
        model.addAttribute("nivel1", Nivelndicador.SOBRESALIENTE.descripcion);
        model.addAttribute("nivel2", Nivelndicador.NOTABLE.descripcion);
        model.addAttribute("nivel3", Nivelndicador.APROBADO.descripcion);
        model.addAttribute("nivel4", Nivelndicador.SUSPENSO.descripcion);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user!=null) {
            Grado gradoUser = null;
            if(user.tieneRol("ROL_ESTUDIANTE")){
                Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
                gradoUser = estudiante.obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", false);
            }
            if(user.tieneRol("ROL_TUTOR_UR")){
                List<Estudiante> tutorizados = tutorURService.getTutorURPorCodnum(user.getId()).getTutorizados();
                gradoUser = tutorizados.get(0).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", true);
            }
            Rubrica rubrica = gradoUser.getRubricaTutorUR();
            if(rubrica!=null) {
                List<Criterio> criteriosRubrica = rubrica.getCriterios();
                List<PesoCriterio> pesosCriterios = new ArrayList<>();
                for(int i=0;i<criteriosRubrica.size();i++){
                    PesoCriterio.IdPesoCriterio id= new PesoCriterio.IdPesoCriterio();
                    id.setId_Rubrica(rubrica.getId());
                    id.setId_Criterio(criteriosRubrica.get(i).getId());
                    pesosCriterios.add(pesoCriterioService.getPesoCriterioPorId(id));
                }
                model.addAttribute("rubrica", rubrica);
                model.addAttribute("criterios", criteriosRubrica);
                model.addAttribute("pesosCriterios", pesosCriterios);
                model.addAttribute("tipoTutor", "tutor de la UR");
                return "rubricaEvaluacion";
            }
            else{
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @GetMapping("rubricaTutorCentro")
    public String consultaRubricaTutorCentro(Model model){
        model.addAttribute("nivel1", Nivelndicador.SOBRESALIENTE.descripcion);
        model.addAttribute("nivel2", Nivelndicador.NOTABLE.descripcion);
        model.addAttribute("nivel3", Nivelndicador.APROBADO.descripcion);
        model.addAttribute("nivel4", Nivelndicador.SUSPENSO.descripcion);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user!=null) {
            Grado gradoUser = null;
            if(user.tieneRol("ROL_ESTUDIANTE")){
                Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
                gradoUser = estudiante.obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", false);
            }
            if(user.tieneRol("ROL_TUTOR_UR")){
                List<Estudiante> tutorizados = tutorURService.getTutorURPorCodnum(user.getId()).getTutorizados();
                gradoUser = tutorizados.get(0).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", true);
            }
            Rubrica rubrica = gradoUser.getRubricaTutorCentro();
            if(rubrica!=null) {
                List<Criterio> criteriosRubrica = rubrica.getCriterios();
                List<PesoCriterio> pesosCriterios = new ArrayList<>();
                for(int i=0;i<criteriosRubrica.size();i++){
                    PesoCriterio.IdPesoCriterio id= new PesoCriterio.IdPesoCriterio();
                    id.setId_Rubrica(rubrica.getId());
                    id.setId_Criterio(criteriosRubrica.get(i).getId());
                    pesosCriterios.add(pesoCriterioService.getPesoCriterioPorId(id));
                }
                model.addAttribute("rubrica", rubrica);
                model.addAttribute("criterios", criteriosRubrica);
                model.addAttribute("pesosCriterios", pesosCriterios);
                model.addAttribute("tipoTutor", "tutor del centro educativo");
                return "rubricaEvaluacion";
            }
            else{
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @GetMapping("calificacion")
    public String consultaCalificacion(Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user!=null) {
            Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
            // Caso de que accede un usuario ESTUDIANTE
            if(estudiante!=null){
                Practicum practicumActivo = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                if (practicumActivo != null) {
                    List<NotaTutorCentro> notasTutoresCentro = notaTutorCentroService.getNotasTutoresPorPracticum(practicumActivo);
                    System.out.println("Id de practicum: "+practicumActivo.getId());
                    System.out.println("Notas de tutor de centro prácticum: ");
                    for(int i=0;i<notasTutoresCentro.size();i++){
                        System.out.println(notasTutoresCentro.get(i).getEspecialidad().descripcion);
                    }
                    if(!notasTutoresCentro.isEmpty()) {
                        if(notasTutoresCentro.size()==2){
                            model.addAttribute("variosTutores", true);
                        }
                        else{
                            model.addAttribute("variosTutores", false);
                        }
                        for (int i = 0; i < notasTutoresCentro.size(); i++) {
                            if (notasTutoresCentro.get(i).getEspecialidad().equals(Especialidad.GENERALISTA.descripcion)) {
                                model.addAttribute("notaTutorCentroGeneralista", notasTutoresCentro.get(i).getNota());
                                model.addAttribute("fechNotaTutorCentroGeneralista", notasTutoresCentro.get(i).getFechaNota());
                            } else {
                                model.addAttribute("notaTutorCentroMencion", notasTutoresCentro.get(i).getNota());
                                model.addAttribute("fechNotaTutorCentroMencion", notasTutoresCentro.get(i).getFechaNota());
                                model.addAttribute("notaFinalMencion", practicumActivo.getNotaFinalMencion());
                                model.addAttribute("fechNotaFinalMencion", practicumActivo.getFechaNFMencion());
                            }
                        }
                        model.addAttribute("notaTutorUR", practicumActivo.getNotaTutorUR());
                        model.addAttribute("fechNotaTutorUR", practicumActivo.getFechaNotaTutorUR());
                        model.addAttribute("notaFinal", practicumActivo.getNotaFinal());
                        model.addAttribute("fechNotaFinal", practicumActivo.getFechaNF());
                        return "verCalificacion";
                    }
                    else{
                        return "redirect:/";
                    }
                }
                else {
                    return "redirect:/itinerariosPropios";
                }
            }
        }
        return "redirect:/";
    }
}
