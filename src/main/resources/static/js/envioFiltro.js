var selects = document.getElementsByTagName("select");
Array.from(selects).forEach( sel => {
     sel.addEventListener("change", function(){
        document.getElementById("filtroGrado").submit();
     });
});