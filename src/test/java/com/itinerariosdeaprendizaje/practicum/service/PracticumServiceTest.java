package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Estudiante;
import com.itinerariosdeaprendizaje.practicum.model.Itinerario;
import com.itinerariosdeaprendizaje.practicum.model.Practicum;
import com.itinerariosdeaprendizaje.practicum.model.TutorUR;
import com.itinerariosdeaprendizaje.practicum.repository.PracticumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PracticumServiceTest {

    @Mock
    PracticumRepository practicumRepository;

    @InjectMocks
    private PracticumService practicumService;

    private Practicum practicum;
    private Itinerario itinerarioPrimero;
    private Itinerario itinerarioUltimo;
    private List<Practicum> practicums;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itinerarioPrimero = new Itinerario();
        itinerarioPrimero.setId(1);
        itinerarioPrimero.setFechaCreacion(new Date());
        itinerarioUltimo = new Itinerario();
        itinerarioUltimo.setId(2);
        itinerarioUltimo.setFechaCreacion(new Date());
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);
        practicum = new Practicum(2,"22/23", 8, new Date(), 0, null, 1, estudiante, new TutorUR(), 5, new Date(), itinerarioPrimero, null, null);
        practicums = new ArrayList<>();
        practicums.add(practicum);
        practicum.setId(3);
        practicums.add(practicum);
    }

    @Test
    void guardarPracticum() {
        practicum.setUltimoItinerario(itinerarioUltimo);
        when(practicumRepository.save(practicum)).thenReturn(practicum);
        assertEquals(practicum, practicumService.guardarPracticum(practicum));
    }

    @Test
    void getPracticum() {
        when(practicumRepository.findById(2)).thenReturn(Optional.ofNullable(practicum));
        Practicum practicumPrueba = practicumService.getPracticum(2);
        assertEquals("22/23", practicumPrueba.getCurso());
        assertEquals(8, practicumPrueba.getNotaFinal() );
        assertEquals(itinerarioPrimero, practicumPrueba.getPrimerItinerario());
    }

    @Test
    void getPracticumActivoEstudiante() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);
        when(practicumRepository.findByEstudianteAndCursoAndConvocatoria(estudiante, "22/23", 1)).thenReturn(practicum);
        assertEquals(practicum, practicumService.getPracticumActivoEstudiante(estudiante, "22/23", 1));
    }

    @Test
    void getPracticums() {
        when(practicumRepository.findAll()).thenReturn(practicums);
        assertEquals(practicums, practicumService.getPracticums());
    }

}