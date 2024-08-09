

function guardarUsuario() {
    let url = "http://localhost:8080/classcraft/api/login/insertUsuario";
    
    
    let p_nombreUsuario = document.getElementById("nombreUsuario").value;
    let p_contrasenia = document.getElementById("contrasenia").value;
    



    let usuario = {
        nombreUsuario:p_nombreUsuario,
        contrasenia:p_contrasenia        
    };
    console.log("Sucursal a enviar al servidor: ", usuario);

    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            usuario: JSON.stringify(usuario)
        })
    };

    fetch(url, requestOptions)
    .then(function (data) {
        return data.json();
    })
    .then(function (json) {
        console.log(json);
        // Muestra una ventana modal de éxito
        Swal.fire({
            title: 'Usuario Registrado',
            text: `Nombre de usuario almacenado como : ${p_nombreUsuario}`,
            icon: 'success'
        });

       
        
    })
    .catch(function (error) {
        console.error('Error al registrar usuario:', error);
        // Muestra una ventana modal de error
        Swal.fire({
            title: 'Error',
            text: 'Hubo un error al registrar usuario',
            icon: 'error'
        });
    });


}

function mostrarInformacion(opcion, event) {
    event.preventDefault();
    let contenido = '';

  switch (opcion) {
    case 'nosotros':
        contenido = '¡Bienvenidos a Poulet poper! Somos un equipo apasionado por el cine y la tecnología, comprometidos en brindarte la mejor experiencia cinematográfica a través de nuestra aplicación móvil. Nuestro equipo está formado por talentosos individuos, entre ellos:\n\n- Ignacio Sánchez\n- Alberto Rosas\n- Bryan Suárez\n- Kimberly Ávila\n- Lluvia Terran\n\nY muchos más profesionales dedicados a hacer de Poulet poper la mejor plataforma para compartir tus opiniones sobre películas.';
        break;
    case 'servicios':
        contenido = 'La falta de una plataforma dedicada para que los usuarios compartan sus opiniones y recomendaciones sobre películas ha sido identificada como una necesidad no cubierta en nuestra comunidad. Nuestra solución es desarrollar una aplicación móvil en Android Studio que permita a los usuarios calificar películas, dejar comentarios y compartir sus experiencias cinematográficas de manera centralizada.';
        break;
    case 'beneficios':
        contenido = 'La aplicación móvil que estamos desarrollando brindará a los usuarios la posibilidad de crear y compartir reseñas de películas. Podrán calificar películas, dejar comentarios detallados y recibir recomendaciones personalizadas en función de sus preferencias cinematográficas. Además, se integrarán funciones sociales para que los usuarios puedan seguir a otros críticos, comentar en sus reseñas y construir una comunidad en torno a la pasión por el cine.';
        break;
    case 'contactanos':
        contenido = 'Si deseas obtener más información sobre nuestra aplicación móvil para calificar películas o tienes alguna pregunta, no dudes en ponerte en contacto con nosotros. Estamos comprometidos a brindarte la mejor experiencia cinematográfica posible y esperamos poder ayudarte a descubrir nuevas películas y compartir tus opiniones con nuestra comunidad de usuarios.';
        break;
    default:
        contenido = 'Lo sentimos, la información solicitada no está disponible en este momento. Por favor, inténtalo de nuevo más tarde.';
        break;
}





    const modalContainer = document.getElementById('modal-container');
    modalContainer.innerHTML = `<div class="modal">
                                    <p>${contenido}</p>
                                </div>`;
    modalContainer.style.display = 'block';

    // Cerrar el modal al hacer clic en cualquier otro lugar de la página
    modalContainer.addEventListener('click', function(event) {
        if (event.target === modalContainer) {
            cerrarModal();
        }
    });
}

function cerrarModal() {
    const modalContainer = document.getElementById('modal-container');
    modalContainer.style.display = 'none';
}
