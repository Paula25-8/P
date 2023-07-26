package com.itinerariosdeaprendizaje.practicum.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class DocumentosService {

    private String carpetaActividades = ".//src//main//resources//static//files//actividadesEstacion//";
    private String carpetaDossier = ".//src//main//resources//static//files//dossierFinal//";

    private final Logger logg = LoggerFactory.getLogger(DocumentosService.class);

    public void guardarActividad(MultipartFile docu, Integer distintitivo) {
        if(!docu.isEmpty()) {
            try {
                byte[] bytes = docu.getBytes();
                Path path = Paths.get(carpetaActividades + distintitivo + docu.getOriginalFilename().replace(" ", "_").trim());
                Files.write(path, bytes, StandardOpenOption.CREATE_NEW, StandardOpenOption.SPARSE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void eliminarActividad(String nombreDocu) throws IOException {
        Path path = Paths.get(carpetaActividades+nombreDocu);
        Files.delete(path);
    }

    public void guardarDossier(MultipartFile docu, Integer distintitivo) {
        if(!docu.isEmpty()){
            try {
                byte[] bytes = docu.getBytes();
                Path path = Paths.get(carpetaDossier + distintitivo + docu.getOriginalFilename().replace(" ", "_").trim());
                Files.write(path, bytes, StandardOpenOption.CREATE_NEW, StandardOpenOption.SPARSE);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void eliminarDossier(String nombreDocu) throws IOException {
        Path path = Paths.get(carpetaDossier+nombreDocu);
        Files.delete(path);
    }
}
