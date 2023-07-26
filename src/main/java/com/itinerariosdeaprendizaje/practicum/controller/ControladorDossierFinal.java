package com.itinerariosdeaprendizaje.practicum.controller;

import com.itinerariosdeaprendizaje.practicum.model.*;
import com.itinerariosdeaprendizaje.practicum.service.*;
import com.itinerariosdeaprendizaje.practicum.utils.MetodosGenerales;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/dossierFinal")
public class ControladorDossierFinal {
    private MetodosGenerales metodosGenerales = new MetodosGenerales();
    @Autowired
    private EstudianteService estudianteService;
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

    @GetMapping("")
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

    @PostMapping("/crearDossier") //Ruta a la que solo acceden ESTUDIANTES
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

    @PostMapping("/modificarDossier") //Ruta a la que solo acceden ESTUDIANTES
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
}
