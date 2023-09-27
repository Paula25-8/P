package com.itinerariosdeaprendizaje.practicum.controller;

import com.itinerariosdeaprendizaje.practicum.model.*;
import com.itinerariosdeaprendizaje.practicum.service.*;
import com.itinerariosdeaprendizaje.practicum.utils.Especialidad;
import com.itinerariosdeaprendizaje.practicum.utils.MetodosGenerales;
import com.itinerariosdeaprendizaje.practicum.utils.Nivelndicador;
import com.itinerariosdeaprendizaje.practicum.utils.TipoPerfil;
import jakarta.servlet.http.HttpServletRequest;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

    @GetMapping("/rubricaTutorUR")
    public String consultaRubricaTutorUR(Model model, HttpServletRequest request) {
        String paginaAnterior = request.getHeader("Referer");
        if (request.getHeader("Referer").contains("/consultarDossierFinal")) {
            model.addAttribute("urlFlecha", paginaAnterior);
        }
        model.addAttribute("nivel1", Nivelndicador.SOBRESALIENTE.descripcion);
        model.addAttribute("nivel2", Nivelndicador.NOTABLE.descripcion);
        model.addAttribute("nivel3", Nivelndicador.APROBADO.descripcion);
        model.addAttribute("nivel4", Nivelndicador.SUSPENSO.descripcion);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if (user != null) {
            Grado gradoUser = null;
            if (user.tieneRol("ROL_ESTUDIANTE")) {
                Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
                gradoUser = estudiante.obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", false);
            }
            if (user.tieneRol("ROL_TUTOR_UR")) {
                List<Estudiante> tutorizados = tutorURService.getTutorURPorCodnum(user.getId()).getTutorizados();
                gradoUser = tutorizados.get(0).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", false);
            }
            if (user.tieneRol("ROL_TUTOR_CENTRO")) {
                List<Estudiante> tutorizados = relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentroService.getTutorCentroPorCodnum(user.getId()));
                gradoUser = tutorizados.get(0).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", false);
            }
            if (user.tieneRol("ROL_COORDINADOR")) {
                List<Grado> grados = gradoService.getGrados();
                gradoUser = grados.get(0);
                model.addAttribute("grados", grados);
                model.addAttribute("gradoSeleccionado", gradoUser.getId());
                model.addAttribute("valorarRubrica", false);
            }
            Rubrica rubrica = gradoUser.getRubricaTutorUR();
            if (rubrica != null) {
                List<Criterio> criteriosRubrica = rubrica.getCriterios();
                List<PesoCriterio> pesosCriterios = new ArrayList<>();
                for (int i = 0; i < criteriosRubrica.size(); i++) {
                    PesoCriterio.IdPesoCriterio id = new PesoCriterio.IdPesoCriterio();
                    id.setId_Rubrica(rubrica.getId());
                    id.setId_Criterio(criteriosRubrica.get(i).getId());
                    pesosCriterios.add(pesoCriterioService.getPesoCriterioPorId(id));
                }
                model.addAttribute("rubrica", rubrica);
                model.addAttribute("criterios", criteriosRubrica);
                model.addAttribute("pesosCriterios", pesosCriterios);
                model.addAttribute("tipoTutor", "tutor de la UR");
                return "rubricaEvaluacion";
            } else {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/rubricaTutorCentro")
    public String consultaRubricaTutorCentro(Model model) {
        model.addAttribute("nivel1", Nivelndicador.SOBRESALIENTE.descripcion);
        model.addAttribute("nivel2", Nivelndicador.NOTABLE.descripcion);
        model.addAttribute("nivel3", Nivelndicador.APROBADO.descripcion);
        model.addAttribute("nivel4", Nivelndicador.SUSPENSO.descripcion);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if (user != null) {
            Grado gradoUser = null;
            if (user.tieneRol("ROL_ESTUDIANTE")) {
                Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
                gradoUser = estudiante.obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", false);
            }
            if (user.tieneRol("ROL_TUTOR_UR")) {
                List<Estudiante> tutorizados = tutorURService.getTutorURPorCodnum(user.getId()).getTutorizados();
                gradoUser = tutorizados.get(0).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", false);
            }
            if (user.tieneRol("ROL_TUTOR_CENTRO")) {
                List<Estudiante> tutorizados = relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentroService.getTutorCentroPorCodnum(user.getId()));
                gradoUser = tutorizados.get(0).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                model.addAttribute("valorarRubrica", false);
            }
            if (user.tieneRol("ROL_COORDINADOR")) {
                List<Grado> grados = gradoService.getGrados();
                gradoUser = grados.get(0);
                model.addAttribute("grados", grados);
                model.addAttribute("gradoSeleccionado", gradoUser.getId());
                model.addAttribute("valorarRubrica", false);
            }
            Rubrica rubrica = gradoUser.getRubricaTutorCentro();
            if (rubrica != null) {
                List<Criterio> criteriosRubrica = rubrica.getCriterios();
                List<PesoCriterio> pesosCriterios = new ArrayList<>();
                for (int i = 0; i < criteriosRubrica.size(); i++) {
                    PesoCriterio.IdPesoCriterio id = new PesoCriterio.IdPesoCriterio();
                    id.setId_Rubrica(rubrica.getId());
                    id.setId_Criterio(criteriosRubrica.get(i).getId());
                    pesosCriterios.add(pesoCriterioService.getPesoCriterioPorId(id));
                }
                model.addAttribute("rubrica", rubrica);
                model.addAttribute("criterios", criteriosRubrica);
                model.addAttribute("pesosCriterios", pesosCriterios);
                model.addAttribute("tipoTutor", "tutor del centro educativo");
                return "rubricaEvaluacion";
            } else {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    // Metodo para filtro de rol COORDINADOR (solo puede acceder este rol a la url)
    @PostMapping("/rubricaTutorUR")
    public String filtradoRubricaTutorUR(@RequestParam("gradoFiltrado") Integer idGrado, Model model) {
        System.out.println("Estamos en metodo POST de la rubrica del tutor UR");
        model.addAttribute("nivel1", Nivelndicador.SOBRESALIENTE.descripcion);
        model.addAttribute("nivel2", Nivelndicador.NOTABLE.descripcion);
        model.addAttribute("nivel3", Nivelndicador.APROBADO.descripcion);
        model.addAttribute("nivel4", Nivelndicador.SUSPENSO.descripcion);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        Grado gradoSeleccionado = gradoService.getGradoPorId(idGrado);
        if (user != null && user.tieneRol("ROL_COORDINADOR") && gradoSeleccionado != null) {
            List<Grado> grados = gradoService.getGrados();
            Rubrica rubrica = gradoSeleccionado.getRubricaTutorUR();
            if (rubrica != null) {
                List<Criterio> criteriosRubrica = rubrica.getCriterios();
                List<PesoCriterio> pesosCriterios = new ArrayList<>();
                for (int i = 0; i < criteriosRubrica.size(); i++) {
                    PesoCriterio.IdPesoCriterio id = new PesoCriterio.IdPesoCriterio();
                    id.setId_Rubrica(rubrica.getId());
                    id.setId_Criterio(criteriosRubrica.get(i).getId());
                    pesosCriterios.add(pesoCriterioService.getPesoCriterioPorId(id));
                }
                model.addAttribute("grados", grados);
                model.addAttribute("gradoSeleccionado", gradoSeleccionado.getId());
                model.addAttribute("valorarRubrica", false);
                model.addAttribute("rubrica", rubrica);
                model.addAttribute("criterios", criteriosRubrica);
                model.addAttribute("pesosCriterios", pesosCriterios);
                model.addAttribute("tipoTutor", "tutor de la UR");
                return "rubricaEvaluacion";
            } else {
                return "redirect:/rubricaTutorUR";
            }
        }
        return"redirect:/rubricaTutorUR";
    }

    // Metodo para filtro de rol COORDINADOR (solo puede acceder este rol a la url)
    @PostMapping("/rubricaTutorCentro")
    public String filtradoRubricaTutorCentro(@RequestParam("gradoFiltrado") Integer idGrado, Model model) {
        System.out.println("Estamos en metodo POST de la rubrica del tutor UR");
        model.addAttribute("nivel1", Nivelndicador.SOBRESALIENTE.descripcion);
        model.addAttribute("nivel2", Nivelndicador.NOTABLE.descripcion);
        model.addAttribute("nivel3", Nivelndicador.APROBADO.descripcion);
        model.addAttribute("nivel4", Nivelndicador.SUSPENSO.descripcion);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        Grado gradoSeleccionado = gradoService.getGradoPorId(idGrado);
        if (user != null && user.tieneRol("ROL_COORDINADOR") && gradoSeleccionado != null) {
            List<Grado> grados = gradoService.getGrados();
            Rubrica rubrica = gradoSeleccionado.getRubricaTutorCentro();
            if (rubrica != null) {
                List<Criterio> criteriosRubrica = rubrica.getCriterios();
                List<PesoCriterio> pesosCriterios = new ArrayList<>();
                for (int i = 0; i < criteriosRubrica.size(); i++) {
                    PesoCriterio.IdPesoCriterio id = new PesoCriterio.IdPesoCriterio();
                    id.setId_Rubrica(rubrica.getId());
                    id.setId_Criterio(criteriosRubrica.get(i).getId());
                    pesosCriterios.add(pesoCriterioService.getPesoCriterioPorId(id));
                }
                model.addAttribute("grados", grados);
                model.addAttribute("gradoSeleccionado", gradoSeleccionado.getId());
                model.addAttribute("valorarRubrica", false);
                model.addAttribute("rubrica", rubrica);
                model.addAttribute("criterios", criteriosRubrica);
                model.addAttribute("pesosCriterios", pesosCriterios);
                model.addAttribute("tipoTutor", "tutor del centro educativo");
                return "rubricaEvaluacion";
            } else {
                return "redirect:/rubricaTutorCentro";
            }
        }
        return"redirect:/rubricaTutorCentro";
    }

    @GetMapping("/calificacion")
    public String consultaCalificacion(Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if (user != null) {
            if (user.tieneRol("ROL_ESTUDIANTE")) {
                Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
                Practicum practicumActivo = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                if (practicumActivo != null) {
                    List<NotaTutorCentro> notasTutoresCentro = notaTutorCentroService.getNotasTutoresPorPracticum(practicumActivo);
                    if (!notasTutoresCentro.isEmpty()) {
                        if (notasTutoresCentro.size() == 2) {
                            model.addAttribute("variosTutores", true);
                        } else {
                            model.addAttribute("variosTutores", false);
                        }
                        for (int i = 0; i < notasTutoresCentro.size(); i++) {
                            if (notasTutoresCentro.get(i).getEspecialidad().equals(Especialidad.GENERALISTA)) {
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
            if (user.tieneRol("ROL_TUTOR_UR") || user.tieneRol("ROL_TUTOR_CENTRO") || user.tieneRol("ROL_COORDINADOR")) {
                List<String> cursos = new ArrayList<>();
                List<Grado> grados = new ArrayList<>();
                List<Estudiante> listadoEstudiantes = null;
                List<EstudianteTabla> estudiantesAMostrar = new ArrayList<>();
                if (user.tieneRol("ROL_TUTOR_UR")) {
                    TutorUR tutorUR = tutorURService.getTutorURPorCodnum(user.getId());
                    listadoEstudiantes = tutorUR.getTutorizados();
                    model.addAttribute("usuarioTutor", true);
                }
                if (user.tieneRol("ROL_TUTOR_CENTRO")) {
                    TutorCentro tutorCentro = tutorCentroService.getTutorCentroPorCodnum(user.getId());
                    listadoEstudiantes = relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentro);
                    model.addAttribute("usuarioTutor", true);
                }
                if (user.tieneRol("ROL_COORDINADOR")) {
                    listadoEstudiantes = estudianteService.getEstudiantes();
                    model.addAttribute("usuarioTutor", false);
                }
                for (int i = 0; i < listadoEstudiantes.size(); i++) {
                    if(user.tieneRol("ROL_COORDINADOR")){
                        List<Practicum> practicums = practicumService.getPracticumsEstudiante(listadoEstudiantes.get(i));
                        if (!practicums.isEmpty()) {
                            for(int j=0;j<practicums.size();j++){
                                String curso = practicums.get(j).getCurso();
                                if (!cursos.contains(curso)) {
                                    cursos.add(curso);
                                }
                                Grado grado = listadoEstudiantes.get(i).obtenerGradoPorCurso(curso);
                                if (!this.contieneGrado(grados, grado)) {
                                    grados.add(grado);
                                }
                                Mencion posibleMencion = listadoEstudiantes.get(i).obtenerMencionPorCurso(curso);
                                if(practicums.get(j).getNotaFinal()!=0.0 &&((posibleMencion!=null && practicums.get(j).getNotaFinalMencion()!=0.0)||(posibleMencion==null))){
                                    estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicums.get(j), null, grado, null, true));
                                }
                                else{
                                    estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicums.get(j), null, grado, null, false));
                                }
                            }
                        }
                    }
                    if(user.tieneRol("ROL_TUTOR_UR") || user.tieneRol("ROL_TUTOR_CENTRO")){
                        Practicum practicum = practicumService.getPracticumActivoEstudiante(listadoEstudiantes.get(i), metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                        if (practicum != null) {
                            Grado grado = listadoEstudiantes.get(i).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                            if (!grados.contains(grado)) {
                                grados.add(grado);
                            }
                            // Comprobamos si el usuario ya ha evaluado al alumno o no
                            if(user.tieneRol("ROL_TUTOR_UR")){
                                if(practicum.getNotaTutorUR()!=0.0){
                                    estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicum, null, grado, null, true));
                                }
                                else{
                                    estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicum, null, grado, null, false));
                                }
                            }
                            if(user.tieneRol("ROL_TUTOR_CENTRO")){
                                NotaTutorCentro.IdNotaTutorCentro id = new NotaTutorCentro.IdNotaTutorCentro();
                                id.setCodnumTutorCentro(user.getId());
                                id.setId_practicum(practicum.getId());
                                NotaTutorCentro nota = notaTutorCentroService.getNotaTutorCentroPorId(id);
                                if(nota.getNota()!=0.0){
                                    estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicum, null, grado, null, true));
                                }
                                else{
                                    estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicum, null, grado, null, false));
                                }
                            }
                        }
                    }
                }
                model.addAttribute("grados", grados);
                model.addAttribute("gradoSeleccionado", 0);
                model.addAttribute("tutorizados", estudiantesAMostrar);
                return "evaluacionAlumnado";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/calificacion/consultarDossierFinal/{id}")
    public String consultaDossierFinalAlumnoConcreto(@PathVariable Integer id, Model model, HttpServletRequest request) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        boolean posibleCalificacion = false;
        if (user != null) {
            Estudiante estudiante = estudianteService.getEstudiantePorCodnum(id);
            if ((user.tieneRol("ROL_TUTOR_UR") && tutorURService.getTutorURPorCodnum(user.getId()).tieneEstudiante(estudiante)) || (user.tieneRol("ROL_TUTOR_CENTRO") && relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentroService.getTutorCentroPorCodnum(user.getId())).contains(estudiante))) {
                if (estudiante != null) {
                    Practicum practicum = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                    Dossier dossier = practicum.getDossier();
                    if (dossier != null) {
                        model.addAttribute("rutaDocu", "/files/dossierFinal/" + dossier.getNombreDocu());
                        model.addAttribute("fechaEntrega", dossier.getFechaEntrega());
                        model.addAttribute("feedbackTutor", dossier.getFeedbackTutorUR());
                        model.addAttribute("fechaFeedback", dossier.getFechaEntrega());
                    }
                    model.addAttribute("dossierFinal", dossier);
                    model.addAttribute("estudiante", estudiante);
                    model.addAttribute("idEstudiante", estudiante.getId());
                    model.addAttribute("urlFlecha", "/calificacion");
                    if(user.tieneRol("ROL_TUTOR_UR") && practicum.getNotaTutorUR()==0.0){
                        posibleCalificacion = true;
                    }
                    if(user.tieneRol("ROL_TUTOR_CENTRO")){
                        NotaTutorCentro.IdNotaTutorCentro idNotaTutorCentro = new NotaTutorCentro.IdNotaTutorCentro();
                        idNotaTutorCentro.setId_practicum(practicum.getId());
                        idNotaTutorCentro.setCodnumTutorCentro(user.getId());
                        if(notaTutorCentroService.getNotaTutorCentroPorId(idNotaTutorCentro).getNota()==0.0){
                            posibleCalificacion=true;
                        }
                    }
                    model.addAttribute("posibleCalificacion", posibleCalificacion);
                    return "dossierFinal";
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping("/calificacion") //Ruta a la que solo acceden TUTOR_UR o TUTOR_CENTRO o COORDINADOR (FILTRO DE EVALUACION)
    public String consultaDossierAlumnadoPorFiltro(@RequestParam("nombreAlumn") String nombreAlumn, @RequestParam("grado") Integer grado, Model model) {
        System.out.println("Grado: "+grado);
        System.out.println("Nombre alumno: "+nombreAlumn);
        // Caso donde filtro tenga todos los valores vacios
        if (grado == 0 && nombreAlumn == "") {
            return "redirect:/calificacion";
        }
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if (user != null) {
            Grado gradoSeleccionado = gradoService.getGradoPorId(grado);
            List<Grado> grados = new ArrayList<>();
            List<Estudiante> listadoEstudiantes = null;
            List<EstudianteTabla> estudiantesAMostrar = new ArrayList<>();
            if (user.tieneRol("ROL_TUTOR_UR")) {
                TutorUR tutorUR = tutorURService.getTutorURPorCodnum(user.getId());
                listadoEstudiantes = tutorUR.getTutorizados();
                model.addAttribute("usuarioTutor", true);
            }
            if (user.tieneRol("ROL_TUTOR_CENTRO")) {
                TutorCentro tutorCentro = tutorCentroService.getTutorCentroPorCodnum(user.getId());
                listadoEstudiantes = relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentro);
                model.addAttribute("usuarioTutor", true);
            }
            if (user.tieneRol("ROL_COORDINADOR")) {
                listadoEstudiantes = estudianteService.getEstudiantes();
                System.out.println("Tam listado alumnos matriculados: "+listadoEstudiantes.size());
                model.addAttribute("usuarioTutor", false);
            }
            for (int i = 0; i < listadoEstudiantes.size(); i++) {
                boolean estaEvaluado = false;
                if(user.tieneRol("ROL_COORDINADOR")){
                    System.out.println("Estudiante: "+listadoEstudiantes.get(i).getNombreCompleto());
                    List<Practicum> practicums = practicumService.getPracticumsEstudiante(listadoEstudiantes.get(i));
                    if (!practicums.isEmpty()) {
                        for(int j=0;j<practicums.size();j++){
                            System.out.println("Practicum: "+practicums.get(j).getId());
                            String curso = practicums.get(j).getCurso();
                            Grado gradoLista = listadoEstudiantes.get(i).obtenerGradoPorCurso(curso);
                            if (!this.contieneGrado(grados, gradoLista)) {
                                grados.add(gradoLista);
                            }
                            Mencion posibleMencion = listadoEstudiantes.get(i).obtenerMencionPorCurso(curso);
                            if(practicums.get(j).getNotaFinal()!=0.0 &&((posibleMencion!=null && practicums.get(j).getNotaFinalMencion()!=0.0)||(posibleMencion==null))){
                                estaEvaluado = true;
                            }
                            // Caso donde el filtro solo se complete con Grado
                            if (nombreAlumn == "" && gradoSeleccionado != null && gradoLista.getNombre().equals(gradoSeleccionado.getNombre())) {
                                System.out.println("Caso de filtro con solo Grado");
                                estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicums.get(j), null, gradoLista, null, estaEvaluado));
                            }else{
                                // Caso donde el filtro solo se complete con Nombre
                                if (grado == 0 && listadoEstudiantes.get(i).getNombreCompletoOrdenado().contains(nombreAlumn)) {
                                    System.out.println("Caso de filtro con solo Nombre");
                                    estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicums.get(j), null, gradoLista, null, estaEvaluado));
                                }else{
                                    // Caso donde el filtro se complete con Grado y Nombre
                                    if (gradoSeleccionado != null && gradoLista.getNombre().equals(gradoSeleccionado.getNombre()) && listadoEstudiantes.get(i).getNombreCompletoOrdenado().contains(nombreAlumn)) {
                                        System.out.println("Caso de filtro con ambas cosas Grado y Nombres");
                                        estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicums.get(j), null, gradoLista, null, estaEvaluado));
                                    }
                                }
                            }
                        }
                    }
                }
                if(user.tieneRol("ROL_TUTOR_UR") || user.tieneRol("ROL_TUTOR_CENTRO")) {
                    Practicum practicum = practicumService.getPracticumActivoEstudiante(listadoEstudiantes.get(i), metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                    if (practicum != null) {
                        Grado gradoLista = listadoEstudiantes.get(i).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                        if (!this.contieneGrado(grados, gradoLista)) {
                            grados.add(gradoLista);
                        }
                        // Comprobamos si el usuario ya ha evaluado al alumno o no
                        if (user.tieneRol("ROL_TUTOR_UR")) {
                            if (practicum.getNotaTutorUR() != 0.0) {
                                estaEvaluado = true;
                            }
                        }
                        if (user.tieneRol("ROL_TUTOR_CENTRO")) {
                            NotaTutorCentro.IdNotaTutorCentro id = new NotaTutorCentro.IdNotaTutorCentro();
                            id.setCodnumTutorCentro(user.getId());
                            id.setId_practicum(practicum.getId());
                            NotaTutorCentro nota = notaTutorCentroService.getNotaTutorCentroPorId(id);
                            if (nota.getNota() != 0.0) {
                                estaEvaluado = true;
                            }
                        }
                        // Caso donde el filtro solo se complete con Grado
                        if (nombreAlumn == "" && gradoSeleccionado != null && gradoLista.equals(gradoSeleccionado)) {
                            estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicum, null, gradoLista, null, estaEvaluado));
                        }else{
                            // Caso donde el filtro solo se complete con Nombre
                            if (grado == 0 && listadoEstudiantes.get(i).getNombreCompletoOrdenado().contains(nombreAlumn)) {
                                estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicum, null, gradoLista, null, estaEvaluado));
                            }else{
                                // Caso donde el filtro se complete con Grado y Nombre
                                if (gradoSeleccionado != null && gradoLista.equals(gradoSeleccionado) && listadoEstudiantes.get(i).getNombreCompletoOrdenado().contains(nombreAlumn)) {
                                    estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicum, null, gradoLista, null, estaEvaluado));
                                }
                            }
                        }
                    }
                }
            }
            model.addAttribute("grados", grados);
            model.addAttribute("gradoSeleccionado", grado);
            model.addAttribute("nombreSeleccionado", nombreAlumn);
            model.addAttribute("tutorizados", estudiantesAMostrar);
            return "evaluacionAlumnado";
        }
        return "redirect:/";
    }

    @GetMapping("/calificacion/evaluacionAlumnado/{id}")
    public String evaluacionAlumnoConcreto(@PathVariable Integer id, Model model) {
        model.addAttribute("nivel1", Nivelndicador.SOBRESALIENTE.descripcion);
        model.addAttribute("nivel2", Nivelndicador.NOTABLE.descripcion);
        model.addAttribute("nivel3", Nivelndicador.APROBADO.descripcion);
        model.addAttribute("nivel4", Nivelndicador.SUSPENSO.descripcion);
        model.addAttribute("valorarRubrica", true);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if (user != null) {
            Estudiante estudiante = estudianteService.getEstudiantePorCodnum(id);
            //Caso de EVALUACION por TUTOR de UR
            if (estudiante != null && user.tieneRol("ROL_TUTOR_UR") && tutorURService.getTutorURPorCodnum(user.getId()).tieneEstudiante(estudiante)) {
                //Obtenemos rúbrica correspondiente al grado del estudiante en ese curso academico
                Grado gradoUser = estudiante.obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                Rubrica rubrica = gradoUser.getRubricaTutorUR();
                if (rubrica != null) {
                    List<Criterio> criteriosRubrica = rubrica.getCriterios();
                    List<PesoCriterio> pesosCriterios = new ArrayList<>();
                    for (int i = 0; i < criteriosRubrica.size(); i++) {
                        PesoCriterio.IdPesoCriterio idPesoCriterio = new PesoCriterio.IdPesoCriterio();
                        idPesoCriterio.setId_Rubrica(rubrica.getId());
                        idPesoCriterio.setId_Criterio(criteriosRubrica.get(i).getId());
                        pesosCriterios.add(pesoCriterioService.getPesoCriterioPorId(idPesoCriterio));
                    }
                    model.addAttribute("rubrica", rubrica);
                    model.addAttribute("criterios", criteriosRubrica);
                    model.addAttribute("pesosCriterios", pesosCriterios);
                    model.addAttribute("tipoTutor", "tutor de la UR");
                    model.addAttribute("nombreEstud", estudiante.getNombreCompletoOrdenado());
                    model.addAttribute("idEstud", estudiante.getId());
                    model.addAttribute("gradoAlum", gradoUser.getNombre());
                    Mencion mencion = metodosGenerales.getMencionByCurso(estudiante.getMenciones(), metodosGenerales.getCursoAcademico());
                    if (mencion != null) {
                        model.addAttribute("mencionAlum", mencion.getNombre());
                    }
                    return "rubricaEvaluacion";
                } else {
                    return "redirect:/calificacion";
                }
            }
            // Caso de EVALUACION por TUTOR de CENTRO EDUCATIVO
            if (estudiante != null && user.tieneRol("ROL_TUTOR_CENTRO") && relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentroService.getTutorCentroPorCodnum(user.getId())).contains(estudiante)) {
                //Obtenemos rúbrica correspondiente al grado del estudiante en ese curso academico
                Grado gradoUser = estudiante.obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                Rubrica rubrica = gradoUser.getRubricaTutorUR();
                if (rubrica != null) {
                    List<Criterio> criteriosRubrica = rubrica.getCriterios();
                    List<PesoCriterio> pesosCriterios = new ArrayList<>();
                    for (int i = 0; i < criteriosRubrica.size(); i++) {
                        PesoCriterio.IdPesoCriterio idPesoCriterio = new PesoCriterio.IdPesoCriterio();
                        idPesoCriterio.setId_Rubrica(rubrica.getId());
                        idPesoCriterio.setId_Criterio(criteriosRubrica.get(i).getId());
                        pesosCriterios.add(pesoCriterioService.getPesoCriterioPorId(idPesoCriterio));
                    }
                    model.addAttribute("rubrica", rubrica);
                    model.addAttribute("criterios", criteriosRubrica);
                    model.addAttribute("pesosCriterios", pesosCriterios);
                    model.addAttribute("tipoTutor", "tutor del centro educativo");
                    model.addAttribute("nombreEstud", estudiante.getNombreCompletoOrdenado());
                    model.addAttribute("idEstud", estudiante.getId());
                    model.addAttribute("gradoAlum", gradoUser.getNombre());
                    Mencion mencion = metodosGenerales.getMencionByCurso(estudiante.getMenciones(), metodosGenerales.getCursoAcademico());
                    if (mencion != null) {
                        model.addAttribute("mencionAlum", mencion.getNombre());
                    }
                    return "rubricaEvaluacion";
                } else {
                    return "redirect:/calificacion";
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping("/evaluacionAlumnado")//Ruta a la que solo acceden TUTOR_UR o TUTOR_CENTRO
    public String confirmarNuevaEvaluacionAlumnado(@RequestParam("idEstudEvaluado") Integer idEstudEvaluado, @RequestParam("notaMedia") float notaMedia, Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if (user != null) {
            Estudiante estudiante = estudianteService.getEstudiantePorCodnum(idEstudEvaluado);
            //Caso de EVALUACION por TUTOR de UR
            if (estudiante != null && user.tieneRol("ROL_TUTOR_UR") && tutorURService.getTutorURPorCodnum(user.getId()).tieneEstudiante(estudiante)) {
                Practicum practicumActivo = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                if (practicumActivo != null) {
                    practicumActivo.setNotaTutorUR(notaMedia);
                    practicumActivo.setFechaNotaTutorUR(new Date());
                    practicumService.guardarPracticum(practicumActivo);
                    this.intentarAsignarNotaFinal(practicumActivo);
                    return "redirect:/calificacion";
                }
            }
            // Caso de EVALUACION por TUTOR de CENTRO EDUCATIVO
            if (estudiante != null && user.tieneRol("ROL_TUTOR_CENTRO") && relacionEstudianteTutorCentroService.getListaEstudiantesPorTutor(tutorCentroService.getTutorCentroPorCodnum(user.getId())).contains(estudiante)) {
                Practicum practicumActivo = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                if (practicumActivo != null) {
                    NotaTutorCentro.IdNotaTutorCentro idNotaTutorCentro = new NotaTutorCentro.IdNotaTutorCentro();
                    idNotaTutorCentro.setId_practicum(practicumActivo.getId());
                    idNotaTutorCentro.setCodnumTutorCentro(user.getId());
                    NotaTutorCentro nota = notaTutorCentroService.getNotaTutorCentroPorId(idNotaTutorCentro);
                    nota.setNota(notaMedia);
                    nota.setFechaNota(new Date());
                    notaTutorCentroService.guardarNotaTutorCentro(nota);
                    this.intentarAsignarNotaFinal(practicumActivo);
                    return "redirect:/calificacion";
                }
            }
        }
        return "redirect:/";
    }

    // Url solo para consultar calificaciones del alumnado por rol COORDINADOR
    @GetMapping("/calificacion/consultarCalificacionFinal/{idPracticum}")
    public String consultaCalificacionFinalAlumnoConcreto(@PathVariable Integer idPracticum, Model model, HttpServletRequest request) {
        System.out.println("Id de practicum: "+idPracticum);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if (user != null && user.tieneRol("ROL_COORDINADOR")) {
            Practicum practicumAMostrar = practicumService.getPracticum(idPracticum);
            /*List<Practicum> practicums = practicumService.getPracticumsEstudiante(estudianteService.getEstudiantePorCodnum(idPracticum));
            Practicum practicumAMostrar = null;
            for(int i=0;i<practicums.size();i++){
                if(practicums.get(i).getId()==42){
                    practicumAMostrar = practicums.get(i);
                }
            }*/
            if (practicumAMostrar != null) {
                List<NotaTutorCentro> notasTutoresCentro = notaTutorCentroService.getNotasTutoresPorPracticum(practicumAMostrar);
                if (!notasTutoresCentro.isEmpty()) {
                    if (notasTutoresCentro.size() == 2) {
                        model.addAttribute("variosTutores", true);
                    } else {
                        model.addAttribute("variosTutores", false);
                    }
                    for (int i = 0; i < notasTutoresCentro.size(); i++) {
                        if (notasTutoresCentro.get(i).getEspecialidad().equals(Especialidad.GENERALISTA)) {
                            model.addAttribute("notaTutorCentroGeneralista", notasTutoresCentro.get(i).getNota());
                            model.addAttribute("fechNotaTutorCentroGeneralista", notasTutoresCentro.get(i).getFechaNota());
                        } else {
                            model.addAttribute("notaTutorCentroMencion", notasTutoresCentro.get(i).getNota());
                            model.addAttribute("fechNotaTutorCentroMencion", notasTutoresCentro.get(i).getFechaNota());
                            model.addAttribute("notaFinalMencion", practicumAMostrar.getNotaFinalMencion());
                            model.addAttribute("fechNotaFinalMencion", practicumAMostrar.getFechaNFMencion());
                        }
                    }
                    model.addAttribute("notaTutorUR", practicumAMostrar.getNotaTutorUR());
                    model.addAttribute("fechNotaTutorUR", practicumAMostrar.getFechaNotaTutorUR());
                    model.addAttribute("notaFinal", practicumAMostrar.getNotaFinal());
                    model.addAttribute("fechNotaFinal", practicumAMostrar.getFechaNF());
                    model.addAttribute("nombreAlumn", practicumAMostrar.getEstudiante().getNombreCompletoOrdenado());
                    model.addAttribute("urlFlecha", "/calificacion");
                    return "verCalificacion";
                } else {
                    return "redirect:/";
                }
            } else {
                System.out.println("El practicum es NULO");
                return "redirect:/calificacion";
            }
        }
        return "redirect:/";
    }

    public void intentarAsignarNotaFinal(Practicum practicum){
        double notaFinal, notaFinalMencion;
        List<NotaTutorCentro> notasTutoresCentro = notaTutorCentroService.getNotasTutoresPorPracticum(practicum);
        if(practicum.getNotaTutorUR() != 0.0 && notasTutoresCentro.size() == 1 && notasTutoresCentro.get(0).getNota() != 0.0){
            notaFinal = Math.round((notasTutoresCentro.get(0).getNota() * 0.6 + practicum.getNotaTutorUR() * 0.4) * 100d) / 100d;
            practicum.setNotaFinal((float) notaFinal);
            practicum.setFechaNF(new Date());
            practicumService.guardarPracticum(practicum);
        }
        if(practicum.getNotaTutorUR()!=0.0 && notasTutoresCentro.size()==2){
            if(notasTutoresCentro.get(0).getNota() != 0.0){
                if(notasTutoresCentro.get(0).getEspecialidad().equals(Especialidad.GENERALISTA) && practicum.getNotaFinal() == 0.0){
                    notaFinal = Math.round((notasTutoresCentro.get(0).getNota() * 0.6 + practicum.getNotaTutorUR() * 0.4) * 100d) / 100d;
                    practicum.setNotaFinal((float) notaFinal);
                    practicum.setFechaNF(new Date());
                    practicumService.guardarPracticum(practicum);
                }
                else{
                    if(practicum.getNotaFinalMencion() == 0.0) {
                        notaFinalMencion = Math.round((notasTutoresCentro.get(0).getNota() * 0.6 + practicum.getNotaTutorUR() * 0.4) * 100d) / 100d;
                        practicum.setNotaFinalMencion((float) notaFinalMencion);
                        practicum.setFechaNFMencion(new Date());
                        practicumService.guardarPracticum(practicum);
                    }
                }
            }
            if(notasTutoresCentro.get(1).getNota()!=0.0){
                if(notasTutoresCentro.get(1).getEspecialidad().equals(Especialidad.GENERALISTA) && practicum.getNotaFinal() == 0.0){
                    notaFinal = Math.round((notasTutoresCentro.get(1).getNota() * 0.6 + practicum.getNotaTutorUR() * 0.4) * 100d) / 100d;
                    practicum.setNotaFinal((float) notaFinal);
                    practicum.setFechaNF(new Date());
                    practicumService.guardarPracticum(practicum);
                }
                else{
                    if(practicum.getNotaFinalMencion()==0.0){
                        notaFinalMencion = Math.round((notasTutoresCentro.get(1).getNota() * 0.6 + practicum.getNotaTutorUR() * 0.4) * 100d) / 100d;
                        practicum.setNotaFinalMencion((float) notaFinalMencion);
                        practicum.setFechaNFMencion(new Date());
                        practicumService.guardarPracticum(practicum);
                    }
                }
            }
        }
    }

    public boolean estaEvaluado(Estudiante estudiante, Practicum practicum){
        // Caso de que estudiante está matriculado en mención
        if(estudiante.obtenerMencionPorCurso(metodosGenerales.getCursoAcademico()) != null){
            System.out.println("Estamos en caso de matricula en Mencion");
            if(practicum.getNotaFinalMencion() != 0.0 && practicum.getNotaFinal() != 0.0){
                return true;
            }
            else{
                return false;
            }
        }
        // Caso en que estudiante NO está matriculado en ninguna mención
        else{
            System.out.println("Estamos en caso de matricula solo GENERALISTA");
            if(practicum.getNotaFinal()!=0.0){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public boolean contieneGrado(List<Grado> listadoGrados, Grado grado){
        for(int i=0;i<listadoGrados.size();i++){
            /*System.out.println("");
            System.out.println("listadoGrados.get(i).getNombre(): "+listadoGrados.get(i).getNombre());
            System.out.println("grado.getNombre(): "+grado.getNombre());
            System.out.println(listadoGrados.get(i).getNombre().equals(grado.getNombre()));
            System.out.println("");*/
            if(listadoGrados.get(i).getNombre().equals(grado.getNombre())){
                return true;
            }
        }
        return false;
    }

}