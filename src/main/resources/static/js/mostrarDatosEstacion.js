/* Transformamos datos de BD a HTML en cada div */

/*COMPETENCIAS ESPACIFICAS */
var seccionCE = document.querySelector("#competencias");
var textoCE = seccionCE.innerText;
seccionCE.innerHTML=textoCE;

/* JUSTIFICACION */
var seccionJustif = document.querySelector("#justificacion");
var textoJustif=seccionJustif.innerText;
seccionJustif.innerHTML=textoJustif;
var tituloJustif = document.createElement("h3");
tituloJustif.setAttribute("class", "text-center");
tituloJustif.innerText="Justificación";
seccionJustif.insertBefore(tituloJustif, seccionJustif.getElementsByTagName("p")[0]);

/* SECUENCIA ACTIVIDADES */
var seccionActiv = document.querySelector("#actividades");
var textoActiv=seccionActiv.innerText;
seccionActiv.innerHTML=textoActiv;

/* REFLEXION */
var seccionReflexion = document.querySelector("#reflexion");
var textoReflexion=seccionReflexion.innerText;
seccionReflexion.innerHTML = textoReflexion;
var tituloReflexion = document.createElement("h3");
tituloReflexion.setAttribute("class", "text-center");
tituloReflexion.innerText="Reflexión";
seccionReflexion.insertBefore(tituloReflexion, seccionReflexion.getElementsByTagName("p")[0]);

/* EVALUACION */
var seccionEvaluacion = document.querySelector("#evaluacion");
var textoEvaluacion = seccionEvaluacion.innerText;
seccionEvaluacion.innerHTML=textoEvaluacion;
var tituloEvaluacion = document.createElement("h3");
tituloEvaluacion.setAttribute("class", "text-center");
tituloEvaluacion.innerText="Evaluación";
seccionEvaluacion.insertBefore(tituloEvaluacion, seccionEvaluacion.getElementsByTagName("p")[0]);

/* OBSERVACIONES */
var seccionObservaciones = document.querySelector("#observaciones");
var textoObservaciones = seccionObservaciones.innerText;
seccionObservaciones.innerHTML=textoObservaciones;
var tituloObservaciones = document.createElement("h3");
tituloObservaciones.setAttribute("class", "text-center");
tituloObservaciones.innerText="Observaciones";
seccionObservaciones.insertBefore(tituloObservaciones, seccionObservaciones.getElementsByTagName("p")[0]);


/* Damos funcionalidad a botones */

/* Funcion auxiliar para ocultar cualquier div de datos que esté activo */
function divDatosDesactivos(){
  var divs = document.querySelectorAll(".seccionDatos>div");
  for(let i=0;i<divs.length;i++){
     if(!divs[i].hasAttribute("hidden")){
        divs[i].setAttribute("hidden", "hidden");
     }
  }
};

/* BTN COMPETENCIAS ESPECIFICAS */
var botonCE = document.getElementById("btnCompetencias");
botonCE.addEventListener("click", function(){
   var datos = document.getElementById("competencias");
   var oculto = datos.hasAttribute("hidden");
   if (oculto) {
     divDatosDesactivos();
     datos.removeAttribute("hidden");
   } else {
     datos.setAttribute("hidden", "hidden");
   }
});

/* BTN JUSTIFICACION*/
var botonJustif=document.getElementById("btnJustificacion");
botonJustif.addEventListener("click", function(){
  var datos = document.getElementById("justificacion");
  var oculto = datos.hasAttribute("hidden");
  if (oculto) {
    divDatosDesactivos();
    datos.removeAttribute("hidden");
  } else {
    datos.setAttribute("hidden", "hidden");
  }
});

/* BTN SECUENCIA ACTIVIDADES */
var botonActividades=document.getElementById("btnActividades");
botonActividades.addEventListener("click", function(){
  var datos = document.getElementById("actividades");
  var oculto = datos.hasAttribute("hidden");
  if (oculto) {
    divDatosDesactivos();
    datos.removeAttribute("hidden");
  } else {
    datos.setAttribute("hidden", "hidden");
  }
});

/* BTN REFLEXION*/
var botonReflexion=document.getElementById("btnReflexion");
botonReflexion.addEventListener("click", function(){
  var datos = document.getElementById("reflexion");
  var oculto = datos.hasAttribute("hidden");
  if (oculto) {
    divDatosDesactivos();
    datos.removeAttribute("hidden");
  } else {
    datos.setAttribute("hidden", "hidden");
  }
});

/* BTN EVALUACION*/
var botonEvaluacion = document.getElementById("btnEvaluacion");
botonEvaluacion.addEventListener("click", function(){
  var datos = document.getElementById("evaluacion");
  var oculto = datos.hasAttribute("hidden");
  if (oculto) {
    divDatosDesactivos();
    datos.removeAttribute("hidden");
  } else {
    datos.setAttribute("hidden", "hidden");
  }
});

/* BTN OBSERVACIONES */
var botonObservaciones=document.getElementById("btnObservaciones");
botonObservaciones.addEventListener("click", function(){
  var datos = document.getElementById("observaciones");
  var oculto = datos.hasAttribute("hidden");
  if (oculto) {
    divDatosDesactivos();
    datos.removeAttribute("hidden");
  } else {
    datos.setAttribute("hidden", "hidden");
  }
});