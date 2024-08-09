function validaUsuario() {
    let nombreUsuario = document.getElementById('nombreUsuario');
    
    
    const noespecial = !/[^!?A-Za-z\d]/.test(nombreUsuario.value);
    
        console.log("contiene ! o ?, pero no otro caracter especial o espacios:", noespecial);
        if(!noespecial){
            
            Swal.fire({
            title: 'Error',
            text: 'el usuario no debe incluir ningun caracter especial',
            icon: 'error'
        });
        } else{
            let nombre = nombreUsuario.value.includes("!");

    let nombrecito = nombreUsuario.value.trim();
    let nombreSinMayusculas = nombrecito.toLowerCase();
    let nombreSinEspacios = nombreSinMayusculas.replace(/\s/g, "_");
    let elName = nombreSinEspacios.toUpperCase();

    

    nombreUsuario.value=elName;

    return elName;
        }
    
}





    function validaContraseña() {
        const clave = document.getElementById('contrasenia').value;
        const ochocaracteres = /.{8,}/.test(clave);
        if (!ochocaracteres) {
            
            Swal.fire({
            title: 'Error',
            text: 'La contraseña debe contener al menos 8 caracteres',
            icon: 'error'
        });
        }
        
        const mayymin = /[A-Z]/.test(clave) && /[a-z]/.test(clave);
        console.log("Mayusculas y minusculas:", mayymin);
        
        if(!mayymin){
            
            Swal.fire({
            title: 'Error',
            text: 'la contraseña debe incluir mayusculas y minusculas',
            icon: 'error'
        });
        }

        const numeros = /\d/.test(clave);
        console.log("números:", numeros);
        if(!numeros){
            
            Swal.fire({
            title: 'Error',
            text: 'la contraseña debe incluir almenos un numero',
            icon: 'error'
        });
        }
        const noespecial = !/[^!?A-Za-z\d]/.test(clave);
        console.log("contiene ! o ?, pero no otro caracter especial:", noespecial);
        if(!noespecial){
            
            Swal.fire({
            title: 'Error',
            text: 'la contraseña no debe incluir ningun caracter especial',
            icon: 'error'
        });
        }
        const noEspacios = /^\S*$/.test(clave); // Expresión regular que verifica que no haya espacios

        if (!noEspacios) {
        
        Swal.fire({
            title: 'Error',
            text: 'La contraseña no debe incluir espacios',
            icon: 'error'
        });
        return false;
        }
        

        const valida = ochocaracteres && mayymin && numeros && noespecial && noEspacios ;
        console.log("contraseña válida:", valida, "\n\n");
        
        
        return valida;
    }

    function validarCampos() {
    const usuario = document.getElementById('nombreUsuario').value;
    const pass = document.getElementById('contrasenia').value;


    if (usuario === "") {
        
        Swal.fire({
            title: 'Error',
            text: 'Todos los campos son obligatorios',
            icon: 'error'
        });
        document.getElementById("nombreUsuario").focus();
        return false;
    }

    if (pass === "") {
        
        Swal.fire({
            title: 'Error',
            text: 'Ingrese contraseña es obligatorio',
            icon: 'error'
        });
        document.getElementById("contrasenia").focus();
        return false;
    }

    

    const usuarioValido = validaUsuario();
    const contraseñaValida = validaContraseña();
    
    // Solo devolver true si todas las validaciones son exitosas
    return usuarioValido && contraseñaValida;
}


function validarEntra() {
    let usu = document.getElementById("nombreUsuario1").value;
    let cont = document.getElementById("contrasenia1").value;

    let usuario = { nombreUsuario: usu, contrasenia: cont };
    let parametros = { usuario: JSON.stringify(usuario) };

    url = "http://localhost:8080/classcraft/api/login/login";

    fetch(url, {
        method: "POST",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        },
        body: new URLSearchParams(parametros)
    }).then(response => response.json())
        .then(response => {
            if (response.idUsuario > 0 && response.idUsuario != null) {
                // Guardar el token en el localStorage
                localStorage.setItem("idUsuario", response.idUsuario);
                localStorage.setItem("token", response.token);
                localStorage.setItem("nombreUsuario", response.nombreUsuario);
                localStorage.setItem("contrasenia", response.contrasenia);

                // Mostrar mensaje de bienvenida
                Swal.fire({
                    icon: 'success',
                    title: 'Bienvenido',
                    text: '¡Gracias por visitar nuestro sitio!',
                    showConfirmButton: false,
                    timer: 3000
                });

                // Redireccionar a la siguiente página
                window.location.href = "http://localhost:8080/classcraft/Inicio.html";
            } else if (response.idUsuario == 0) {
                Swal.fire({
                    icon: 'error',
                    title: 'Usuario o contraseña incorrecta',
                    showConfirmButton: false,
                    timer: 1500 
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Ups! tenemos una falla con el servidor',
                    showConfirmButton: false,
                    timer: 3000 
                });
            }

        }).catch(error => {
            console.error('Error en la solicitud fetch:', error);
            alert('Error en la solicitud fetch');
        });
}



function salir() {
    let parametros = { t: localStorage.getItem("token") };
    let ruta = "http://localhost:8080/classcraft/api/login/logout";
    fetch(ruta, {
        method: "POST",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        },
        body: new URLSearchParams(parametros)
    }).then(response => response.json())
        .then(response => {
            if (response.result) {
                // Mostrar mensaje de despedida
                Swal.fire({
                    icon: 'success',
                    title: '¡Adiós!',
                    text: 'Nos vemos pronto.',
                    showConfirmButton: false,
                    timer: 1500 
                }).then(() => {
                    // Redirigir a la página principal
                    window.location.href = "http://localhost:8080/classcraft/index.html";
                    // Remover datos del usuario del almacenamiento local
                    localStorage.removeItem("idUsuario");
                    localStorage.removeItem("token");
                    localStorage.removeItem("nombreUsuario");
                    localStorage.removeItem("contrasenia");
                    // Recargar la página para eliminar el nombre de usuario mostrado
                    location.reload();
                });
            } else if (response.error) {
                alert(response.error);
            }
        });
}




function mostrarNombreUsuario() {
    // Verificar si existe el atributo nombreUsuario en localStorage y si el usuario ha iniciado sesión
    if (localStorage.getItem("nombreUsuario") && localStorage.getItem("token")) {
        // Obtener el nombre de usuario almacenado en localStorage
        var nombreUsuario = localStorage.getItem("nombreUsuario");

        // Actualizar el contenido del elemento de navegación
        var nombreUsuarioElemento = document.createElement("span");
        nombreUsuarioElemento.textContent = "Bienvenido " + nombreUsuario;
        nombreUsuarioElemento.classList.add("nombre-usuario");

        // Establecer el color blanco al nombre de usuario
        nombreUsuarioElemento.style.color = "white";

        // Obtener el elemento con la clase 'dropdown' para insertar el nombre de usuario
        var dropdownElement = document.querySelector(".dropdown");

        // Insertar el nombre de usuario antes del elemento 'dropdown'
        dropdownElement.parentNode.insertBefore(nombreUsuarioElemento, dropdownElement);
    }
}


    document.addEventListener("DOMContentLoaded", function() {
        mostrarNombreUsuario();
    });


document.getElementById("singUp").addEventListener("click", function(event) {
    event.preventDefault();
    if (validarCampos()) {
        guardarUsuario();
    }
});
