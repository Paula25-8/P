package com.itinerariosdeaprendizaje.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.id.IntegralDataTypeHolder;

import java.io.Serializable;

@Entity
@Data
@Table(name="PESO_CRITERIO", schema="USER_UR")
public class PesoCriterio {
    @Embeddable
    @Data
    @NoArgsConstructor
    public static class IdPesoCriterio implements Serializable{
        @Column(name="ID_CRITERIO")
        private Integer id_Criterio;
        @Column(name="ID_RUBRICA")
        private Integer id_Rubrica;
    }
    @EmbeddedId
    private IdPesoCriterio id=new IdPesoCriterio();

    @NotNull
    private Integer porcentaje;

    @ManyToOne
    @JoinColumn(name="ID_CRITERIO", nullable=false, insertable = false, updatable = false)
    private Criterio criterio;

    @ManyToOne
    @JoinColumn(name="ID_RUBRICA", nullable=false, insertable = false, updatable = false)
    private Rubrica rubrica;
}
