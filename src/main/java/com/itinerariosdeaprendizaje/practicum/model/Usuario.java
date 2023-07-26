package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Data
@Inheritance(strategy=InheritanceType.JOINED)
@Table (name = "USUARIO",schema="USER_UR")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="CODNUM")
    private Integer id;
    @NotNull
    private String cuasi;
    @NotNull
    private String nombre;
    @NotNull
    private String apellido1;
    private String apellido2;
    @Size(max=50)
    private String dni;
    @NotNull
    private Integer activo; // Si usuario está activo será 1 e inactivo 0

    @ManyToMany
    @JoinTable(name="USUARIO_ROL",
            joinColumns = {
                    @JoinColumn(name="CODNUM_USER", referencedColumnName = "CODNUM")},
            inverseJoinColumns = {
                    @JoinColumn(name="ID_ROL", referencedColumnName = "ID")})
    private List<Autoridad> roles;

    public String getNombreCompleto() {
        return String.format("%s %s, %s", this.getApellido1() , (this.getApellido2() == null) ? "" : this.getApellido2(), this.getNombre());
    }

    public String getNombreCompletoOrdenado() {
        return String.format("%s %s %s",  this.getNombre(), this.getApellido1() , (this.getApellido2() == null) ? "" : this.getApellido2());
    }

    public boolean tieneRol (String rol){
        List<Autoridad> roles = this.getRoles();
        for(int i=0;i<roles.size();i++) {
            if (roles.get(i).getRol().descripcion==rol) {
                return true;
            }
        }
        return false;
    }

    public boolean estaActivo(){
        if(this.getActivo()==1){
            return true;
        }else{
            return false;
        }
    }
}

