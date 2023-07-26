/* Transformamos texto de base de datos a html */
var modales = document.querySelectorAll(".fade");
var texto;
for(let i=0;i<modales.length;i++){
     texto = modales[i].querySelector(".modal-body").innerText;
     modales[i].querySelector(".modal-body").innerHTML=texto;
}

var selects = document.getElementsByTagName("select");
Array.from(selects).forEach( sel => {
     sel.addEventListener("change", function(){
       document.location.href=this.value;
       var selectedOption = this.options[this.selectedIndex];
       this.options[0].selected;
     });
});