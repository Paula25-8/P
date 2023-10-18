$(function(){
    $(document).ready(function(){
		$('.collapse').on('show.bs.collapse',function(){
		$('.collapse.show').collapse('toggle');
		});
	});
});

var btnCancelar = document.getElementById("btnCancelarNuevoDato");
btnCancelar.addEventListener("click", ()=>{
    document.querySelector("p.btnNuevo>button").click();
});