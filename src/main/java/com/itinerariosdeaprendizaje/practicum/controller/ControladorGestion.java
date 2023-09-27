package com.itinerariosdeaprendizaje.practicum.controller;

import com.itinerariosdeaprendizaje.practicum.model.*;
import com.itinerariosdeaprendizaje.practicum.service.*;
import com.itinerariosdeaprendizaje.practicum.utils.MetodosGenerales;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;


@Controller
public class ControladorGestion {

    private MetodosGenerales metodosGenerales = new MetodosGenerales();
    // Declaracion de los distintos servicios a utilizar
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private TutorURService tutorUrService;
    @Autowired
    private TutorCentroService tutorCentroService;
    @Autowired
    private PreguntaService preguntaServicio;
    @Autowired
    private SelloService selloService;
    @Autowired
    private LineasAprendService lineasAprendService;
    @Autowired
    private EstacionAprendService estacionAprendService;
    @Autowired
    private NotaTutorCentroService notaTutorCentroService;
    @Autowired
    private PracticumService practicumService;
    @Autowired
    private RelacionEstudianteTutorCentroService relacionEstudianteTutorCentroService;

    /* OPCION MENU: MODELO DE ITINERARIOS con nombre del grado del usuario bien usado */
    @GetMapping("/")
    public String paginaInicio(Model model){
        // Obengo nombre de usuario para guardar objeto Usuario en la sesion y poder personalizar las distintas paginas
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = null ;
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        }
        //@SuppressWarnings("null")
        String userName = userDetails.getUsername();
        Usuario usuario = usuarioService.getUsuarioPorCuasi(userName);
        if(usuario.estaActivo()){
            // Guardamos el usuario logueado en HttpSession
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession sesion = attr.getRequest().getSession(true);
            sesion.setAttribute("usuarioSesion", usuario);
            if(usuario.tieneRol("ROL_ESTUDIANTE")){
                Estudiante estudiante = estudianteService.getEstudiantePorCodnum(usuario.getId());
                List<Grado> grados = estudiante.getGrados();
                for(int i=0;i<grados.size();i++){
                    if(grados.get(i).getCurso().equals(metodosGenerales.getCursoAcademico())){
                        model.addAttribute("grado", "del grado de "+grados.get(i).getNombre());
                    }
                }
            }
            if(usuario.tieneRol("ROL_TUTOR_UR")){
                TutorUR tutorUR = tutorUrService.getTutorURPorCodnum(usuario.getId());
                List<Estudiante> tutorizados = tutorUR.getTutorizados();
                if(!tutorizados.isEmpty()){
                    model.addAttribute("grado", "del grado de "+tutorizados.get(0).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico()).getNombre());
                }
            }
            if(usuario.tieneRol("ROL_TUTOR_CENTRO")){
                TutorCentro tutorCentro = tutorCentroService.getTutorCentroPorCodnum(usuario.getId());
                List<Estudiante> tutorizados = relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentro);
                if(!tutorizados.isEmpty()){
                    model.addAttribute("grado", "del grado de "+tutorizados.get(0).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico()).getNombre());
                }
            }
            if(usuario.tieneRol("ROL_COORDINADOR")){
                model.addAttribute("grado", "de los grados de Educación");
            }
            return "index";
        }else{
            // Caso de que usuario no este activo, por lo que no podra acceder a la aplicacion
            return "redirect:/login?logout&accessdenied";
        }
    }

    /* OPCION MENU: INFO GENERAL */
    @GetMapping("/preguntas")
    public String preguntas(Model model){
        model.addAttribute("preguntas", preguntaServicio.getPreguntas());
        List<Caracteristica> caracts = selloService.getSelloPorCurso(metodosGenerales.getCursoAcademico()).getCaracteristicas();
        model.addAttribute("caracteristicas", caracts);
        return "infoGeneral";
    }

    /* OPCION MENU: ITINERARIOS > LINEAS APREND */
    @GetMapping("/lineasAprendizaje")
    public String lineasAprendizaje(Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user !=null) {
            String curso = metodosGenerales.getCursoAcademico();
            if (user.tieneRol("ROL_ESTUDIANTE")) { // Buscamos líneas de grado donde está matriculado estudiante en ese curso
                Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
                model.addAttribute("lineas", lineasAprendService.getLineasPorCursoYGrado(curso, estudiante.obtenerGradoPorCurso(curso)));
                model.addAttribute("tieneItinerarioCreado", estudiante.tieneItinerarioCreado(curso, metodosGenerales.getConvocatoriaPorFechas()));
            } else {
                model.addAttribute("lineas", lineasAprendService.getLineasPorCurso(curso));
            }
            model.addAttribute("estaciones", estacionAprendService.getEstacionesPorCurso(curso));
            return "lineasAprendizaje";
        }
        return "redirect:/";
    }

}
