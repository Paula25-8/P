package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.PreguntaGeneral;
import com.itinerariosdeaprendizaje.practicum.repository.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PreguntaServiceTest {

    @Mock
    private PreguntaRepository preguntaRepository;
    @InjectMocks
    private PreguntaService preguntaService;

    private List<PreguntaGeneral> preguntas;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        preguntas = new ArrayList<>();
        preguntas.add(new PreguntaGeneral(1, "Enunciado 1", "Respuesta 1", "22/23"));
        preguntas.add(new PreguntaGeneral(2, "Enunciado 2", "Respuesta 2", "22/23"));
        preguntas.add(new PreguntaGeneral(3, "Enunciado 3", "Respuesta 3", "22/23"));
        preguntas.add(new PreguntaGeneral(4, "Enunciado 4", "Respuesta 4", "22/23"));
    }
    @Test
    void getPregunta() {
        when(preguntaRepository.findById(1)).thenReturn(Optional.of(new PreguntaGeneral(1, "Enunciado de la pregunta", "Respuesta de la pregunta", "22/23")));
        PreguntaGeneral pregunta = preguntaService.getPregunta(1);
        assertEquals("Enunciado de la pregunta", pregunta.getEnunciado());
        assertEquals("Respuesta de la pregunta", pregunta.getRespuesta());
        assertEquals("22/23", pregunta.getCurso());

    }

    @Test
    void nuevaPregunta() {
        PreguntaGeneral pregunta = new PreguntaGeneral();
        pregunta.setCodigo(1);
        pregunta.setEnunciado("Enunciado de la pregunta");
        pregunta.setRespuesta("Respuesta de la pregunta");
        pregunta.setCurso("22/23");
        when(preguntaRepository.save(pregunta)).thenReturn(pregunta);

        pregunta = preguntaService.nuevaPregunta(pregunta);

        assertEquals("Enunciado de la pregunta", pregunta.getEnunciado());
        assertEquals("Respuesta de la pregunta", pregunta.getRespuesta());
        assertEquals("22/23", pregunta.getCurso());
    }

    @Test
    void getPreguntas() {
        when(preguntaRepository.findAll()).thenReturn(preguntas);
        assertEquals(preguntas, preguntaService.getPreguntas());
    }

    @Test
    void getEstacionesPorCurso() {
        when(preguntaRepository.findByCurso("22/23")).thenReturn(preguntas);
        assertEquals(preguntas, preguntaService.getPreguntasPorCurso("22/23"));
    }
}