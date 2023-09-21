var notas = document.getElementById("tabla_rubrica").querySelectorAll("input.inputNota");
Array.from(notas, nota =>{
    nota.addEventListener("change", (event) => {
        /* Comprobacion de nota asignada es correcta y calculo de la nota media incluyendo nueva evaluacion */
        var notaParcial = parseFloat(event.target.value);;
        if(notaParcial>10 || notaParcial<0){
            /*alert("El valor de la nota asignada debe estar comprendido entre 0 y 10.");*/
            event.target.value = 0.0;
        }
        else{
            var textoCriterio = event.target.parentNode.parentNode.querySelectorAll("td.porcentajeCriterio")[0].innerText;
            var criterioNotaParcial = parseFloat(textoCriterio.substring(textoCriterio.indexOf("(")+1, textoCriterio.indexOf("%")));
            var notaMedia = document.getElementById("tabla_rubrica").querySelectorAll("input.inputNotaMedia")[0];
            if(notaMedia.value == "0.0"){
                notaMedia.value = 0.0;
            }
            var nuevaNotaMedia = parseFloat(notaMedia.value) + (notaParcial * criterioNotaParcial / 100);
            notaMedia.value = nuevaNotaMedia.toFixed(2);
        }

        /* Funcion para quitar mensaje y activar boton de evaluacion SOLO si estan todas las casillas completadas*/
        comprobarCampos(event);
    });
})

var btnGuardar = document.getElementById("btnConfirmarEvaluacion");
btnGuardar.addEventListener("click", (event)=>{
    alert("Hemos clicado en boton de descarga de pdf");
    //event.preventDefault();

    // PARTE DE DESCARGA DE PDF CON NOTAS
   /* var tabla_rubrica = document.getElementById("tabla_rubrica");
    var primeraTabla = tabla_rubrica.cloneNode(true);
    var segundaTabla = tabla_rubrica.cloneNode(true);
    var terceraTabla = tabla_rubrica.cloneNode(true);*/

    /* Borramos parte de <thead> */
    /*var thead2 = segundaTabla.getElementsByTagName("thead")[0];
    segundaTabla.removeChild(thead2);
    var thead3 = terceraTabla.getElementsByTagName("thead")[0];
    terceraTabla.removeChild(thead3);*/

    /* Borramos filas de sobra las tablas */
   /* var filasTabla1 = primeraTabla.querySelectorAll("tbody>tr");
    var filasTabla2 = segundaTabla.querySelectorAll("tbody>tr");
    var filasTabla3 = terceraTabla.querySelectorAll("tbody>tr");
    // alert(filasTabla.length);
    for(let i=0;i<filasTabla1.length;i++){
        if(i=>3){
            primeraTabla.querySelectorAll("tbody")[0].removeChild(filasTabla1[i]);
        }
        if((i<3) || (i=>7)){
            segundaTabla.querySelectorAll("tbody")[0].removeChild(filasTabla2[i]);
        }
        if(i<7){
            terceraTabla.querySelectorAll("tbody")[0].removeChild(filasTabla3[i]);
        }
    }
    // alert("Primera tabla con solo filas necesarias: "+primeraTabla.innerHTML);
    // alert("Segunda tabla con solo filas necesarias: "+segundaTabla.innerHTML);
    // alert("Tercera tabla con solo filas necesarias: "+terceraTabla.innerHTML);*/

    /* Borramos parte de <tfoot> */
   /* var tfoot1 = primeraTabla.getElementsByTagName("tfoot")[0];
    var tfoot2 = segundaTabla.getElementsByTagName("tfoot")[0];
    primeraTabla.removeChild(tfoot1);
    segundaTabla.removeChild(tfoot2);

    var cajaRubrica = document.getElementById("cajaRubrica");
    var cajaRubricaNueva1 = cajaRubrica.cloneNode(true);
    var tabla_rubrica1 = cajaRubricaNueva1.getElementsByTagName("table")[0];
    //cajaRubricaNueva1.replaceChild(tablaRubrica1, primeraTabla);
    cajaRubricaNueva1.removeChild(tabla_rubrica1);
    cajaRubricaNueva1.appendChild(primeraTabla);
    console.log(cajaRubricaNueva1);


    var cajaRubricaNueva2 = cajaRubrica.cloneNode(true);
    var tabla_rubrica2 = cajaRubricaNueva2.querySelectorAll("#tabla_rubrica")[0];
    // cajaRubricaNueva2.replaceChild(tabla_rubrica2, segundaTabla);
    cajaRubricaNueva2.removeChild(tabla_rubrica2);
    cajaRubricaNueva2.appendChild(segundaTabla);
    console.log(cajaRubricaNueva2);

    var cajaRubricaNueva3 = cajaRubrica.cloneNode(true);
    var tabla_rubrica3 = cajaRubricaNueva3.querySelectorAll("#tabla_rubrica")[0];
    // cajaRubricaNueva3.replaceChild(tabla_rubrica3, terceraTabla);
    cajaRubricaNueva3.removeChild(tabla_rubrica3);
    cajaRubricaNueva3.appendChild(terceraTabla);
    console.log(cajaRubricaNueva3);
    */


    /*var contenedorEvaluacion = $('#contenedorEvaluacion');
    var formulario = contenedorEvaluacion[0].getElementsByTagName("form")[0];
    formulario.removeChild(document.getElementById("divAviso").parentNode);
    formulario.removeChild(document.getElementById("botonesConfirmarEvaluacion"));*/

   /* var contenedorEvaluacion = document.getElementById("contenedorEvaluacion");
    var formulario = contenedorEvaluacion.getElementsByTagName("form")[0];
    formulario.removeChild(document.getElementById("divAviso").parentNode);
    formulario.removeChild(document.getElementById("botonesConfirmarEvaluacion"));

    var opt = {
      //margin:       ,
      filename:     'prueba.pdf',
      pagebreak:    {  mode: 'avoid-all', after: '.saltoPagina' },
      //html2canvas:  { scale: 2 },
      jsPDF:        { unit: 'mm', format: 'a4', orientation: 'landscape' }
    };

    // New Promise-based usage:
    html2pdf().set(opt).from(contenedorEvaluacion).save();*/

    /*var options = {
        size: '100px',
        background: '#fff',
    };
    var pdf = new jsPDF('p', 'mm', 'a4');  // 'p' -> pagina vertical ; 'l' -> pagina horizontal
    pdf.addHTML(contenedorEvaluacion , 0, 5, options, function(){
        pdf.save("prueba.pdf");
    });


    pdf.addHTML(cajaRubricaNueva2, 0, 5, options, function(){});
    pdf.addPage();
    pdf.addHTML(cajaRubricaNueva3, 0, 5, options, function(){});
    */


    /*html2canvas(elementHTML).then((canvas) => {
          var imgWidth = 200;
          var pageHeight = 190;
          var imgHeight = canvas.height * imgWidth / canvas.width;
          var heightLeft = imgHeight;
          const contentDataURL = canvas.toDataURL('image/png', 10)
          var options = {
          size: '70px',
          background: '#fff',
          pagesplit: true,
        };
        let pdf = new jsPDF('p', 'mm', 'a4', 1); // A4 size page of PDF
        var position = 0;
        var width = pdf.internal.pageSize.width;
        var height = pdf.internal.pageSize.height;
        pdf.addImage(contentDataURL, 'PNG', 2, position, imgWidth, imgHeight, options)
        heightLeft -= pageHeight;
        while (heightLeft >= 0) {
          position = heightLeft - imgHeight;
          pdf.addPage();
          pdf.addImage(contentDataURL, 'PNG', 2, position, imgWidth, imgHeight, options);
          heightLeft -= pageHeight;
        }
        pdf.save('prueba3.pdf'); // Generated PDF
        });*/

    //var pdf = new jsPDF();

    /*var specialElementHandlers = {
        '#elementH': function (element, renderer) {
            return true;
        }
    };
    pdf.fromHTML(elementHTML , 15, 15
    , {
        'width': 170,
        'elementHandlers': specialElementHandlers
    }
    );

    alert("Procedemos a guardar el pdf");
    // Save the PDF
    pdf.save('ejemploEvaluacion.pdf');*/
});

function comprobarCampos(evt){
    var btnGuardar = document.getElementById("btnConfirmarEvaluacion");
    var camposRellenos = true;
    for(var i=0; i<notas.length; i++){
        if(notas[i].value === ""){
            camposRellenos = false;
        }
    };
    var aviso = document.getElementById("divAviso");
    var claseBtn = btnGuardar.getAttribute("class");
    // Faltan criterios sin rellenar
    if(camposRellenos === false){
        //alert("Falta algun campo por rellenar");
        if(aviso.hidden === true){
            aviso.removeAttribute("hidden");
        }
        if(!claseBtn.includes("disabled")){
            claseBtn += " disabled";
            btnGuardar.setAttribute("class", claseBtn);
        }
    }
    // Todos los criterios rellenados
    else{
        aviso.hidden = true;
        if(claseBtn.includes("disabled")){
            var i = claseBtn.indexOf("disabled");
            claseBtn = claseBtn.substring(0, i);
            btnGuardar.setAttribute("class", claseBtn);
        }
    }
}