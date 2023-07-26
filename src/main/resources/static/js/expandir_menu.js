var pagina = window.top.location.href;
// Buscamos la entrada del menú que coincida con la url
var encontrado = buscarTab(limpiarURL(pagina));
//alert("Hemos obtenido encontrado, ahora comprobamos si es nulo "+encontrado);
if (!encontrado){   
    // Si no encontramos la url actual, buscamos la anterior    
    encontrado = buscarTab(limpiarURL(document.referrer));
}

function buscarTab(url){
    //alert("Procedemos a buscarTab: url-> "+url);
    var navs = document.getElementsByClassName("nav-link");
    for (i=0;i<navs.length;i++){
        var nav = navs[i].getAttribute("href");
        if (nav.indexOf("?")>-1){
            nav = nav.substring(0, nav.indexOf("?"));
        }
        //alert("nav-> "+nav);
        if (url.endsWith(nav)){
            var nodoli = navs[i].parentNode;                        
            var nodoul = navs[i].parentNode.parentNode;
            navs[i].classList.add("menu_sidebar_activo"); /* nodoli.classList.add("menu_sidebar_activo"); */
            nodoul.classList.add("show");
            nodoul.ariaExpanded = "true";
            if(navs[i].href.includes('rubrica')){
                nodoli=nodoul.parentNode;
                nodoli.getElementsByTagName("a")[0].classList.add("menu_sidebar_activo");
                nodoul=nodoli.parentNode;
                nodoul.classList.add("show");
                nodoul.ariaExpanded = "true";
            }
            return true;
        }
        if((url.includes('infoEstacion') && nav.includes('/lineasAprendizaje')) || (url.includes('itinerariosPropios/consultarActividad') && nav.includes('/itinerariosPropios'))){
            var nodoli = navs[i].parentNode;
            var nodoul = navs[i].parentNode.parentNode;
            navs[i].classList.add("menu_sidebar_activo"); /* nodoli.classList.add("menu_sidebar_activo"); */
            nodoul.classList.add("show");
            nodoul.ariaExpanded = "true";
        }
    }
    return false;
}

function limpiarURL(url){
    if (url.indexOf("?")>-1){
        url = url.substring(0, url.indexOf("?"));
    }
    return url;
}