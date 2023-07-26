package com.itinerariosdeaprendizaje.practicum.repository;

import com.itinerariosdeaprendizaje.practicum.model.SelloCalidad;
import com.itinerariosdeaprendizaje.practicum.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("select u from Usuario u where u.cuasi = ?1")
    public Optional<Usuario> findByLogin(String username);
    List<Usuario> findByCuasi(String cuasi);

}
