package com.itinerariosdeaprendizaje.practicum.controller;

import com.itinerariosdeaprendizaje.practicum.model.*;
import com.itinerariosdeaprendizaje.practicum.service.*;
import com.itinerariosdeaprendizaje.practicum.utils.MetodosGenerales;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ControladorDossierFinal {
    private MetodosGenerales metodosGenerales = new MetodosGenerales();
    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private TutorURService tutorURService;
    @Autowired
    private TutorCentroService tutorCentroService;
    @Autowired
    private PracticumService practicumService;
    @Autowired
    private DossierService dossierService;
    @Autowired
    private DocumentosService documentosService;
    @Autowired
    private RelacionEstudianteTutorCentroService relacionEstudianteTutorCentroService;
    @Autowired
    private NotaTutorCentroService notaTutorCentroService;
    @Autowired
    private GradoService gradoService;

    @GetMapping("/dossierFinal")
    public String consultaDossierFinal(Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user!=null) {
            Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
            Practicum practicumActivo = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(),metodosGenerales.getConvocatoriaPorFechas());
            if(practicumActivo==null){
                // Creamos un nuevo practicum para ese usuario en el caso de que no tenga ya creado uno para ese curso y convocatoria
                practicumActivo = new Practicum();
                practicumActivo.setCurso(metodosGenerales.getCursoAcademico());
                practicumActivo.setConvocatoria(metodosGenerales.getConvocatoriaPorFechas());
                practicumActivo.setEstudiante(estudiante);
                practicumActivo.setTutorUR(estudiante.getTutorUR());
                practicumActivo=practicumService.guardarPracticum(practicumActivo);
                // Creamos también las filas correspondientes a las notas de tutores de centro para estudiantes
                List<RelacionEstudianteTutorCentro> relacionesEstudTutorCentro = relacionEstudianteTutorCentroService.getTutoresPorEstudiante(estudiante);
                for(int i=0;i<relacionesEstudTutorCentro.size();i++){
                    NotaTutorCentro.IdNotaTutorCentro id = new NotaTutorCentro.IdNotaTutorCentro();
                    id.setCodnumTutorCentro(relacionesEstudTutorCentro.get(i).getTutorCentro().getId());
                    id.setId_practicum(practicumActivo.getId());
                    NotaTutorCentro notaTutorCentro = new NotaTutorCentro();
                    notaTutorCentro.setId(id);
                    notaTutorCentro.setEspecialidad(relacionesEstudTutorCentro.get(i).getEspecialidad());
                    notaTutorCentroService.guardarNotaTutorCentro(notaTutorCentro);
                }
            }
            Dossier dossier = practicumActivo.getDossier();
            if(dossier != null){
                model.addAttribute("rutaDocu", "/files/dossierFinal/" + dossier.getNombreDocu());
                model.addAttribute("fechaEntrega", dossier.getFechaEntrega());
                model.addAttribute("feedbackTutor", dossier.getFeedbackTutorUR());
                model.addAttribute("fechaFeedback", dossier.getFechaEntrega());
            }
            model.addAttribute("dossierFinal", dossier);
            return "dossierFinal";
        }
        return "redirect:/";
    }

    @PostMapping("/dossierFinal/crearDossier") //Ruta a la que solo acceden ESTUDIANTES
    public String nuevoDossierFinal(@RequestParam("docuDossier") MultipartFile docu, Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if (user != null) {
            Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
            Practicum practicumActivo = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
            if (practicumActivo != null) {
                Dossier dossier = new Dossier();
                dossier.setNombreDocu(user.getId()+docu.getOriginalFilename().replace(" ", "_").trim());
                dossier.setFechaEntrega(new Date());
                dossier.setPracticum(practicumActivo);
                dossier = dossierService.guardarDossier(dossier);
                practicumActivo.setDossier(dossier);
                documentosService.guardarDossier(docu, user.getId());
                model.addAttribute("accion", "crear");
                return "cambioDossier";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/dossierFinal/modificarDossier") //Ruta a la que solo acceden ESTUDIANTES
    public String modificarDossierFinal(@RequestParam("docuDossier") MultipartFile docu, @RequestParam("idDossier") Integer idDossier, Model model){
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession sesion = attr.getRequest().getSession(true);
            Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
            if (user != null) {
                Dossier dossier = dossierService.getDossierPorId(idDossier);
                String docuAntiguo = dossier.getNombreDocu();
                documentosService.guardarDossier(docu, user.getId());
                System.out.println("El nombre del archivo a guardar es: "+user.getId()+docu.getOriginalFilename().replace(" ", "_").trim());
                dossier.setNombreDocu(user.getId()+docu.getOriginalFilename().replace(" ", "_").trim());
                dossier.setFechaEntrega(new Date());
                dossier = dossierService.guardarDossier(dossier);
                documentosService.eliminarDossier(docuAntiguo);
                return "cambioDossier";
            }
            return "redirect:/";
        }
        catch (IOException exception){
            return "redirect:/";
        }
    }

    @GetMapping("/dossierAlumnado")
    public String consultaDossierFinalListadoAlumnos(Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        List<EstudianteTabla> estudiantes = new ArrayList<>();
        if(user!=null) {
            List<Grado> grados = new ArrayList<>();
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
                    estudiantes.add(new EstudianteTabla(tutorizados.get(i).getId(), tutorizados.get(i).getNombreCompletoOrdenado(), practicum, null, grado, null, false));
                }
            }
            model.addAttribute("grados",grados);
            model.addAttribute("gradoSeleccionado", 0);
            model.addAttribute("tutorizados", estudiantes);
            return "dossierAlumnado";
        }
        return "redirect:/";
    }

    @GetMapping("/dossierAlumnado/consultarDossierFinal/{id}")
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
                    model.addAttribute("urlFlecha", "/dossierAlumnado");
                    return "dossierFinal";
                }
            }
        }
        return "redirect:/";
    }

    @PostMapping("/dossierAlumnado/consultarDossierFinal/nuevoComentario/{id}") // Ruta solo para rol TUTOR_UR
    public String nuevoComentarioDossierFinal(@PathVariable Integer id, @RequestParam("feedbackTutor") String feedbackTutor, Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user!=null) {
            Estudiante estudiante = estudianteService.getEstudiantePorCodnum(id);
            if(estudiante!=null) {
                Practicum practicum = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                Dossier dossier = practicum.getDossier();
                dossier.setFeedbackTutorUR(feedbackTutor);
                dossier.setFechaFeedback(new Date());
                dossierService.guardarDossier(dossier);
            }
            return "redirect:/dossierAlumnado/consultarDossierFinal/"+id;
        }
        return "redirect:/";
    }

    @PostMapping("/dossierAlumnado") //Ruta a la que solo acceden TUTOR_UR o TUTOR_CENTRO
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
                        return "redirect:/dossierAlumnado";
                    }
                    // Caso donde el filtro solo se complete con Grado
                    if (gradoLista.equals(gradoService.getGradoPorId(grado)) && nombreAlumn=="") {
                        estudiantes.add(new EstudianteTabla(tutorizados.get(i).getId(), tutorizados.get(i).getNombreCompletoOrdenado(), practicum, null, gradoLista, null, false));
                    }
                    else{
                        // Caso donde el filtro solo se complete con Nombre
                        if (grado==0 && tutorizados.get(i).getNombreCompletoOrdenado().contains(nombreAlumn)) {
                            estudiantes.add(new EstudianteTabla(tutorizados.get(i).getId(), tutorizados.get(i).getNombreCompletoOrdenado(), practicum, null, gradoLista, null, false));
                        }
                        if (gradoLista.equals(gradoService.getGradoPorId(grado)) && tutorizados.get(i).getNombreCompletoOrdenado().contains(nombreAlumn)) {
                            estudiantes.add(new EstudianteTabla(tutorizados.get(i).getId(), tutorizados.get(i).getNombreCompletoOrdenado(), practicum, null, gradoLista, null, false));
                        }
                    }
                }
            }
            model.addAttribute("grados", grados);
            model.addAttribute("gradoSeleccionado", grado);
            model.addAttribute("nombreSeleccionado", nombreAlumn);
            model.addAttribute("tutorizados",estudiantes);
            return "dossierAlumnado";
        }
        return "redirect:/";
    }
}
