package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Dossier;
import com.itinerariosdeaprendizaje.practicum.model.Itinerario;
import com.itinerariosdeaprendizaje.practicum.repository.DossierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.dsig.dom.DOMSignContext;
import java.util.Optional;

@Service
public class DossierService {
    @Autowired
    private DossierRepository dossierRepository;

    public Dossier getDossierPorId (Integer id) {
        Optional<Dossier> opt=dossierRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }else{
            return null;
        }
    }

    public Dossier guardarDossier(Dossier dossier){
        return dossierRepository.save(dossier);
    }

    public void eliminarDossier(Dossier dossier){
        dossierRepository.delete(dossier);
    }
}
