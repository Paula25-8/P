package com.itinerariosdeaprendizaje.practicum.controller;

import com.itinerariosdeaprendizaje.practicum.model.*;
import com.itinerariosdeaprendizaje.practicum.service.*;
import com.itinerariosdeaprendizaje.practicum.utils.MetodosGenerales;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
public class ControladorItinerarios {

    private MetodosGenerales metodosGenerales = new MetodosGenerales();
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private TutorURService tutorUrService;
    @Autowired
    private PracticumService practicumService;
    @Autowired
    private LineasAprendService lineasAprendService;
    @Autowired
    private EstacionAprendService estacionAprendService;
    @Autowired
    private ItinerarioService itinerarioService;
    @Autowired
    private EntregaEstacionService entregaEstacionService;
    @Autowired
    private SelloService selloService;
    @Autowired
    private DocumentosService documentosService;
    @Autowired
    private RelacionEstudianteTutorCentroService relacionEstudianteTutorCentroService;
    @Autowired
    private NotaTutorCentroService notaTutorCentroService;
    @Autowired
    private GradoService gradoService;

    @GetMapping("/itinerariosPropios") //Ruta a la que solo acceden ESTUDIANTES
    public String consultaItinerariosPropios(Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user!=null) {
            Integer numCambios=0;
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
            if(practicumActivo.getPrimerItinerario()!=null) {
                List<EstacionAprend> estacionesPrimerItinerario = this.getEstacionesDeItinerario(practicumActivo.getPrimerItinerario().getId());
                List<Integer> indicesPrimerItinerario =entregaEstacionService.getIndicesEstacionDeItinerario(practicumActivo.getPrimerItinerario().getId());
                Collections.sort(indicesPrimerItinerario);
                if (practicumActivo.getUltimoItinerario() != null) {
                    List<EstacionAprend> estacionesUltimoItinerario = this.getEstacionesDeItinerario(practicumActivo.getUltimoItinerario().getId());
                    List<Integer> indicesUltimoItinerario = entregaEstacionService.getIndicesEstacionDeItinerario(practicumActivo.getUltimoItinerario().getId());
                    Collections.sort(indicesUltimoItinerario);
                    // Miramos número de cambios y guardamos la posicion de ellos
                    List<Integer> posCambios = new ArrayList<Integer>();
                    for (int i=0;i<indicesPrimerItinerario.size();i++){
                        if(!indicesUltimoItinerario.contains(indicesPrimerItinerario.get(i))){
                            posCambios.add(i);
                            numCambios++;
                        }
                    }
                    switch (numCambios) {
                        case 1:
                            List<EstacionAprend> estacionesPrimerItinerarioNuevo = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(0)), 3, estacionesPrimerItinerario);
                            List<EstacionAprend> estacionesUltimoItinerarioNuevo = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(0)), 5, estacionesUltimoItinerario);
                            model.addAttribute("estacionesPrimerItinerario", estacionesPrimerItinerarioNuevo);
                            model.addAttribute("estacionesUltimoItinerario", estacionesUltimoItinerarioNuevo);
                        break;
                        case 2:
                            List<EstacionAprend> estacionesPrimerItinerarioNuevo21 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(0)), 1, estacionesPrimerItinerario);
                            List<EstacionAprend> estacionesPrimerItinerarioNuevo22 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(1)), 5, estacionesPrimerItinerarioNuevo21);
                            List<EstacionAprend> estacionesUltimoItinerarioNuevo21 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(0)), 1, estacionesUltimoItinerario);
                            List<EstacionAprend> estacionesUltimoItinerarioNuevo22 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(1)), 5, estacionesUltimoItinerarioNuevo21);
                            model.addAttribute("estacionesPrimerItinerario", estacionesPrimerItinerarioNuevo22);
                            model.addAttribute("estacionesUltimoItinerario", estacionesUltimoItinerarioNuevo22);
                        break;
                        case 3:
                            List<EstacionAprend> estacionesPrimerItinerarioNuevo31 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(0)), 1, estacionesPrimerItinerario);
                            List<EstacionAprend> estacionesPrimerItinerarioNuevo32 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(1)), 3, estacionesPrimerItinerarioNuevo31);
                            List<EstacionAprend> estacionesPrimeroItinerarioNuevo33 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(2)), 4, estacionesPrimerItinerarioNuevo32);
                            List<EstacionAprend> estacionesUltimoItinerarioNuevo31 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(0)), 1, estacionesUltimoItinerario);
                            List<EstacionAprend> estacionesUltimoItinerarioNuevo32 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(1)), 3, estacionesUltimoItinerarioNuevo31);
                            List<EstacionAprend> estacionesUltimoItinerarioNuevo33 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(2)), 4, estacionesUltimoItinerarioNuevo32);
                            model.addAttribute("estacionesPrimerItinerario", estacionesPrimeroItinerarioNuevo33);
                            model.addAttribute("estacionesUltimoItinerario", estacionesUltimoItinerarioNuevo33);
                        break;
                        case 4:
                            List<EstacionAprend> estacionesPrimerItinerarioNuevo41 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(0)), 1, estacionesPrimerItinerario);
                            List<EstacionAprend> estacionesPrimerItinerarioNuevo42 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(1)), 2, estacionesPrimerItinerarioNuevo41);
                            List<EstacionAprend> estacionesPrimeroItinerarioNuevo43 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(2)), 4, estacionesPrimerItinerarioNuevo42);
                            List<EstacionAprend> estacionesPrimeroItinerarioNuevo44 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(3)), 5, estacionesPrimeroItinerarioNuevo43);
                            List<EstacionAprend> estacionesUltimoItinerarioNuevo41 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(0)), 1, estacionesUltimoItinerario);
                            List<EstacionAprend> estacionesUltimoItinerarioNuevo42 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(1)), 2, estacionesUltimoItinerarioNuevo41);
                            List<EstacionAprend> estacionesUltimoItinerarioNuevo43 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(2)), 4, estacionesUltimoItinerarioNuevo42);
                            List<EstacionAprend> estacionesUltimoItinerarioNuevo44 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(3)), 5, estacionesUltimoItinerarioNuevo43);
                            model.addAttribute("estacionesPrimerItinerario", estacionesPrimeroItinerarioNuevo44);
                            model.addAttribute("estacionesUltimoItinerario", estacionesUltimoItinerarioNuevo44);
                        break;
                    }
                }
                else{
                    model.addAttribute("estacionesPrimerItinerario", estacionesPrimerItinerario);
                }
            }
            model.addAttribute("numCambios", numCambios);
            model.addAttribute("primerItinerario", practicumActivo.getPrimerItinerario());
            model.addAttribute("ultimoItinerario", practicumActivo.getUltimoItinerario());
            return "itinerariosPropios";
        }
        return "redirect:/";
    }

    @GetMapping("/itinerariosPropios/crearItinerario") //Ruta a la que solo acceden ESTUDIANTES
    public String mostrarPaginaCreacionItinerario(Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user !=null) {
            String curso = metodosGenerales.getCursoAcademico();
            Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
            model.addAttribute("lineas", lineasAprendService.getLineasPorCursoYGrado(curso, estudiante.obtenerGradoPorCurso(curso)));
            model.addAttribute("estaciones", estacionAprendService.getEstacionesPorCurso(curso));
            return "crearItinerario";
        }
        return "redirect:/";
    }
    @PostMapping("/itinerariosPropios/crearItinerario") //Ruta a la que solo acceden ESTUDIANTES
    public String creacionNuevoItinerario(@RequestParam(name="estacionLinea1") String estacionLinea1, @RequestParam(name="estacionLinea2") String estacionLinea2, @RequestParam(name="estacionLinea3") String estacionLinea3, @RequestParam(name="estacionLinea4") String estacionLinea4, Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user !=null) {
            if(estacionLinea1.equals("null") || estacionLinea2.equals("null") || estacionLinea3.equals("null") || estacionLinea4.equals("null")) {
                String mensajeError = "Debe elegir una estación en la línea.";
                if (estacionLinea1.equals("null")) {
                    model.addAttribute("errorL1", mensajeError);
                }else{
                    model.addAttribute("valorL1", estacionLinea1);
                }
                if (estacionLinea2.equals("null")) {
                    model.addAttribute("errorL2", mensajeError);
                }else{
                    model.addAttribute("valorL2", estacionLinea2);
                }
                if (estacionLinea3.equals("null")) {
                    model.addAttribute("errorL3", mensajeError);
                }else{
                    model.addAttribute("valorL3", estacionLinea3);
                }
                if (estacionLinea4.equals("null")) {
                    model.addAttribute("errorL4", mensajeError);
                }else{
                    model.addAttribute("valorL4", estacionLinea4);
                }
                String curso = metodosGenerales.getCursoAcademico();
                Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
                model.addAttribute("lineas", lineasAprendService.getLineasPorCursoYGrado(curso, estudiante.obtenerGradoPorCurso(curso)));
                model.addAttribute("estaciones", estacionAprendService.getEstacionesPorCurso(curso));
                return "creacionItinerario";
            }
            Itinerario nuevoItinerario = new Itinerario();
            nuevoItinerario.setFechaCreacion(new Date());
            nuevoItinerario=itinerarioService.guardarItinerario(nuevoItinerario);
            Practicum practicumActivo = practicumService.getPracticumActivoEstudiante( estudianteService.getEstudiantePorCodnum(user.getId()), metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
            if(practicumActivo!=null) {
                if (practicumActivo.getPrimerItinerario() == null) {
                    practicumActivo.setPrimerItinerario(nuevoItinerario);
                } else {
                    practicumActivo.setUltimoItinerario(nuevoItinerario);
                }
                practicumActivo = practicumService.guardarPracticum(practicumActivo);
                ArrayList<Integer> seleccionEstaciones= new ArrayList<>();
                seleccionEstaciones.add(Integer.parseInt(estacionLinea1));
                seleccionEstaciones.add(Integer.parseInt(estacionLinea2));
                seleccionEstaciones.add(Integer.parseInt(estacionLinea3));
                seleccionEstaciones.add(Integer.parseInt(estacionLinea4));
                for(int i=0;i<seleccionEstaciones.size()+1;i++) {
                    EntregaEstacion entregaEstacion = new EntregaEstacion();
                    EntregaEstacion.IdEntregaEstacion id = new EntregaEstacion.IdEntregaEstacion();
                    id.setId_Itinerario(nuevoItinerario.getId());
                    if(seleccionEstaciones.size()==i){
                        id.setId_Estacion(estacionAprendService.getEstacionPorTitulo("Unidad de programación").getId());
                    }else{
                        id.setId_Estacion(estacionAprendService.getEstacionPorId(seleccionEstaciones.get(i)).getId());
                    }
                    entregaEstacion.setId(id);
                    entregaEstacion.setCurso(metodosGenerales.getCursoAcademico());
                    entregaEstacion.setItinerario(nuevoItinerario);
                    if(seleccionEstaciones.size()==i){
                         entregaEstacion.setEstacion(estacionAprendService.getEstacionPorTitulo("Unidad de Programación"));
                    }else{
                        entregaEstacion.setEstacion(estacionAprendService.getEstacionPorId(seleccionEstaciones.get(i)));
                    }
                    entregaEstacionService.guardarEntrega(entregaEstacion);
                }
                return "redirect:/itinerariosPropios";
            }
            return "redirect:/";
        }
        else{
            return "redirect:/";
        }
    }

    @GetMapping("/itinerariosPropios/consultarItinerario/{id}")
    public String mostrarPaginaConsultaItinerario(@PathVariable Integer id, Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user !=null) {
            Practicum pract = practicumService.getPracticumActivoEstudiante(estudianteService.getEstudiantePorCodnum(user.getId()), metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
            // Controlamos que no nos puedan manipular la URL cambiando el identificador del itinerario a consultar
            if(!pract.tieneItinerario(id)){
                return "redirect:/itinerariosPropios";
            }
            Itinerario itinerario = itinerarioService.getItinerarioPorCod(id);
            model.addAttribute("itinerario", itinerario);
            // En caso de que haya dos itinerarios creados miramos la diferencia de estaciones que hay
            Integer numCambios=0;
            List<Integer> posCambios = new ArrayList<Integer>();
            if(pract.getUltimoItinerario()!=null){
                List<Integer> indicesPrimerIter = entregaEstacionService.getIndicesEstacionDeItinerario(pract.getPrimerItinerario().getId());
                Collections.sort(indicesPrimerIter);
                List<Integer> indicesUltimoIter = entregaEstacionService.getIndicesEstacionDeItinerario(pract.getUltimoItinerario().getId());
                Collections.sort(indicesUltimoIter);
                for(int i=0;i<indicesPrimerIter.size();i++){
                    if(!indicesUltimoIter.contains(indicesPrimerIter.get(i))){
                        posCambios.add(i);
                        numCambios++;
                    }
                }
            }
            if(pract.getPrimerItinerario().getId()==id){
                model.addAttribute("caracterItinerario", "inicial");
                if(pract.getUltimoItinerario()==null){
                    List<EstacionAprend> estaciones = this.getEstacionesDeItinerario(id);
                    model.addAttribute("estaciones", estaciones);
                    model.addAttribute("entregas", this.obtenerEntregas(itinerario.getId(), estaciones));
                    model.addAttribute("estado", "Activo");
                    model.addAttribute("hayUltimoItinerario", false);
                }
                else {
                    List<EstacionAprend> estaciones = this.getEstacionesDeItinerario(id);
                    switch (numCambios) {
                        case 1:
                            estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 5, estaciones);
                            break;
                        case 2:
                            estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                            estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 5, estaciones);
                            break;
                        case 3:
                            estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                            estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 3, estaciones);
                            estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(2)), 4, estaciones);
                            break;
                        case 4:
                            estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                            estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 2, estaciones);
                            estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(2)), 4, estaciones);
                            estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(3)), 5, estaciones);
                            break;
                    }
                    model.addAttribute("estaciones", estaciones);
                    model.addAttribute("numCambios", numCambios);
                    model.addAttribute("estado", "Inactivo");
                    model.addAttribute("hayUltimoItinerario", true);
                }
            }
            else{
                List<EstacionAprend> estaciones = this.getEstacionesDeItinerario(id);
                switch (numCambios) {
                    case 1:
                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 5, estaciones);
                    break;
                    case 2:
                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 5, estaciones);
                    break;
                    case 3:
                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 3, estaciones);
                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(2)), 4, estaciones);
                    break;
                    case 4:
                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 2, estaciones);
                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(2)), 4, estaciones);
                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(3)), 5, estaciones);
                    break;
                }
                model.addAttribute("caracterItinerario", "final");
                model.addAttribute("estaciones", estaciones);
                model.addAttribute("entregas", this.obtenerEntregas(itinerario.getId(), estaciones));
                model.addAttribute("numCambios", numCambios);
                model.addAttribute("estado", "Activo");
                model.addAttribute("hayUltimoItinerario", true);
            }
            List<Caracteristica> caracts = selloService.getSelloPorCurso(metodosGenerales.getCursoAcademico()).getCaracteristicas();
            model.addAttribute("caracteristicas", caracts);
            return "consultarItinerario";
        }
        return "redirect:/";
    }

    @PostMapping("/itinerariosPropios/consultarItinerario/{idEstacion}") //Ruta a la que solo acceden ESTUDIANTES
    public String nuevaActividadEstacion(@PathVariable Integer idEstacion, @RequestParam("actividadEstacion") MultipartFile actividadEstacion, @RequestParam("numItinerario") Integer numItinerario, Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if (user != null) {
            EntregaEstacion.IdEntregaEstacion id = new EntregaEstacion.IdEntregaEstacion();
            id.setId_Itinerario(numItinerario);
            id.setId_Estacion(idEstacion);
            EntregaEstacion entrega = entregaEstacionService.getEntregasPorId(id);
            entrega.setNombreDocu(user.getId()+idEstacion+actividadEstacion.getOriginalFilename().replace(" ", "_").trim());
            entrega.setFechaEntrega(new Date());
            entregaEstacionService.guardarEntrega(entrega);
            documentosService.guardarActividad(actividadEstacion, user.getId()+idEstacion);
            return "redirect:/itinerariosPropios/consultarItinerario/" + numItinerario;
        } else {
            return "redirect:/";
        }

    }

    @GetMapping("/itinerariosPropios/consultarActividad/{idEntrega}")
    public String mostrarPaginaConsultarActividad(@PathVariable String idEntrega, Model model){
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession sesion = attr.getRequest().getSession(true);
            Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
            if(user !=null) {
                Integer pos = idEntrega.indexOf("-");
                if (pos == -1) {
                    return "redirect:/itinerariosPropios";
                }
                Integer idItinerario = Integer.parseInt(idEntrega.substring(0, pos));
                Integer idEstacion = Integer.parseInt(idEntrega.substring(pos + 1, idEntrega.length()));
                Practicum pract = practicumService.getPracticumActivoEstudiante(estudianteService.getEstudiantePorCodnum(user.getId()), metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                // Controlamos que no nos puedan manipular la URL cambiando el identificador del itinerario a consultar
                if (!pract.tieneItinerario(idItinerario) || !entregaEstacionService.getIndicesEstacionDeItinerario(idItinerario).contains(idEstacion)) {
                    return "redirect:/itinerariosPropios";
                }
                EntregaEstacion.IdEntregaEstacion id = new EntregaEstacion.IdEntregaEstacion();
                id.setId_Itinerario(idItinerario);
                id.setId_Estacion(idEstacion);
                EntregaEstacion entrega = entregaEstacionService.getEntregasPorId(id);
                model.addAttribute("feedbackTutor", entrega.getFeedbackTutorUR());
                model.addAttribute("fechaFeedback", entrega.getFechaEntrega());
                model.addAttribute("fechaEntrega", entrega.getFechaEntrega());
                model.addAttribute("rutaDocu", "../../files/actividadesEstacion/" + entrega.getNombreDocu());
                List<Caracteristica> caracts = selloService.getSelloPorCurso(metodosGenerales.getCursoAcademico()).getCaracteristicas();
                model.addAttribute("caracteristicas", caracts);
                model.addAttribute("estacion", estacionAprendService.getEstacionPorId(idEstacion));
                model.addAttribute("idEstudiante", estudianteService.getEstudiantePorCodnum(user.getId()).getId());
                model.addAttribute("itinerario", itinerarioService.getItinerarioPorCod(idItinerario));
                return "consultarActividadEntrega";
            }
            else{
                return "redirect:/";
            }
        }
        catch(NumberFormatException exception){
            return "redirect:/itinerariosPropios";
        }
    }

    @GetMapping("/itinerariosPropios/eliminarActividad/{idEntrega}")
    public String eliminarPaginaConsultarActividad(@PathVariable String idEntrega, HttpServletRequest request, Model model){
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession sesion = attr.getRequest().getSession(true);
            Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
            if(user !=null) {
                Integer pos = idEntrega.indexOf("-");
                if (pos == -1) {
                    return "redirect:/itinerariosPropios";
                }
                Integer idItinerario = Integer.parseInt(idEntrega.substring(0, pos));
                Integer idEstacion = Integer.parseInt(idEntrega.substring(pos + 1, idEntrega.length()));
                Practicum pract = practicumService.getPracticumActivoEstudiante(estudianteService.getEstudiantePorCodnum(user.getId()), metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                // Controlamos que no nos puedan manipular la URL cambiando el identificador del itinerario a consultar
                if (!pract.tieneItinerario(idItinerario) || !entregaEstacionService.getIndicesEstacionDeItinerario(idItinerario).contains(idEstacion)) {
                    return "redirect:/itinerariosPropios";
                }
                EntregaEstacion.IdEntregaEstacion id = new EntregaEstacion.IdEntregaEstacion();
                id.setId_Itinerario(idItinerario);
                id.setId_Estacion(idEstacion);
                EntregaEstacion entrega = entregaEstacionService.getEntregasPorId(id);
                documentosService.eliminarActividad(entrega.getNombreDocu());
                entrega.setNombreDocu(null);
                entrega.setFechaEntrega(null);
                entregaEstacionService.guardarEntrega(entrega);
                return "redirect:/itinerariosPropios/consultarItinerario/"+idItinerario;
            }
            else{
                return "redirect:/";
            }
        }
        catch(NumberFormatException exception){
            return "redirect:/itinerariosPropios";
        }
        catch (IOException exception){
            return "redirect:/itinerariosPropios";
        }
    }

    @GetMapping("/itinerariosPropios/modificarItinerario/{id}")
    public String mostrarPaginaModificarItinerario(@PathVariable Integer id, Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user !=null) {
            String curso = metodosGenerales.getCursoAcademico();
            Estudiante estudiante = estudianteService.getEstudiantePorCodnum(user.getId());
            Practicum pract = practicumService.getPracticumActivoEstudiante(estudiante, curso, metodosGenerales.getConvocatoriaPorFechas());
            // Controlamos que no nos puedan manipular la URL cambiando el identificador del itinerario a consultar
            if(!pract.tieneItinerario(id)){
                return "redirect:/itinerariosPropios";
            }
            List<EstacionAprend> estacionesPrevias = this.getEstacionesDeItinerario(id);
            List<EstacionAprend> estaciones =  estacionAprendService.getEstacionesPorCurso(curso);
            for(int i=0;i<estacionesPrevias.size();i++){
                estaciones.remove(estacionesPrevias.get(i));
            }
            model.addAttribute("estacionesPrevias", estacionesPrevias);
            model.addAttribute("lineas", lineasAprendService.getLineasPorCursoYGrado(curso, estudiante.obtenerGradoPorCurso(curso)));
            model.addAttribute("estaciones", estaciones);
            model.addAttribute("idItinerario", id);
            return "modificarItinerario";
        }
        return "redirect:/";
    }

    @PostMapping("/itinerariosPropios/modificarItinerario/{id}") //Ruta a la que solo acceden ESTUDIANTES
    public String modificacionItinerario(@PathVariable Integer id, @RequestParam(name="estacionLinea1") String estacionLinea1, @RequestParam(name="estacionLinea2") String estacionLinea2, @RequestParam(name="estacionLinea3") String estacionLinea3, @RequestParam(name="estacionLinea4") String estacionLinea4, Model model) {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession sesion = attr.getRequest().getSession(true);
            Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
            if (user != null) {
                Practicum practicumActivo = practicumService.getPracticumActivoEstudiante(estudianteService.getEstudiantePorCodnum(user.getId()), metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                if (practicumActivo != null) {
                    if (!practicumActivo.tieneItinerario(id)) {
                        return "redirect:/itinerariosPropios";
                    }
                    ArrayList<Integer> seleccionEstaciones = new ArrayList<>();
                    seleccionEstaciones.add(Integer.parseInt(estacionLinea1));
                    seleccionEstaciones.add(Integer.parseInt(estacionLinea2));
                    seleccionEstaciones.add(Integer.parseInt(estacionLinea3));
                    seleccionEstaciones.add(Integer.parseInt(estacionLinea4));
                    if (practicumActivo.caracterItinerario(id).equals("Inicial")) {
                        // Previo a crear el nuevo itinerario, borramos los documentos de las entregas del viejo (primer itinerario)
                        List<Integer> indicesEstacionesViejas = entregaEstacionService.getIndicesEstacionDeItinerario(id);
                        for(int i=0;i<indicesEstacionesViejas.size();i++){
                            EntregaEstacion.IdEntregaEstacion idEntrega = new EntregaEstacion.IdEntregaEstacion();
                            idEntrega.setId_Itinerario(id);
                            idEntrega.setId_Estacion(indicesEstacionesViejas.get(i));
                            EntregaEstacion entrega = entregaEstacionService.getEntregasPorId(idEntrega);
                            if(entrega.getNombreDocu()!=null && !entrega.getNombreDocu().isEmpty()){
                                documentosService.eliminarActividad(entrega.getNombreDocu());
                                entrega.setNombreDocu("");
                                entrega.setFechaEntrega(null);
                                entregaEstacionService.guardarEntrega(entrega);
                            }else{
                                System.out.println("No hay documento entregado");
                            }
                        }
                        // Creamos nuevo itinerario
                        Itinerario nuevoItinerario = new Itinerario();
                        nuevoItinerario.setFechaCreacion(new Date());
                        nuevoItinerario = itinerarioService.guardarItinerario(nuevoItinerario);
                        practicumActivo.setUltimoItinerario(nuevoItinerario);
                        practicumActivo = practicumService.guardarPracticum(practicumActivo);
                        for (int i = 0; i < seleccionEstaciones.size() + 1; i++) {
                            EntregaEstacion entregaEstacion = new EntregaEstacion();
                            EntregaEstacion.IdEntregaEstacion idEntrega = new EntregaEstacion.IdEntregaEstacion();
                            idEntrega.setId_Itinerario(nuevoItinerario.getId());
                            if (seleccionEstaciones.size() == i) {
                                idEntrega.setId_Estacion(estacionAprendService.getEstacionPorTitulo("Unidad de programación").getId());
                            } else {
                                idEntrega.setId_Estacion(estacionAprendService.getEstacionPorId(seleccionEstaciones.get(i)).getId());
                            }
                            entregaEstacion.setId(idEntrega);
                            entregaEstacion.setCurso(metodosGenerales.getCursoAcademico());
                            entregaEstacion.setItinerario(nuevoItinerario);
                            if (seleccionEstaciones.size() == i) {
                                entregaEstacion.setEstacion(estacionAprendService.getEstacionPorTitulo("Unidad de Programación"));
                            } else {
                                entregaEstacion.setEstacion(estacionAprendService.getEstacionPorId(seleccionEstaciones.get(i)));
                            }
                            entregaEstacionService.guardarEntrega(entregaEstacion);
                        }
                        return "redirect:/itinerariosPropios";
                    }
                    // Caso donde haya ya 2 itinerarios creados, hay que modificar ultimo itinerario
                    else {
                        Itinerario itinerario = itinerarioService.getItinerarioPorCod(id);
                        itinerario.setFechaCreacion(new Date());
                        itinerarioService.guardarItinerario(itinerario);
                        List<EntregaEstacion> entregasViejas = entregaEstacionService.getEntregasDeItinerario(id);
                        //Hay que incluir la estacion de Unidad de Programación (UP)
                        seleccionEstaciones.add(5);
                        // Eliminamos entregas de estaciones no seleccionadas para el nuevo itinerario
                        for (int i = 0; i < entregasViejas.size(); i++) {
                            if (!seleccionEstaciones.contains(entregasViejas.get(i).getId().getId_Estacion())) {
                                entregaEstacionService.removeEntrega(entregasViejas.get(i));
                                if(entregasViejas.get(i).getNombreDocu()!=null) {
                                    documentosService.eliminarActividad(entregasViejas.get(i).getNombreDocu());
                                }
                            }
                        }
                        // Incluimos entregas de estaciones nuevas
                        List<Integer> indicesBDEntrega = entregaEstacionService.getIndicesEstacionDeItinerario(id);
                        for (int i = 0; i < seleccionEstaciones.size(); i++) {
                            if (!indicesBDEntrega.contains(seleccionEstaciones.get(i))) {
                                EntregaEstacion entregaNueva = new EntregaEstacion();
                                EntregaEstacion.IdEntregaEstacion idEntregaNueva = new EntregaEstacion.IdEntregaEstacion();
                                idEntregaNueva.setId_Itinerario(id);
                                idEntregaNueva.setId_Estacion(seleccionEstaciones.get(i));
                                entregaNueva.setId(idEntregaNueva);
                                entregaNueva.setCurso(metodosGenerales.getCursoAcademico());
                                entregaNueva.setItinerario(itinerario);
                                entregaNueva.setEstacion(estacionAprendService.getEstacionPorId(seleccionEstaciones.get(i)));
                                entregaEstacionService.guardarEntrega(entregaNueva);
                            }
                        }
                        return "redirect:/itinerariosPropios";
                    }
                }
                return "redirect:/";
            } else {
                return "redirect:/";
            }
        }
        catch(IOException exception){
            return "redirect:/";
        }
    }

    @GetMapping("/itinerariosAlumnado") //Ruta a la que solo acceden TUTOR_UR y COORDINADOR
    public String consultaItinerariosAlumnado(Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user!=null) {
            List<Grado> grados = new ArrayList<>();
            List<Estudiante> listadoEstudiantes = null;
            List<EstudianteTabla> estudiantesAMostrar = new ArrayList<>();
            if(user.tieneRol("ROL_TUTOR_UR")){
                TutorUR tutorUR = tutorUrService.getTutorURPorCodnum(user.getId());
                listadoEstudiantes = tutorUR.getTutorizados();
            }
            if(user.tieneRol("ROL_COORDINADOR")){
                listadoEstudiantes = estudianteService.getEstudiantes();
            }
            for(int i=0;i<listadoEstudiantes.size();i++){
                Practicum practicum = practicumService.getPracticumActivoEstudiante(listadoEstudiantes.get(i), metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                if(practicum!=null){
                    Grado grado = listadoEstudiantes.get(i).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                    if(!grados.contains(grado)){
                        grados.add(grado);
                    }
                    estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(),listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicum, practicum.numItinerarios(), grado, practicum.getItinerarioActivo(), false));
                }
            }
            model.addAttribute("grados", grados);
            model.addAttribute("gradoSeleccionado", 0);
            model.addAttribute("estudiantes",estudiantesAMostrar);
            return "itinerariosAlumnado";
        }
        return "redirect:/";
    }

    @PostMapping("/itinerariosAlumnado") //Ruta a la que solo acceden TUTOR_UR y COORDINADOR
    public String consultaItinerariosAlumnadoPorFiltro(@RequestParam("nombreAlumn") String nombreAlumn, @RequestParam("grado") Integer grado, Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user!=null) {
            List<Grado> grados = new ArrayList<>();
            List<Estudiante> listadoEstudiantes = null;
            List<EstudianteTabla> estudiantesAMostrar = new ArrayList<>();
            if(user.tieneRol("ROL_TUTOR_UR")) {
                TutorUR tutorUR = tutorUrService.getTutorURPorCodnum(user.getId());
                listadoEstudiantes = tutorUR.getTutorizados();
            }
            if(user.tieneRol("ROL_COORDINADOR")){
                listadoEstudiantes = estudianteService.getEstudiantes();
            }
            for(int i=0;i<listadoEstudiantes.size();i++){
                System.out.println(listadoEstudiantes.get(i).getId()+", "+listadoEstudiantes.get(i).getNombreCompleto());
                Practicum practicum = practicumService.getPracticumActivoEstudiante(listadoEstudiantes.get(i), metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());

                // ESTO SE DEBERÍA BORRAR EN CUANTO PREPAREMOS LA PARTE DE GESTION
                if(practicum==null){
                    return "redirect:/";
                }else

                {
                    Grado gradoLista = listadoEstudiantes.get(i).obtenerGradoPorCurso(metodosGenerales.getCursoAcademico());
                    if (!grados.contains(gradoLista)) {
                        grados.add(gradoLista);
                    }
                    // Caso donde filtro tenga todos los valores vacios
                    if(grado==0 && nombreAlumn==""){
                        return "redirect:/itinerariosAlumnado";
                    }
                    // Caso donde el filtro solo se complete con Grado
                    if (gradoLista.equals(gradoService.getGradoPorId(grado)) && nombreAlumn=="") {
                        estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicum, practicum.numItinerarios(), gradoLista, practicum.getItinerarioActivo(), false));
                    }
                    else{
                        // Caso donde el filtro solo se complete con Nombre
                        if (grado==0 && listadoEstudiantes.get(i).getNombreCompletoOrdenado().contains(nombreAlumn)) {
                            estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicum, practicum.numItinerarios(), gradoLista, practicum.getItinerarioActivo(), false));
                        }
                        if (gradoLista.equals(gradoService.getGradoPorId(grado)) && listadoEstudiantes.get(i).getNombreCompletoOrdenado().contains(nombreAlumn)) {
                            estudiantesAMostrar.add(new EstudianteTabla(listadoEstudiantes.get(i).getId(), listadoEstudiantes.get(i).getNombreCompletoOrdenado(), practicum, practicum.numItinerarios(), gradoLista, practicum.getItinerarioActivo(), false));
                        }
                    }
                }
            }
            model.addAttribute("grados", grados);
            model.addAttribute("gradoSeleccionado", grado);
            model.addAttribute("nombreSeleccionado", nombreAlumn);
            model.addAttribute("estudiantes",estudiantesAMostrar);
            return "itinerariosAlumnado";
        }
        return "redirect:/";
    }

    @GetMapping("/itinerariosAlumnado/consultarPracticum/{id}") //Ruta a la que solo acceden TUTOR_UR y COORDINADOR
    public String consultaItinerariosAlumnoConcreto(@PathVariable Integer id, Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user!=null) {
            List<Estudiante> listadoEstudiantes = null;
            List<EstudianteTabla> estudiantes = new ArrayList<>();
            Integer numCambios = 0;
            Estudiante estudiante = estudianteService.getEstudiantePorCodnum(id);
            if (user.tieneRol("ROL_TUTOR_UR")) {
                listadoEstudiantes = tutorUrService.getTutorURPorCodnum(user.getId()).getTutorizados();
            }
            if (user.tieneRol("ROL_COORDINADOR")) {
                listadoEstudiantes = estudianteService.getEstudiantes();
            }
            if (estudiante != null && listadoEstudiantes.contains(estudiante)) {
                Practicum practicumActivo = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                if (practicumActivo.getPrimerItinerario() != null) {
                    List<EstacionAprend> estacionesPrimerItinerario = this.getEstacionesDeItinerario(practicumActivo.getPrimerItinerario().getId());
                    List<Integer> indicesPrimerItinerario = entregaEstacionService.getIndicesEstacionDeItinerario(practicumActivo.getPrimerItinerario().getId());
                    Collections.sort(indicesPrimerItinerario);
                    if (practicumActivo.getUltimoItinerario() != null) {
                        List<EstacionAprend> estacionesUltimoItinerario = this.getEstacionesDeItinerario(practicumActivo.getUltimoItinerario().getId());
                        List<Integer> indicesUltimoItinerario = entregaEstacionService.getIndicesEstacionDeItinerario(practicumActivo.getUltimoItinerario().getId());
                        Collections.sort(indicesUltimoItinerario);
                        // Miramos número de cambios y guardamos la posicion de ellos
                        List<Integer> posCambios = new ArrayList<Integer>();
                        for (int i = 0; i < indicesPrimerItinerario.size(); i++) {
                            if (!indicesUltimoItinerario.contains(indicesPrimerItinerario.get(i))) {
                                posCambios.add(i);
                                numCambios++;
                            }
                        }
                        switch (numCambios) {
                            case 1:
                                List<EstacionAprend> estacionesPrimerItinerarioNuevo = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(0)), 3, estacionesPrimerItinerario);
                                List<EstacionAprend> estacionesUltimoItinerarioNuevo = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(0)), 5, estacionesUltimoItinerario);
                                model.addAttribute("estacionesPrimerItinerario", estacionesPrimerItinerarioNuevo);
                                model.addAttribute("estacionesUltimoItinerario", estacionesUltimoItinerarioNuevo);
                                break;
                            case 2:
                                List<EstacionAprend> estacionesPrimerItinerarioNuevo21 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(0)), 1, estacionesPrimerItinerario);
                                List<EstacionAprend> estacionesPrimerItinerarioNuevo22 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(1)), 5, estacionesPrimerItinerarioNuevo21);
                                List<EstacionAprend> estacionesUltimoItinerarioNuevo21 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(0)), 1, estacionesUltimoItinerario);
                                List<EstacionAprend> estacionesUltimoItinerarioNuevo22 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(1)), 5, estacionesUltimoItinerarioNuevo21);
                                model.addAttribute("estacionesPrimerItinerario", estacionesPrimerItinerarioNuevo22);
                                model.addAttribute("estacionesUltimoItinerario", estacionesUltimoItinerarioNuevo22);
                                break;
                            case 3:
                                List<EstacionAprend> estacionesPrimerItinerarioNuevo31 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(0)), 1, estacionesPrimerItinerario);
                                List<EstacionAprend> estacionesPrimerItinerarioNuevo32 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(1)), 3, estacionesPrimerItinerarioNuevo31);
                                List<EstacionAprend> estacionesPrimeroItinerarioNuevo33 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(2)), 4, estacionesPrimerItinerarioNuevo32);
                                List<EstacionAprend> estacionesUltimoItinerarioNuevo31 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(0)), 1, estacionesUltimoItinerario);
                                List<EstacionAprend> estacionesUltimoItinerarioNuevo32 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(1)), 3, estacionesUltimoItinerarioNuevo31);
                                List<EstacionAprend> estacionesUltimoItinerarioNuevo33 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(2)), 4, estacionesUltimoItinerarioNuevo32);
                                model.addAttribute("estacionesPrimerItinerario", estacionesPrimeroItinerarioNuevo33);
                                model.addAttribute("estacionesUltimoItinerario", estacionesUltimoItinerarioNuevo33);
                                break;
                            case 4:
                                List<EstacionAprend> estacionesPrimerItinerarioNuevo41 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(0)), 1, estacionesPrimerItinerario);
                                List<EstacionAprend> estacionesPrimerItinerarioNuevo42 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(1)), 2, estacionesPrimerItinerarioNuevo41);
                                List<EstacionAprend> estacionesPrimeroItinerarioNuevo43 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(2)), 4, estacionesPrimerItinerarioNuevo42);
                                List<EstacionAprend> estacionesPrimeroItinerarioNuevo44 = this.posicionarEstacion(estacionesPrimerItinerario.get(posCambios.get(3)), 5, estacionesPrimeroItinerarioNuevo43);
                                List<EstacionAprend> estacionesUltimoItinerarioNuevo41 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(0)), 1, estacionesUltimoItinerario);
                                List<EstacionAprend> estacionesUltimoItinerarioNuevo42 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(1)), 2, estacionesUltimoItinerarioNuevo41);
                                List<EstacionAprend> estacionesUltimoItinerarioNuevo43 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(2)), 4, estacionesUltimoItinerarioNuevo42);
                                List<EstacionAprend> estacionesUltimoItinerarioNuevo44 = this.posicionarEstacion(estacionesUltimoItinerario.get(posCambios.get(3)), 5, estacionesUltimoItinerarioNuevo43);
                                model.addAttribute("estacionesPrimerItinerario", estacionesPrimeroItinerarioNuevo44);
                                model.addAttribute("estacionesUltimoItinerario", estacionesUltimoItinerarioNuevo44);
                                break;
                        }
                    } else {
                        model.addAttribute("estacionesPrimerItinerario", estacionesPrimerItinerario);
                    }
                    model.addAttribute("estudiante", estudiante);
                    model.addAttribute("numCambios", numCambios);
                    model.addAttribute("primerItinerario", practicumActivo.getPrimerItinerario());
                    model.addAttribute("ultimoItinerario", practicumActivo.getUltimoItinerario());
                    return "itinerariosPropios";
                }
            }
        }
        return "redirect:/";
    }

    @GetMapping("/itinerariosAlumnado/consultarItinerario/{id}")
    public String mostrarPaginaConsultaItinerarioAlumno(@PathVariable String id, Model model){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if(user !=null) {
            Integer pos = id.indexOf("-");
            if (pos != -1) {
                Integer idItinerario = Integer.parseInt(id.substring(0, pos));
                Integer idEstudiante = Integer.parseInt(id.substring(pos + 1, id.length()));
                Estudiante estudiante = estudianteService.getEstudiantePorCodnum(idEstudiante);
                TutorUR tutorUR = tutorUrService.getTutorURPorCodnum(user.getId());
                if ((estudiante!=null && user.tieneRol("ROL_TUTOR_UR") && tutorUR!=null && tutorUR.tieneEstudiante(estudiante)) || (estudiante!=null && user.tieneRol("ROL_COORDINADOR"))){
                    Practicum pract = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                    // Controlamos que no nos puedan manipular la URL cambiando el identificador del itinerario a consultar
                    if (pract.tieneItinerario(idItinerario)) {
                        Itinerario itinerario = itinerarioService.getItinerarioPorCod(idItinerario);
                        model.addAttribute("itinerario", itinerario);
                        model.addAttribute("estudiante", estudiante);
                        // En caso de que haya dos itinerarios creados miramos la diferencia de estaciones que hay
                        Integer numCambios = 0;
                        List<Integer> posCambios = new ArrayList<Integer>();
                        if (pract.getUltimoItinerario() != null) {
                            List<Integer> indicesPrimerIter = entregaEstacionService.getIndicesEstacionDeItinerario(pract.getPrimerItinerario().getId());
                            Collections.sort(indicesPrimerIter);
                            List<Integer> indicesUltimoIter = entregaEstacionService.getIndicesEstacionDeItinerario(pract.getUltimoItinerario().getId());
                            Collections.sort(indicesUltimoIter);
                            for (int i = 0; i < indicesPrimerIter.size(); i++) {
                                if (!indicesUltimoIter.contains(indicesPrimerIter.get(i))) {
                                    posCambios.add(i);
                                    numCambios++;
                                }
                            }
                        }
                        if (pract.getPrimerItinerario().getId() == idItinerario) {
                            model.addAttribute("caracterItinerario", "inicial");
                            if (pract.getUltimoItinerario() == null) {
                                List<EstacionAprend> estaciones = this.getEstacionesDeItinerario(idItinerario);
                                model.addAttribute("estaciones", estaciones);
                                model.addAttribute("entregas", this.obtenerEntregas(itinerario.getId(), estaciones));
                                model.addAttribute("estado", "Activo");
                                model.addAttribute("hayUltimoItinerario", false);
                            } else {
                                List<EstacionAprend> estaciones = this.getEstacionesDeItinerario(idItinerario);
                                switch (numCambios) {
                                    case 1:
                                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 5, estaciones);
                                        break;
                                    case 2:
                                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 5, estaciones);
                                        break;
                                    case 3:
                                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 3, estaciones);
                                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(2)), 4, estaciones);
                                        break;
                                    case 4:
                                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 2, estaciones);
                                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(2)), 4, estaciones);
                                        estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(3)), 5, estaciones);
                                        break;
                                }
                                model.addAttribute("estaciones", estaciones);
                                model.addAttribute("numCambios", numCambios);
                                model.addAttribute("estado", "Inactivo");
                                model.addAttribute("hayUltimoItinerario", true);
                            }
                        } else {
                            List<EstacionAprend> estaciones = this.getEstacionesDeItinerario(idItinerario);
                            switch (numCambios) {
                                case 1:
                                    estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 5, estaciones);
                                    break;
                                case 2:
                                    estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                                    estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 5, estaciones);
                                    break;
                                case 3:
                                    estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                                    estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 3, estaciones);
                                    estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(2)), 4, estaciones);
                                    break;
                                case 4:
                                    estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(0)), 1, estaciones);
                                    estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(1)), 2, estaciones);
                                    estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(2)), 4, estaciones);
                                    estaciones = this.posicionarEstacion(estaciones.get(posCambios.get(3)), 5, estaciones);
                                    break;
                            }
                            model.addAttribute("caracterItinerario", "final");
                            model.addAttribute("estaciones", estaciones);
                            model.addAttribute("entregas", this.obtenerEntregas(itinerario.getId(), estaciones));
                            model.addAttribute("numCambios", numCambios);
                            model.addAttribute("estado", "Activo");
                            model.addAttribute("hayUltimoItinerario", true);
                        }
                        List<Caracteristica> caracts = selloService.getSelloPorCurso(metodosGenerales.getCursoAcademico()).getCaracteristicas();
                        model.addAttribute("caracteristicas", caracts);
                        return "consultarItinerario";
                    }
                }
            }
            return "redirect:/itinerariosAlumnado";
        }
        return "redirect:/";
    }

    @GetMapping("/itinerariosAlumnado/consultarActividad/{idEntrega}")
    public String mostrarPaginaConsultarActividadAlumnoConcreto(@PathVariable String idEntrega, Model model) {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession sesion = attr.getRequest().getSession(true);
            Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
            if (user != null) {
                Integer pos = idEntrega.indexOf("-");
                if (pos != -1) {
                    Integer idItinerario = Integer.parseInt(idEntrega.substring(0, pos));
                    String idEntrega2 = idEntrega.substring(pos + 1, idEntrega.length());
                    pos = idEntrega2.indexOf("-");
                    if(pos!=null) {
                        Integer idEstacion = Integer.parseInt(idEntrega2.substring(0, pos));
                        Integer idEstudiante = Integer.parseInt(idEntrega2.substring(pos+1, idEntrega2.length()));
                        TutorUR tutorUR = tutorUrService.getTutorURPorCodnum(user.getId());
                        Estudiante estudiante = estudianteService.getEstudiantePorCodnum(idEstudiante);
                        if ((estudiante!=null && user.tieneRol("ROL_TUTOR_UR") && tutorUR!=null && tutorUR.tieneEstudiante(estudiante)) || (estudiante!=null && user.tieneRol("ROL_COORDINADOR"))) {
                            Practicum pract = practicumService.getPracticumActivoEstudiante(estudiante, metodosGenerales.getCursoAcademico(), metodosGenerales.getConvocatoriaPorFechas());
                            if (pract.tieneItinerario(idItinerario) && entregaEstacionService.getIndicesEstacionDeItinerario(idItinerario).contains(idEstacion)) {
                                EntregaEstacion.IdEntregaEstacion id = new EntregaEstacion.IdEntregaEstacion();
                                id.setId_Itinerario(idItinerario);
                                id.setId_Estacion(idEstacion);
                                EntregaEstacion entrega = entregaEstacionService.getEntregasPorId(id);
                                model.addAttribute("feedbackTutor", entrega.getFeedbackTutorUR());
                                model.addAttribute("fechaFeedback", entrega.getFechaEntrega());
                                model.addAttribute("fechaEntrega", entrega.getFechaEntrega());
                                model.addAttribute("rutaDocu", "../../files/actividadesEstacion/" + entrega.getNombreDocu());
                                List<Caracteristica> caracts = selloService.getSelloPorCurso(metodosGenerales.getCursoAcademico()).getCaracteristicas();
                                model.addAttribute("caracteristicas", caracts);
                                model.addAttribute("estudiante", estudiante);
                                model.addAttribute("idEstudiante", estudiante.getId());
                                model.addAttribute("estacion", estacionAprendService.getEstacionPorId(idEstacion));
                                model.addAttribute("itinerario", itinerarioService.getItinerarioPorCod(idItinerario));
                                return "consultarActividadEntrega";
                            }
                        }
                    }
                }
            }
            return "redirect:/itinerariosAlumnado";
        }
        catch(NumberFormatException exception){
            return "redirect:/itinerariosAlumnado";
        }
    }

    @PostMapping("/itinerariosAlumnado/consultarActividad/nuevoComentario/{id}") //Ruta a la que solo acceden TUTOR_UR
    public String nuevaComentarioActividadEstacion(@PathVariable Integer id, @RequestParam("feedbackTutor") String feedbackTutor, @RequestParam("idItinerario") Integer idItinerario, @RequestParam("idEstacion") Integer idEstacion, Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        if (user != null) {
            EntregaEstacion.IdEntregaEstacion idEntrega = new EntregaEstacion.IdEntregaEstacion();
            idEntrega.setId_Itinerario(idItinerario);
            idEntrega.setId_Estacion(idEstacion);
            EntregaEstacion entregaEstacion = entregaEstacionService.getEntregasPorId(idEntrega);
            if(entregaEstacion!=null){
                entregaEstacion.setFeedbackTutorUR(feedbackTutor);
                entregaEstacion.setFechaFeedback(new Date());
                entregaEstacionService.guardarEntrega(entregaEstacion);
            }
            String idEntregaCadena = Integer.toString(idItinerario)+'-'+Integer.toString(idEstacion)+'-'+Integer.toString(id);
            return "redirect:/itinerariosAlumnado/consultarActividad/"+idEntregaCadena;
        }
        return "redirect:/";
    }

    public List<EstacionAprend> getEstacionesDeItinerario(Integer idItinerario){
        List<Integer> indicesEstaciones = entregaEstacionService.getIndicesEstacionDeItinerario(idItinerario);
        Collections.sort(indicesEstaciones);
        List<EstacionAprend> estaciones = new ArrayList<EstacionAprend>();
        for(int i=0;i<indicesEstaciones.size();i++){
            estaciones.add(estacionAprendService.getEstacionPorId(indicesEstaciones.get(i)));
        }
        return estaciones;
    }

    public List<EstacionAprend> posicionarEstacion(EstacionAprend estacion, Integer posicion, List<EstacionAprend> listado){
        List<EstacionAprend> listadoFinal = new ArrayList<EstacionAprend>();
        for(int i=0;i<listado.size();i++){
            if(!listado.get(i).equals(estacion)){
                listadoFinal.add(listado.get(i));
            }
        }
        listadoFinal.add(posicion-1, estacion);
        return listadoFinal;
    }

    public List<EntregaEstacion> obtenerEntregas(Integer idItinerario, List<EstacionAprend> estaciones){
        List<EntregaEstacion> entregas = new ArrayList<>();
        for(int i=0;i<estaciones.size();i++){
            EntregaEstacion.IdEntregaEstacion id = new EntregaEstacion.IdEntregaEstacion();
            id.setId_Itinerario(idItinerario);
            id.setId_Estacion(estaciones.get(i).getId());
            entregas.add(entregaEstacionService.getEntregasPorId(id));
        }
        return entregas;
    }

}
