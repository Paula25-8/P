package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.EntregaEstacion;
import com.itinerariosdeaprendizaje.practicum.repository.EntregaEstacionRepository;
import com.itinerariosdeaprendizaje.practicum.utils.MetodosGenerales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EntregaEstacionService {

    private MetodosGenerales metodosGenerales = new MetodosGenerales();
    @Autowired
    private EntregaEstacionRepository entregaEstacionRepository;

    public EntregaEstacion getEntregasPorId(EntregaEstacion.IdEntregaEstacion id){
        Optional<EntregaEstacion> opt = entregaEstacionRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        else{
            return null;
        }
    }

    public List<EntregaEstacion> getEntregas(){
        return entregaEstacionRepository.findAll();
    }

    public List<Integer> getIndicesEstacionDeItinerario(Integer idItinerario){
        List<Integer> indicesEstaciones = new ArrayList<Integer>();
        List<EntregaEstacion> entregas = this.entregaEstacionRepository.findByCurso(metodosGenerales.getCursoAcademico());
        for(int i=0;i<entregas.size();i++){
            if(entregas.get(i).getId().getId_Itinerario()==idItinerario){
                indicesEstaciones.add(entregas.get(i).getId().getId_Estacion());
            }
        }
        return indicesEstaciones;
    }

    public List<EntregaEstacion> getEntregasDeItinerario(Integer idItinerario){
        List<EntregaEstacion> entregaEstaciones = new ArrayList<EntregaEstacion>();
        List<EntregaEstacion> entregas = this.entregaEstacionRepository.findByCurso(metodosGenerales.getCursoAcademico());
        for(int i=0;i<entregas.size();i++){
            if(entregas.get(i).getId().getId_Itinerario()==idItinerario){
                entregaEstaciones.add(entregas.get(i));
            }
        }
        return entregaEstaciones;
    }

    public EntregaEstacion guardarEntrega(EntregaEstacion entregaEstacion){
        return entregaEstacionRepository.save(entregaEstacion);
    }

    public void removeEntrega(EntregaEstacion entrega){
        entregaEstacionRepository.delete(entrega);
    }

    // Cargamos documento a base de datos
    /*public EntregaEstacion subidaDocumento(MultipartFile file, EntregaEstacion entrega) throws IOException {
        entrega.setDocu(ArchivosUtils.compressDocu(file.getBytes()));
        System.out.println("Documento de entrega: "+entrega.getDocu().toString());
        return entregaEstacionRepository.save(entrega);
    }*/
    // Obtenemos documento de base de datos
    /*public byte[] descargaDocumento(EntregaEstacion entrega){
        return ArchivosUtils.decompressDocu(entrega.getDocu());
    }*/
}
