package com.itinerariosdeaprendizaje.practicum.utils;

import com.itinerariosdeaprendizaje.practicum.model.Mencion;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@NoArgsConstructor
public class MetodosGenerales {
    public String getCursoAcademico (){
        Calendar fecha = new GregorianCalendar();
        int año = fecha.get(Calendar.YEAR)-2000;
        int mes = fecha.get(Calendar.MONTH);
        if(mes+1>=10) {
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

    public Mencion getMencionByCurso(List<Mencion> menciones, String curso){
        if(menciones!=null && !menciones.isEmpty()){
            for(int i=0;i< menciones.size();i++){
                if(menciones.get(i).getCurso().equals(curso)){
                    return menciones.get(i);
                }
            }
        }
        return null;
    }

    public List<String> getCursos(){
        List<String> cursos = new ArrayList<>();
        String ultimoCurso = this.getCursoAcademico();
        Integer primerAnio = Integer.parseInt(ultimoCurso.substring(0, ultimoCurso.indexOf("/")));
        Integer segundoAnio = Integer.parseInt(ultimoCurso.substring(ultimoCurso.indexOf("/")+1));
        Integer primerAnioNuevo = 22;
        Integer segundoAnioNuevo = 23;
        while(primerAnioNuevo<=primerAnio && segundoAnioNuevo<=segundoAnio){
            cursos.add(primerAnioNuevo+"/"+segundoAnioNuevo);
            primerAnioNuevo++;
            segundoAnioNuevo++;
        }
        return cursos;
    }

}
