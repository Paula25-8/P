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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    private RelacionEstudianteTutorCentroService relacionEstudianteTutorCentroService;
    @Autowired
    private TutorCentroService tutorCentroService;
    @Autowired
    private GradoService gradoService;

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
                model.addAttribute("valorarRubrica", false);
            }
            if(user.tieneRol("ROL_TUTOR_CENTRO")){
                List<Estudiante> tutorizados = relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentroService.getTutorCentroPorCodnum(user.getId()));
                gradoUser = tutorizados.get(0).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", false);
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
                model.addAttribute("valorarRubrica", false);
            }
            if(user.tieneRol("ROL_TUTOR_CENTRO")){
                List<Estudiante> tutorizados = relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentroService.getTutorCentroPorCodnum(user.getId()));
                gradoUser = tutorizados.get(0).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", false);
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

    @GetMapping("/calificacion")
    public String consultaCalificacion(Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user!=null) {
            if(user.tieneRol("ROL_ESTUDIANTE")) {
                Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
                Practicum practicumActivo = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                if (practicumActivo != null) {
                    List<NotaTutorCentro> notasTutoresCentro = notaTutorCentroService.getNotasTutoresPorPracticum(practicumActivo);
                    System.out.println("Id de practicum: " + practicumActivo.getId());
                    System.out.println("Notas de tutor de centro prácticum: ");
                    for (int i = 0; i < notasTutoresCentro.size(); i++) {
                        System.out.println(notasTutoresCentro.get(i).getEspecialidad().descripcion);
                    }
                    if (!notasTutoresCentro.isEmpty()) {
                        if (notasTutoresCentro.size() == 2) {
                            model.addAttribute("variosTutores", true);
                        } else {
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
                    } else {
                        return "redirect:/";
                    }
                } else {
                    return "redirect:/itinerariosPropios";
                }
            }
            if(user.tieneRol("ROL_TUTOR_UR") || user.tieneRol("ROL_TUTOR_CENTRO")){
                List<Grado> grados = new ArrayList<>();
                List<EstudianteTabla> estudiantes = new ArrayList<>();
                List<Estudiante> tutorizados = new ArrayList<>();
                if(user.tieneRol("ROL_TUTOR_UR")){
                    TutorUR tutorUR = tutorURService.getTutorURPorCodnum(user.getId());
                    tutorizados = tutorUR.getTutorizados();
                }
                if(user.tieneRol("ROL_TUTOR_CENTRO")){
                    TutorCentro tutorCentro = tutorCentroService.getTutorCentroPorCodnum(user.getId());
                    tutorizados = relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentro);
                }
                for(int i=0;i<tutorizados.size();i++) {
                    Practicum practicum = practicumService.getPracticumActivoEstudiante(tutorizados.get(i), metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                    if(practicum!=null){
                        Grado grado = tutorizados.get(i).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                        if(!grados.contains(grado)){
                            grados.add(grado);
                        }
                        estudiantes.add(new EstudianteTabla(tutorizados.get(i).getId(), tutorizados.get(i).getNombreCompletoOrdenado(), null, grado, null, practicum.getDossier(), practicum.getConvocatoria()));
                    }
                }
                model.addAttribute("grados",grados);
                model.addAttribute("gradoSeleccionado", 0);
                model.addAttribute("tutorizados", estudiantes);
                return "evaluacionAlumnado";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/calificacion/consultarDossierFinal/{id}")
    public String consultaDossierFinalAlumnoConcreto(@PathVariable Integer id, Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user!=null) {
            Estudiante estudiante = estudianteService.getEstudiantePorCodnum(id);
            if((user.tieneRol("ROL_TUTOR_UR") && tutorURService.getTutorURPorCodnum(user.getId()).tieneEstudiante(estudiante))||(user.tieneRol("ROL_TUTOR_CENTRO") && relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentroService.getTutorCentroPorCodnum(user.getId())).contains(estudiante))){
                if(estudiante!=null){
                    Practicum practicum = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                    Dossier dossier = practicum.getDossier();
                    if(dossier != null){
                        model.addAttribute("rutaDocu", "/files/dossierFinal/" + dossier.getNombreDocu());
                        model.addAttribute("fechaEntrega", dossier.getFechaEntrega());
                        model.addAttribute("feedbackTutor", dossier.getFeedbackTutorUR());
                        model.addAttribute("fechaFeedback", dossier.getFechaEntrega());
                    }
                    model.addAttribute("dossierFinal", dossier);
                    model.addAttribute("estudiante", estudiante);
                    model.addAttribute("idEstudiante", estudiante.getId());
                    model.addAttribute("urlFlecha", "/calificacion");
                    return "dossierFinal";
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping("/calificacion") //Ruta a la que solo acceden TUTOR_UR o TUTOR_CENTRO
    public String consultaDossierAlumnadoPorFiltro(@RequestParam("nombreAlumn") String nombreAlumn, @RequestParam("grado") Integer grado, Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        List<EstudianteTabla> estudiantes = new ArrayList<>();
        if(user!=null) {
            List<Grado> grados = new ArrayList<>();
            List<Estudiante> tutorizados = new ArrayList<>();
            if(user.tieneRol("ROL_TUTOR_UR")) {
                TutorUR tutorUR = tutorURService.getTutorURPorCodnum(user.getId());
                tutorizados = tutorUR.getTutorizados();
            }
            if(user.tieneRol("ROL_TUTOR_CENTRO")){
                TutorCentro tutorCentro = tutorCentroService.getTutorCentroPorCodnum(user.getId());
                tutorizados = relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentro);
            }
            for(int i=0;i<tutorizados.size();i++){
                Practicum practicum = practicumService.getPracticumActivoEstudiante(tutorizados.get(i), metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                if(practicum!=null){
                    Grado gradoLista = tutorizados.get(i).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                    if (!grados.contains(gradoLista)) {
                        grados.add(gradoLista);
                    }
                    // Caso donde filtro tenga todos los valores vacios
                    if(grado==0 && nombreAlumn==""){
                        return "redirect:/calificacion";
                    }
                    // Caso donde el filtro solo se complete con Grado
                    if (gradoLista.equals(gradoService.getGradoPorId(grado)) && nombreAlumn=="") {
                        estudiantes.add(new EstudianteTabla(tutorizados.get(i).getId(), tutorizados.get(i).getNombreCompletoOrdenado(), null, gradoLista, null, practicum.getDossier(), practicum.getConvocatoria()));
                    }
                    else{
                        // Caso donde el filtro solo se complete con Nombre
                        if (grado==0 && tutorizados.get(i).getNombreCompletoOrdenado().contains(nombreAlumn)) {
                            estudiantes.add(new EstudianteTabla(tutorizados.get(i).getId(), tutorizados.get(i).getNombreCompletoOrdenado(), null, gradoLista, null, practicum.getDossier(), practicum.getConvocatoria()));
                        }
                        if (gradoLista.equals(gradoService.getGradoPorId(grado)) && tutorizados.get(i).getNombreCompletoOrdenado().contains(nombreAlumn)) {
                            estudiantes.add(new EstudianteTabla(tutorizados.get(i).getId(), tutorizados.get(i).getNombreCompletoOrdenado(), null, gradoLista, null, practicum.getDossier(), practicum.getConvocatoria()));
                        }
                    }
                }
            }
            model.addAttribute("grados", grados);
            model.addAttribute("gradoSeleccionado", grado);
            model.addAttribute("nombreSeleccionado", nombreAlumn);
            model.addAttribute("tutorizados",estudiantes);
            return "evaluacionAlumnado";
        }
        return "redirect:/";
    }
}
