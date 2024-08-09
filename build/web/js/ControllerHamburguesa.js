const $openClose = document.getElementById("open-close"),
      $aside = document.getElementById("aside");

$openClose.addEventListener("click",()=>{
    $aside.classList.toggle("desplegar")
})

function verificarTokenini() {
    // Verificar si existe un token en el localStorage
    let token = localStorage.getItem("token");

    if (!token) {
        // Si no existe el token, redirigir a la página de login
        window.location.href = "http://localhost:8080/classcraft/index.html";
    }
}