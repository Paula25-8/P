function pruebaES5(){
    console.log('Inicio de la función de prueba.');
    sleepES5(30000);    //Dormimos la ejecución durante 3 segundos
    console.log('Fin de la función de prueba.');
};

var sleepES5 = function(ms){
    var esperarHasta = new Date().getTime() + ms;
    while(new Date().getTime() < esperarHasta) continue;
};

window.addEventListener('load', function() {
    var boton = document.querySelectorAll("a.btn-secondary")[0];
    pruebaES5();
    var textoEspera = document.getElementById("textoEspera");
    textoEspera.innerHTML = '<i>Ya puede consultar el nuevo dossier final del prácticum. </i>';
    boton.setAttribute("class", "btn btn-primary w-100");
 });
