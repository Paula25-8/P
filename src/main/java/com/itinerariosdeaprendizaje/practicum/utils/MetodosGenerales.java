package com.itinerariosdeaprendizaje.practicum.utils;

import com.itinerariosdeaprendizaje.practicum.model.Grado;
import com.itinerariosdeaprendizaje.practicum.model.Mencion;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.id.IntegralDataTypeHolder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class MetodosGenerales {
    public String getCursoAcademico (){
        Calendar fecha = new GregorianCalendar();
        int año = fecha.get(Calendar.YEAR)-2000;
        int mes = fecha.get(Calendar.MONTH);
        if(mes+1>10) { // Habra que cambiar esta condicion cuando este desarrollada la aplicacion al completo
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

    // Siguientes 2 métodos necesarios para comprobar que información escrita en HTML está correctamente
    public Integer contarSubstring(String cadena, String subcadena){
        Integer numApariciones = 0;
        Integer pos = cadena.indexOf(subcadena);
        while(pos!=-1){
            numApariciones++;
            cadena = cadena.substring(pos+3);
            pos = cadena.indexOf(subcadena);
        }
        return numApariciones;
    }

    public boolean escritoEnHTML(String cadena){
        Integer numP = this.contarSubstring(cadena, "<p>");
        Integer num_P = this.contarSubstring(cadena, "</p>");
        while (num_P!=0 && numP!=0 && !cadena.equals("")){
            if(numP==num_P){
                Integer posInicioP = cadena.indexOf("<p>");
                Integer posInicio_P = cadena.indexOf("</p>");
                if(posInicio_P<posInicioP && posInicio_P!=-1){
                    return false;
                }
                else {
                    cadena = cadena.substring(posInicioP+3);
                    posInicioP = cadena.indexOf("<p>");
                    posInicio_P = cadena.indexOf("</p>");
                    if(posInicioP<posInicio_P && posInicioP!=-1){
                        return false;
                    }
                    else{
                        cadena = cadena.substring(posInicio_P+4);
                    }
                    numP = this.contarSubstring(cadena, "<p>");
                    num_P = this.contarSubstring(cadena, "</p>");
                }
            }
            else{
                return false;
            }
        }
        return true;
    }

    public Map<Integer, String> getNumLineasDisponibles(){
        Map<Integer, String> mapa = new HashMap<>();
        mapa.put(1, "1 - Azul");
        mapa.put(2, "2 - Verde");
        mapa.put(3, "3 - Morado");
        mapa.put(4, "4 - Rojo");
        return mapa;
    }

    public boolean arrayGradosIguales(List<Grado> gradosViejos, List<Grado> gradosNuevos){
        if(gradosNuevos.size()==gradosViejos.size()){
            for(int i=0;i<gradosViejos.size();i++){
                if(!gradosNuevos.contains(gradosViejos.get(i))){
                    System.out.println("Los arrays no son iguales");
                    return false;
                }
            }
            System.out.println("Los arrays son iguales");
            return true;
        }
        else{
            System.out.println("Los arrays no son iguales");
            return false;
        }
    }
}
