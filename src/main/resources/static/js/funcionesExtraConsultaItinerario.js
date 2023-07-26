$(document).ready(function(){
    $.each($('a.disabled'), function(index, value) {
      $(this).bind('click', false);
    });
});

var canvasEstaciones = document.getElementsByTagName("canvas")[1];
var modalInfo = document.getElementById("modalSelloCalidad");
var botonInfo = document.getElementById("infoSello").parentNode;
var spanEstaciones = canvasEstaciones.parentNode.getElementsByTagName("span")

botonInfo.addEventListener("click", () => {
    canvasEstaciones.style.visibility='hidden';
    Array.from(spanEstaciones).map(span =>{
        span.style.visibility='hidden';
    })
})

$('#modalSelloCalidad').on('hidden.bs.modal', () =>{
    canvasEstaciones.style.visibility='visible';
    Array.from(spanEstaciones).map(span =>{
        span.style.visibility='visible';
    })
});

var  inputsSubidaActividad = document.querySelectorAll("input.subidaActividadEstacion");
Array.from(inputsSubidaActividad).forEach(input =>{
    input.addEventListener("change", function(){
        var formulario = input.parentNode.parentNode.submit();
    })
});