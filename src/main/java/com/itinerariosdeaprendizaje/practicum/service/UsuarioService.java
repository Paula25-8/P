package com.itinerariosdeaprendizaje.practicum.service;

import com.itinerariosdeaprendizaje.practicum.model.Usuario;
import com.itinerariosdeaprendizaje.practicum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Devuelve objeto Usuario con esta cuasi en BD
    public Usuario getUsuarioPorCuasi(String cuasi){
        List<Usuario> usuario=usuarioRepository.findByCuasi(cuasi);
        if(!usuario.isEmpty() && usuario.size()==1){
            return usuario.get(0);
        }else{
            return null;
        }
    }

    public Usuario getUsuarioPorId(Integer id){
        Optional<Usuario> opt = usuarioRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }else{
            return null;
        }
    }

}
