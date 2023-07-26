package com.itinerariosdeaprendizaje.practicum.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.GregorianCalendar;

@NoArgsConstructor
public class MetodosGenerales {
    public String getCursoAcademico (){
        Calendar fecha = new GregorianCalendar();
        int año = fecha.get(Calendar.YEAR)-2000;
        int mes = fecha.get(Calendar.MONTH);
        if(mes+1>=9) {
            return año+"/"+(año+1);
        }else{
            return (año-1)+"/"+año;
        }
    }

    public Integer getConvocatoriaPorFechas(){
        Calendar fecha = new GregorianCalendar();
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int mes = fecha.get(Calendar.MONTH)+1;
        if(mes==7 && dia>5 && dia<22){
            return 2; // Segunda convocatoria del curso academico
        }else{
            return 1; // Primera convocatoria del curso academico
        }
    }

}
