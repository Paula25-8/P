var  inputsSubidaActividad = document.querySelectorAll("input.subidaDossierFinal");
Array.from(inputsSubidaActividad).forEach(input =>{
    input.addEventListener("change", function(){
        var formulario = input.parentNode.parentNode.submit();
    })
});

