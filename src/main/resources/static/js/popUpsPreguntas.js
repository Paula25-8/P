var modales = document.querySelectorAll(".fade");

/* Transformamos texto de base de datos a html */
var texto;
for(let i=0;i<modales.length;i++){
     if(modales[i].getAttribute("id")!=="modalSelloCalidad"){
         texto = modales[i].querySelector(".modal-body").innerText;
         modales[i].querySelector(".modal-body").innerHTML=texto;
     }
}


