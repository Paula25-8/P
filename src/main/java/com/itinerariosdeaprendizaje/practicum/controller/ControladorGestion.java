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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private GradoService gradoService;
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
            if(usuario.tieneRol("ROL_ADMINISTRADOR")){
                return "indexGestion";
            }
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
        SelloCalidad sello = selloService.getSelloPorCurso(metodosGenerales.getCursoAcademico());
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

    /** Implementación de métodos para ZONA DE ADMINISTRACION **/

    // Url para acceder a ZONA DE ADMINISTRACION para rol COORDINADOR
    @GetMapping("/administracion")
    public String paginaInicioParaCoordinador(Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user !=null && user.tieneRol("ROL_ADMINISTRADOR")) {

            return "indexAdmin";
        }
        return "redirect:/";
    }

    /* OPCION MENU: GESTION > PREGUNTAS GENERALES */
    @GetMapping("/gestionPreguntasGenerales")
    public String paginaGestionPreguntas(Model model){
        model.addAttribute("cursos", metodosGenerales.getCursos());
        model.addAttribute("preguntas", preguntaServicio.getPreguntas());
        model.addAttribute("curso", "-");
        return "gestionPreguntasGenerales";
    }

    // Url para incluir nueva pregunta
    @PostMapping("/gestionPreguntasGenerales/nuevaPregunta")
    public String aniadirNuevaPreguntas(@RequestParam("enunciado") String enunciado, @RequestParam("respuesta") String respuesta, @RequestParam("curso") String curso, Model model){
        if(enunciado.equals("") || respuesta.indexOf("<p>")==-1 || respuesta.indexOf("</p>")==-1 || !metodosGenerales.escritoEnHTML(respuesta) || curso.equals("-")){
            if(enunciado.equals("")){
                model.addAttribute("errorEnunciado", "Debe escribir un enunciado");
            }
            if(respuesta.indexOf("<p>")==-1 || respuesta.indexOf("</p>")==-1 || !metodosGenerales.escritoEnHTML(respuesta)){
                model.addAttribute("errorRespuesta", "Debe escribir correctamente la respuesta a la pregunta como un párrafo en HTML");
            }
            if(curso.equals("-")){
                model.addAttribute("errorCurso", "Debe seleccionar un curso");
            }
            model.addAttribute("enunciado", enunciado);
            model.addAttribute("respuesta", respuesta);
            model.addAttribute("curso", curso);
            model.addAttribute("cursos", metodosGenerales.getCursos());
            model.addAttribute("preguntas", preguntaServicio.getPreguntas());
            return "gestionPreguntasGenerales";
        }
        else{
            PreguntaGeneral nuevaPregunta = new PreguntaGeneral();
            nuevaPregunta.setEnunciado(enunciado);
            nuevaPregunta.setRespuesta(respuesta);
            nuevaPregunta.setCurso(curso);
            preguntaServicio.guardarPregunta(nuevaPregunta);
            return "redirect:/gestionPreguntasGenerales";
        }
    }

    @GetMapping("/gestionPreguntasGenerales/editarPregunta/{id}")
    public String paginaEditarPregunta(@PathVariable("id") Integer id, Model model) {
        PreguntaGeneral pregunta = preguntaServicio.getPregunta(id);
        if(pregunta!=null){
            model.addAttribute("idPregunta", id);
            model.addAttribute("enunciado", pregunta.getEnunciado());
            model.addAttribute("respuesta", pregunta.getRespuesta());
            model.addAttribute("curso", pregunta.getCurso());
            model.addAttribute("cursos", metodosGenerales.getCursos());
            return "editarPregunta";
        }
        return "redirect:/gestionPreguntasGenerales";
    }

    // Url para editar pregunta concreta
    @PostMapping("/gestionPreguntasGenerales/editarPregunta")
    public String editarPregunta(@RequestParam("idPregunta") Integer idPregunta, @RequestParam("enunciado") String enunciado, @RequestParam("respuesta") String respuesta, @RequestParam("curso") String curso, Model model){
        if(enunciado.equals("") || respuesta.indexOf("<p>")==-1 || respuesta.indexOf("</p>")==-1 || !metodosGenerales.escritoEnHTML(respuesta) || curso.equals("-")){
            if(enunciado.equals("")){
                model.addAttribute("errorEnunciado", "Debe escribir un enunciado");
            }
            if(respuesta.indexOf("<p>")==-1 || respuesta.indexOf("</p>")==-1 || !metodosGenerales.escritoEnHTML(respuesta)){
                model.addAttribute("errorRespuesta", "Debe escribir correctamente la respuesta a la pregunta como un párrafo en HTML");
            }
            if(curso.equals("-")){
                model.addAttribute("errorCurso", "Debe seleccionar un curso");
            }
            model.addAttribute("idPregunta", idPregunta);
            model.addAttribute("enunciado", enunciado);
            model.addAttribute("respuesta", respuesta);
            model.addAttribute("curso", curso);
            model.addAttribute("cursos", metodosGenerales.getCursos());
            return "editarPregunta";
        }
        else{
            PreguntaGeneral preguntaGeneral = preguntaServicio.getPregunta(idPregunta);
            if(preguntaGeneral!=null){
                if(!enunciado.equals(preguntaGeneral.getEnunciado())){
                    preguntaGeneral.setEnunciado(enunciado);
                }
                if(!respuesta.equals(preguntaGeneral.getRespuesta())){
                    preguntaGeneral.setRespuesta(respuesta);
                }
                if(!curso.equals(preguntaGeneral.getCurso())){
                    preguntaGeneral.setCurso(curso);
                }
                preguntaServicio.guardarPregunta(preguntaGeneral);
            }
            return "redirect:/gestionPreguntasGenerales";
        }
    }

    /*OPCION MENU: GESTION > LINEAS APREND */
    @GetMapping("/gestionLineasAprendizaje")
    public String paginaGestionLineasAprend(Model model){
        model.addAttribute("cursos", metodosGenerales.getCursos());
        model.addAttribute("lineas", lineasAprendService.getLineas());
        model.addAttribute("numLineasDisponibles", metodosGenerales.getNumLineasDisponibles());
        model.addAttribute("grados", gradoService.getNombreGrados());
        // Inicializamos los valores de los distintos selects
        model.addAttribute("curso", "-");
        model.addAttribute("numLinea", 0);
        List<String> grados = new ArrayList<>();
        grados.add("-");
        model.addAttribute("gradosSeleccionados", grados);
        return "gestionLineasAprendizaje";
    }

    // Url para incluir nueva línea de aprendizaje
    @PostMapping("/gestionLineasAprendizaje/nuevaLinea")
    public String aniadirNuevaLinea(@RequestParam("titulo") String titulo, @RequestParam("descripcion") String descripcion, @RequestParam("curso") String curso, @RequestParam("numeroLinea") Integer numeroLinea, @RequestParam("grados") List<String> grados, Model model){
        if(titulo.equals("") || descripcion.indexOf("<p>")==-1 || descripcion.indexOf("</p>")==-1 || !metodosGenerales.escritoEnHTML(descripcion) || curso.equals("-") || numeroLinea==0 || (grados.size()==1 && grados.get(0).equals("-"))){
            if(titulo.equals("")){
                model.addAttribute("errorTitulo", "Debe escribir un título");
            }
            if(descripcion.indexOf("<p>")==-1 || descripcion.indexOf("</p>")==-1 || !metodosGenerales.escritoEnHTML(descripcion)){
                model.addAttribute("errorDescripcion", "Debe escribir correctamente la descripción de la línea como un párrafo en HTML");
            }
            if(curso.equals("-")){
                model.addAttribute("errorCurso", "Debe seleccionar un curso");
            }
            if(numeroLinea==0){
                model.addAttribute("errorNumLinea", "Debe seleccionar un número de línea");
            }
            if(grados.size()==1 && grados.get(0).equals("-")){
                model.addAttribute("errorGrados", "Debe seleccionar mínimo un grado");
            }
            model.addAttribute("titulo", titulo);
            model.addAttribute("descripcion", descripcion);
            model.addAttribute("curso", curso);
            model.addAttribute("numLinea", numeroLinea);
            model.addAttribute("gradosSeleccionados", grados);
            model.addAttribute("cursos", metodosGenerales.getCursos());
            model.addAttribute("lineas", lineasAprendService.getLineas());
            model.addAttribute("numLineasDisponibles", metodosGenerales.getNumLineasDisponibles());
            model.addAttribute("grados", gradoService.getNombreGrados());
            return "gestionLineasAprendizaje";
        }
        else{
            // Antes de manipular el array de los grados eliminamos la opción trivial '-'
            grados.remove("-");
            // Creo nueva línea y la guardo en BD incluyendo los nuevos grados seleccionados
            LineaAprend nuevaLinea = new LineaAprend();
            nuevaLinea.setTitulo(titulo);
            nuevaLinea.setDescripcion(descripcion);
            nuevaLinea.setCurso(curso);
            nuevaLinea.setNumeroLinea(numeroLinea);
            List<Grado> gradosLinea = new ArrayList<>();
            Grado gradoSeleccionado = null;
            for(int i=0;i<grados.size();i++){
                gradoSeleccionado = gradoService.getGradoPorNombreYCurso(grados.get(i), curso);
                if(gradoSeleccionado==null){
                    return "redirect:/gestionLineasAprendizaje";
                }
                else{
                    gradosLinea.add(gradoSeleccionado);
                }
            }
            nuevaLinea.setGrados(gradosLinea);
            lineasAprendService.guardarLinea(nuevaLinea);
            // Modifico cada grado seleccionado para incluir la nueva línea a su colección
            for(int i=0;i<gradosLinea.size();i++) {
                List<LineaAprend> lineasGrado = gradosLinea.get(i).getLineas();
                lineasGrado.add(nuevaLinea);
                gradosLinea.get(i).setLineas(lineasGrado);
                gradoService.guardarGrado(gradosLinea.get(i));
            }
            return "redirect:/gestionLineasAprendizaje";
        }
    }

    @GetMapping("/gestionLineasAprendizaje/editarLinea/{id}")
    public String paginaEditarLinea(@PathVariable("id") Integer id, Model model) {
        LineaAprend linea = lineasAprendService.getLineasPorId(id);
        if(linea!=null){
            model.addAttribute("idLinea", id);
            model.addAttribute("titulo", linea.getTitulo());
            model.addAttribute("descripcion", linea.getDescripcion());
            model.addAttribute("numLinea", linea.getNumeroLinea());
            model.addAttribute("curso", linea.getCurso());
            model.addAttribute("gradosSeleccionados", lineasAprendService.getNombreGradosLinea(linea));
            model.addAttribute("cursos", metodosGenerales.getCursos());
            model.addAttribute("numLineasDisponibles", metodosGenerales.getNumLineasDisponibles());
            model.addAttribute("grados", gradoService.getNombreGrados());
            return "editarLineaAprendizaje";
        }
        return "redirect:/gestionLineasAprendizaje";
    }

    // Url para editar una línea de aprendizaje concreta
    @PostMapping("/gestionLineasAprendizaje/editarLinea")
    public String paginaEditarLinea(@RequestParam("idLinea") Integer idLinea, @RequestParam("titulo") String titulo, @RequestParam("descripcion") String descripcion, @RequestParam("curso") String curso, @RequestParam("numeroLinea") Integer numeroLinea, @RequestParam("grados") List<String> grados, Model model) {
        if (titulo.equals("") || descripcion.indexOf("<p>") == -1 || descripcion.indexOf("</p>") == -1 || !metodosGenerales.escritoEnHTML(descripcion) || curso.equals("-") || numeroLinea == 0 || (grados.size() == 1 && grados.get(0).equals("-"))) {
            if (titulo.equals("")) {
                model.addAttribute("errorTitulo", "Debe escribir un título");
            }
            if (descripcion.indexOf("<p>") == -1 || descripcion.indexOf("</p>") == -1 || !metodosGenerales.escritoEnHTML(descripcion)) {
                model.addAttribute("errorDescripcion", "Debe escribir correctamente la descripción de la línea como un párrafo en HTML");
            }
            if (curso.equals("-")) {
                model.addAttribute("errorCurso", "Debe seleccionar un curso");
            }
            if (numeroLinea == 0) {
                model.addAttribute("errorNumLinea", "Debe seleccionar un número de línea");
            }
            if (grados.size() == 1 && grados.get(0).equals("-")) {
                model.addAttribute("errorGrados", "Debe seleccionar mínimo un grado");
            }
            model.addAttribute("idLinea", idLinea);
            model.addAttribute("titulo", titulo);
            model.addAttribute("descripcion", descripcion);
            model.addAttribute("curso", curso);
            model.addAttribute("numLinea", numeroLinea);
            model.addAttribute("gradosSeleccionados", grados);
            model.addAttribute("cursos", metodosGenerales.getCursos());
            model.addAttribute("numLineasDisponibles", metodosGenerales.getNumLineasDisponibles());
            model.addAttribute("grados", gradoService.getNombreGrados());
            return "editarLineaAprendizaje";
        }
        else {
            // Edito línea seleccionada y la guardo en BD incluyendo las modificaciones seleccionadas
            LineaAprend lineaEditar = lineasAprendService.getLineasPorId(idLinea);
            if(lineaEditar!=null) {
                if(!titulo.equals(lineaEditar.getTitulo())){
                    lineaEditar.setTitulo(titulo);
                }
                if(!descripcion.equals(lineaEditar.getDescripcion())){
                    lineaEditar.setDescripcion(descripcion);
                }
                if(!curso.equals(lineaEditar.getCurso())) {
                    lineaEditar.setCurso(curso);
                }
                if(numeroLinea!=lineaEditar.getNumeroLinea()){
                    lineaEditar.setNumeroLinea(numeroLinea);
                }
                // Creo array de grados seleccionados
                List<Grado> gradosLineaNuevos = new ArrayList<>();
                List<Grado> gradosLineaViejos = lineaEditar.getGrados();
                Grado gradoSeleccionado = null;
                for (int i = 0; i < grados.size(); i++) {
                    gradoSeleccionado = gradoService.getGradoPorNombreYCurso(grados.get(i), curso);
                    if (gradoSeleccionado == null) {
                        return "redirect:/gestionLineasAprendizaje";
                    } else {
                        gradosLineaNuevos.add(gradoSeleccionado);
                    }
                }
                if(!metodosGenerales.arrayGradosIguales(gradosLineaViejos, gradosLineaNuevos)) {
                    lineaEditar.setGrados(gradosLineaNuevos);
                    // Borro línea de grados que NO han sido seleccionados en edición y previamente SÍ
                    for(int j=0;j<gradosLineaViejos.size();j++){
                        if(!gradosLineaNuevos.contains(gradosLineaViejos.get(j))){
                            List<LineaAprend> lineasGradoViejo = gradosLineaViejos.get(j).getLineas();
                            lineasGradoViejo.remove(lineaEditar);
                            gradosLineaViejos.get(j).setLineas(lineasGradoViejo);
                            gradoService.guardarGrado(gradosLineaViejos.get(j));
                        }
                    }
                    // Incluyo línea en grados que SÍ han sido seleccionados en edición y previamente NO
                    for (int i = 0; i < gradosLineaNuevos.size(); i++) {
                        if(!gradosLineaViejos.contains(gradosLineaNuevos.get(i))) {
                            List<LineaAprend> lineasGrado = gradosLineaNuevos.get(i).getLineas();
                            lineasGrado.add(lineaEditar);
                            gradosLineaNuevos.get(i).setLineas(lineasGrado);
                            gradoService.guardarGrado(gradosLineaNuevos.get(i));
                        }
                    }
                }
                lineasAprendService.guardarLinea(lineaEditar);
            }
            return "redirect:/gestionLineasAprendizaje";
        }
    }

    /*OPCION MENU: GESTION > ESTACIONES APREND */
    @GetMapping("/gestionEstacionesAprendizaje")
    public String paginaGestionEstacionesAprend(Model model){
        model.addAttribute("cursos", metodosGenerales.getCursos());
        model.addAttribute("lineasDisponibles", this.getLineasDisponibles());


        model.addAttribute("lineas", lineasAprendService.getLineas());
        model.addAttribute("numLineasDisponibles", metodosGenerales.getNumLineasDisponibles());
        model.addAttribute("grados", gradoService.getNombreGrados());

        // Inicializamos los valores de los distintos selects
        model.addAttribute("curso", "-");
        model.addAttribute("idLista", 0);

        List<String> grados = new ArrayList<>();
        grados.add("-");
        model.addAttribute("gradosSeleccionados", grados);
        return "gestionEstacionesAprendizaje";
    }

    // Url para incluir nueva línea de aprendizaje
    @PostMapping("/gestionEstacionesAprendizaje/nuevaEstacion")
    public String aniadirNuevaEstacion(@RequestParam("titulo") String titulo, @RequestParam("errorCompetencias") List<String> competencias, @RequestParam("justificacion") String justificacion, @RequestParam("curso") String curso, @RequestParam("grados") List<String> grados, Model model){
        /*if(titulo.equals("") || justificacion.indexOf("<p>")==-1 || justificacion.indexOf("</p>")==-1 || !metodosGenerales.escritoEnHTML(justificacion) || curso.equals("-") || numeroLinea==0 || (grados.size()==1 && grados.get(0).equals("-"))){
            if(titulo.equals("")){
                model.addAttribute("errorTitulo", "Debe escribir un título");
            }
            if(justificacion.indexOf("<p>")==-1 || justificacion.indexOf("</p>")==-1 || !metodosGenerales.escritoEnHTML(justificacion)){
                model.addAttribute("errorJustificacion", "Debe escribir correctamente la justificacion de la estación como un párrafo en HTML");
            }
            if(curso.equals("-")){
                model.addAttribute("errorCurso", "Debe seleccionar un curso");
            }
            if(numeroLinea==0){
                model.addAttribute("errorNumLinea", "Debe seleccionar un número de línea");
            }
            if(grados.size()==1 && grados.get(0).equals("-")){
                model.addAttribute("errorGrados", "Debe seleccionar mínimo un grado");
            }
            model.addAttribute("titulo", titulo);
            model.addAttribute("descripcion", descripcion);
            model.addAttribute("curso", curso);
            model.addAttribute("numLinea", numeroLinea);
            model.addAttribute("gradosSeleccionados", grados);
            model.addAttribute("cursos", metodosGenerales.getCursos());
            model.addAttribute("lineas", lineasAprendService.getLineas());
            model.addAttribute("numLineasDisponibles", metodosGenerales.getNumLineasDisponibles());
            model.addAttribute("grados", gradoService.getNombreGrados());
            return "gestionEstacionesAprendizaje";
        }
        else{
            // Antes de manipular el array de los grados eliminamos la opción trivial '-'
            grados.remove("-");
            // Creo nueva línea y la guardo en BD incluyendo los nuevos grados seleccionados
            LineaAprend nuevaLinea = new LineaAprend();
            nuevaLinea.setTitulo(titulo);
            nuevaLinea.setDescripcion(descripcion);
            nuevaLinea.setCurso(curso);
            nuevaLinea.setNumeroLinea(numeroLinea);
            List<Grado> gradosLinea = new ArrayList<>();
            Grado gradoSeleccionado = null;
            for(int i=0;i<grados.size();i++){
                gradoSeleccionado = gradoService.getGradoPorNombreYCurso(grados.get(i), curso);
                if(gradoSeleccionado==null){
                    return "redirect:/gestionEstacionesAprendizaje";
                }
                else{
                    gradosLinea.add(gradoSeleccionado);
                }
            }
            nuevaLinea.setGrados(gradosLinea);
            lineasAprendService.guardarLinea(nuevaLinea);
            // Modifico cada grado seleccionado para incluir la nueva línea a su colección
            for(int i=0;i<gradosLinea.size();i++) {
                List<LineaAprend> lineasGrado = gradosLinea.get(i).getLineas();
                lineasGrado.add(nuevaLinea);
                gradosLinea.get(i).setLineas(lineasGrado);
                gradoService.guardarGrado(gradosLinea.get(i));
            }*/
            return "redirect:/gestionEstacionesAprendizaje";
        //}
    }




    public Map<Integer, String> getLineasDisponibles(){
        List<LineaAprend> lineas = lineasAprendService.getLineasPorCurso(metodosGenerales.getCursoAcademico());
        Map<Integer, String> mapa = new HashMap<>();
        for(int i=0;i<lineas.size();i++){
            mapa.put(lineas.get(i).getId(), i+" - "+lineas.get(i).getTitulo());
        }
        return mapa;
    }
}
