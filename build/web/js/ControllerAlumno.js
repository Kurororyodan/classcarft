// Variable global para almacenar el ID del profesor seleccionado
let alumnoSeleccionadoId = null;

// Función para seleccionar un profesor al hacer clic en la fila de la tabla
function seleccionarAlumno(idAlumno) {
    alumnoSeleccionadoId = idAlumno;
        obtenerDetallesAlumno(idAlumno);

}

// Función para cargar los datos de los profesores desde el servidor
async function cargarDatosAlumnos() {
    try {
        const response = await fetch('http://localhost:8080/ClassCraft/api/alumno/getAllAlumnos');
        if (!response.ok) {
            throw new Error('Error al cargar los datos de alumnos');
        }
        const data = await response.json();
        mostrarDatosEnTabla(data); // Mostrar los datos en la tabla
    } catch (error) {
        console.error('Error:', error);
    }
}

// Función para obtener los detalles del alumno desde el servidor
async function obtenerDetallesAlumno(idAlumno) {
    try {
        const response = await fetch('http://localhost:8080/ClassCraft/api/alumno/getAlumnoById', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `idAlumno=${idAlumno}`
        });

        if (!response.ok) {
            throw new Error('Error al obtener los detalles del alumno');
        }

        const data = await response.json();
        mostrarDetallesAlumno(data);
    } catch (error) {
        console.error('Error:', error);
    }
}

// Función para mostrar los detalles del alumno en el formulario
function mostrarDetallesAlumno(alumno) {
    const modal = document.getElementById("mymodal3");

    modal.querySelector("#txtNombre").value = alumno.persona.nombre;
    modal.querySelector("#txtApellidoPaterno").value = alumno.persona.apellidoPaterno;
    modal.querySelector("#txtApellidoMaterno").value = alumno.persona.apellidoMaterno;
    modal.querySelector("#txtGenero").value = alumno.persona.genero;
    modal.querySelector("#txtFechaNacimiento").value = alumno.persona.fechaNacimiento;
    modal.querySelector("#RFC").value = alumno.persona.rfc;
    modal.querySelector("#CURP").value = alumno.persona.curp;
    modal.querySelector("#txtDomicilio").value = alumno.persona.domicilio;
    modal.querySelector("#txtCodigoPostal").value = alumno.persona.codigoPostal;
    modal.querySelector("#txtCiudad").value = alumno.persona.ciudad;
    modal.querySelector("#txtEstado").value = alumno.persona.estado;
    modal.querySelector("#txtTelefono").value = alumno.persona.telefono;
    modal.querySelector("#txtEmail").value = alumno.persona.email;
    modal.querySelector("#txtMatricula").value = alumno.matricula;
    modal.querySelector("#txtEstatus").value = alumno.estatus;
    modal.querySelector("#txtModalidad").value = alumno.modalidad;
    modal.querySelector("#txtFechaIngreso").value = alumno.fechaIngreso;

    // Mostrar materias seleccionadas
    const materiasSeleccionadas = modal.querySelector("#materiasSeleccionadas");
    materiasSeleccionadas.innerHTML = ''; // Limpiar contenido previo
    alumno.materias.forEach(materia => {
        const option = document.createElement('option');
        option.text = materia;
        option.value = materia;
        materiasSeleccionadas.add(option);
    });
}

// Event listener para cargar los datos de los alumnos al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    cargarDatosAlumnos();
    cargarMaterias(); // Llamar a la función para cargar las materias al cargar la página

    const btnEliminarAlumno = document.querySelector('.btn-eliminar-alumno');
    btnEliminarAlumno.addEventListener('click', handleEliminarAlumno);

    const btnActivarAlumno = document.querySelector('.btn-activar-alumno');
    btnActivarAlumno.addEventListener('click', handleActivarAlumno);

    const btnBuscarAlumno = document.getElementById('btn-buscar');
    btnBuscarAlumno.addEventListener('click', buscarAlumno);
});

// Función para mostrar los datos de los profesores en la tabla HTML
function mostrarDatosEnTabla(alumnos) {
    const tbody = document.getElementById('tbody-alumnos');
    tbody.innerHTML = ''; // Limpiar contenido anterior de la tabla

    alumnos.forEach(alumno => {
        const row = document.createElement('tr');
        row.dataset.id = alumno.idAlumno; // Almacenar el ID del alumno en un atributo data-

        row.innerHTML = `
            <td>${alumno.idAlumno}</td>
            <td>${alumno.matricula}</td>
            <td>${alumno.persona.nombre}</td>
            <td>${alumno.persona.apellidoPaterno}</td>
            <td>${alumno.estatus}</td>
        `;


        // Agregar evento click para seleccionar el profesor al hacer clic en la fila
        row.addEventListener('click', () => {
            // Remover la clase de fila seleccionada de todas las filas
            const filas = tbody.querySelectorAll('tr');
            filas.forEach(fila => fila.classList.remove('fila-seleccionada'));

            // Agregar la clase de fila seleccionada a la fila actual
            row.classList.add('fila-seleccionada');

            // Llamar a la función para seleccionar el profesor
                 seleccionarAlumno(alumno.idAlumno);
        });

        tbody.appendChild(row);
    });
}

// Función para desactivar (eliminar) un profesor
async function handleEliminarAlumno() {
    if (!alumnoSeleccionadoId) {
        // Mostrar mensaje de error si no se ha seleccionado ningún alumno
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Primero seleccione un alumno.'
        });
        return;
    }

    // Mostrar SweetAlert de confirmación
    const result = await Swal.fire({
        title: '¿Está seguro?',
        text: "¡Se desactivará al Alumno!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, desactivar'
    });

    if (result.isConfirmed) {
try {
        const response = await fetch('http://localhost:8080/ClassCraft/api/alumno/desactivarAlumno', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `idAlumno=${alumnoSeleccionadoId}`
        });

        if (!response.ok) {
            throw new Error('Error al intentar desactivar el alumno');
        }

            // Mostrar SweetAlert de confirmación
            Swal.fire({
                icon: 'success',
                title: 'Alumno desactivado',
                text: 'El Alumno se ha desactivado correctamente.',
                showConfirmButton: false,
                timer: 1500 // Cerrar automáticamente el SweetAlert después de 1.5 segundos
            });

            // Limpiar la selección de profesor después de desactivarlo
            alumnoSeleccionadoId = null;

            // Recargar los datos de la tabla de profesores después de eliminar uno
            cargarDatosAlumnos();
        } catch (error) {
            console.error('Error:', error);
            // Mostrar SweetAlert de error si ocurre algún problema al desactivar al profesor
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ocurrió un error al intentar desactivar el Alumno. Intente más tarde.'
            });
        }
    }
}

// Función para activar un profesor
async function handleActivarAlumno() {
    if (!alumnoSeleccionadoId) {
        // Mostrar mensaje de error si no se ha seleccionado ningún profesor
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Primero seleccione un Alumno.'
        });
        return;
    }

    // Mostrar SweetAlert de confirmación
    const result = await Swal.fire({
        title: '¿Está seguro?',
        text: "¡El Alumno será activado!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, activar'
    });

    if (result.isConfirmed) {
        try {
            const response = await fetch('http://localhost:8080/ClassCraft/api/alumno/activarAlumno', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                 body: `idAlumno=${alumnoSeleccionadoId}`
            });

            if (!response.ok) {
                throw new Error('Error al intentar activar el Alumno');
            }

            // Mostrar SweetAlert de confirmación
            Swal.fire({
                icon: 'success',
                title: 'Alumno activado',
                text: 'El Alumno se ha activado correctamente.',
                showConfirmButton: false,
                timer: 1500 // Cerrar automáticamente el SweetAlert después de 1.5 segundos
            });

            // Limpiar la selección de profesor después de activarlo
            alumnoSeleccionadoId = null;

            // Recargar los datos de la tabla de profesores después de activar uno
            cargarDatosAlumnos();
        } catch (error) {
            console.error('Error:', error);
            // Mostrar SweetAlert de error si ocurre algún problema al activar al profesor
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ocurrió un error al intentar activar el Alumno. Intente más tarde.'
            });
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    cargarDatosAlumnos();

    // Event listener que llama a handleEliminarProfesor cuando se hace clic en el botón de eliminar
    const btnEliminarAlumno = document.querySelector('.btn-eliminar-alumno');
    btnEliminarAlumno.addEventListener('click', handleEliminarAlumno);

    // Event listener que llama a handleActivarProfesor cuando se hace clic en el botón de activar
    const btnActivarAlumno = document.querySelector('.btn-activar-alumno');
    btnActivarAlumno.addEventListener('click', handleActivarAlumno);

    // Event listener que llama a buscarAlumno cuando se hace clic en el botón de buscar
    const btnBuscarAlumno = document.getElementById('btn-buscar');
    btnBuscarAlumno.addEventListener('click', buscarAlumno);
});

async function buscarAlumno() {
    const inputBusqueda = document.getElementById('input-busqueda').value.trim();

    try {
        const response = await fetch('http://localhost:8080/ClassCraft/api/alumno/buscarAlumno', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `matricula=${inputBusqueda}`
        });

        if (!response.ok) {
            throw new Error('Error al buscar alumno');
        }

        const data = await response.json();
        mostrarDatosEnTabla(data); // Mostrar resultados en la tabla
    } catch (error) {
        console.error('Error:', error);
        // Mostrar mensaje de error usando SweetAlert u otro método
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Ocurrió un error al buscar el alumno. Intente más tarde.'
        });
    }
}





// Función para insertar un alumno utilizando Fetch API
async function insertarAlumno() {
    // Obtener los valores del formulario
    let v_nombre = document.getElementById("txtNombre").value.trim();
    let v_apellidoPaterno = document.getElementById("txtApellidoPaterno").value.trim();
    let v_apellidoMaterno = document.getElementById("txtApellidoMaterno").value.trim();
    let v_genero = document.getElementById("txtGenero").value.trim();
    let v_fechaNacimiento = document.getElementById("txtFechaNacimiento").value.trim();
    let v_rfc = document.getElementById("RFC").value.trim();
    let v_curp = document.getElementById("CURP").value.trim();
    let v_domicilio = document.getElementById("txtDomicilio").value.trim();
    let v_codigoPostal = document.getElementById("txtCodigoPostal").value.trim();
    let v_ciudad = document.getElementById("txtCiudad").value.trim();
    let v_estado = document.getElementById("txtEstado").value.trim();
    let v_telefono = document.getElementById("txtTelefono").value.trim();
    let v_email = document.getElementById("txtEmail").value.trim();
    let v_matricula = document.getElementById("txtMatricula").value.trim();
    let v_estatus = document.getElementById("txtEstatus").value.trim();
    let v_modalidad = document.getElementById("txtModalidad").value.trim();
    let v_fechaIngreso = document.getElementById("txtFechaIngreso").value.trim();

    // Validar que todos los campos estén llenos
    if (!v_nombre || !v_apellidoPaterno || !v_apellidoMaterno || !v_genero || !v_fechaNacimiento || !v_rfc || 
        !v_curp || !v_domicilio || !v_codigoPostal || !v_ciudad || !v_estado || !v_telefono || !v_email || 
        !v_matricula || !v_estatus || !v_modalidad || !v_fechaIngreso) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Todos los campos son obligatorios'
        });
        return;
    }

    // Validar que el nombre no contenga números
    if (/\d/.test(v_nombre)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El nombre no puede contener números'
        });
        return;
    }

    // Validar que el apellido paterno no contenga números
    if (/\d/.test(v_apellidoPaterno)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El apellido paterno no puede contener números'
        });
        return;
    }

    // Validar que el apellido materno no contenga números
    if (/\d/.test(v_apellidoMaterno)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El apellido materno no puede contener números'
        });
        return;
    }

    // Validar que el código postal sea numérico
    if (isNaN(v_codigoPostal)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El código postal debe ser numérico'
        });
        return;
    }

    // Validar que el teléfono sea numérico
    if (isNaN(v_telefono)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El teléfono debe ser numérico'
        });
        return;
    }

    // Validar que la matrícula sea numérica
    if (isNaN(v_matricula)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'La matrícula debe ser numérica'
        });
        return;
    }

    // Validar que el estatus sea numérico
    if (isNaN(v_estatus)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El estatus debe ser numérico'
        });
        return;
    }


    // Verificar si la matrícula ya existe
    try {
        const response = await fetch('http://localhost:8080/ClassCraft/api/alumno/verificarMatricula', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `matricula=${v_matricula}`
        });
        const data = await response.json();

        if (data.existe) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'La matrícula ya existe'
            });
            return;
        }
    } catch (error) {
        console.error('Error al verificar la matrícula:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Hubo un error al verificar la matrícula. Intente más tarde.'
        });
        return;
    }

    // Obtener las materias seleccionadas
    let materiasSeleccionadas = obtenerMateriasSeleccionadas();

    // Construir el objeto alumno
    let alumno = {
        persona: {
            nombre: v_nombre,
            apellidoPaterno: v_apellidoPaterno,
            apellidoMaterno: v_apellidoMaterno,
            genero: v_genero,
            fechaNacimiento: v_fechaNacimiento,
            rfc: v_rfc,
            curp: v_curp,
            domicilio: v_domicilio,
            codigoPostal: v_codigoPostal,
            ciudad: v_ciudad,
            estado: v_estado,
            telefono: v_telefono,
            email: v_email
        },
        matricula: v_matricula,
        estatus: v_estatus,
        modalidad: v_modalidad,
        fechaIngreso: v_fechaIngreso,
        materias: materiasSeleccionadas.map(id => ({ idMateria: id }))
    };

    // Imprimir el JSON que se va a enviar
    console.log('JSON enviado:', JSON.stringify(alumno));

    // Realizar la petición para insertar el alumno
    fetch('http://localhost:8080/ClassCraft/api/alumno/insertarAlumno', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(alumno)
    })
    .then(response => response.json())
    .then(data => {
        // Manejar la respuesta
        console.log('Alumno insertado correctamente', data);
        Swal.fire({
            title: 'Alumno Registrado',
            text: `Datos almacenados para: ${v_nombre}`,
            icon: 'success'
        });
    })
    .catch(error => {
        console.error('Error al insertar alumno:', error);
        Swal.fire({
            title: 'Error',
            text: 'Hubo un error al registrar el Alumno',
            icon: 'error'
        });
    });
}


// Función para obtener las materias seleccionadas
function obtenerMateriasSeleccionadas() {
    const materias = document.querySelectorAll('input[name="materia"]:checked');
    const materiasSeleccionadas = [];
    materias.forEach(materia => {
        materiasSeleccionadas.push(parseInt(materia.value)); // Convertir a entero
    });
    return materiasSeleccionadas;
}

async function actualizarAlumno() {
    if (!alumnoSeleccionadoId) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Primero seleccione un alumno.'
        });
        return;
    }

    // Obtener los valores del formulario
    const modal = document.getElementById("mymodal3");
    const alumno = {
        idAlumno: alumnoSeleccionadoId,
        persona: {
            nombre: modal.querySelector("#txtNombre").value,
            apellidoPaterno: modal.querySelector("#txtApellidoPaterno").value,
            apellidoMaterno: modal.querySelector("#txtApellidoMaterno").value,
            genero: modal.querySelector("#txtGenero").value,
            fechaNacimiento: modal.querySelector("#txtFechaNacimiento").value,
            rfc: modal.querySelector("#RFC").value,
            curp: modal.querySelector("#CURP").value,
            domicilio: modal.querySelector("#txtDomicilio").value,
            codigoPostal: modal.querySelector("#txtCodigoPostal").value,
            ciudad: modal.querySelector("#txtCiudad").value,
            estado: modal.querySelector("#txtEstado").value,
            telefono: modal.querySelector("#txtTelefono").value,
            email: modal.querySelector("#txtEmail").value,
            idPersona: null // Asigna el ID de la persona si es necesario
        },
        matricula: modal.querySelector("#txtMatricula").value,
        estatus: modal.querySelector("#txtEstatus").value,
        modalidad: modal.querySelector("#txtModalidad").value,
        fechaIngreso: modal.querySelector("#txtFechaIngreso").value
    };

    try {
        const response = await fetch('http://localhost:8080/ClassCraft/api/alumno/actualizarAlumno', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(alumno)
        });

        if (!response.ok) {
            throw new Error('Error al actualizar el alumno');
        }

        const data = await response.json();
        Swal.fire({
            icon: 'success',
            title: 'Alumno actualizado',
            text: data.result,
            showConfirmButton: false,
            timer: 1500
        });

        cargarDatosAlumnos(); // Recargar los datos de los alumnos en la tabla
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Ocurrió un error al intentar actualizar el Alumno. Intente más tarde.'
        });
    }
}



// Función para cargar las materias desde el servidor
function cargarMaterias() {
    const url = 'http://localhost:8080/ClassCraft/api/materia/getAll?limit=10&skip=0'; // URL para obtener todas las materias
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const materiasContainer = document.getElementById('materiasContainer');
            materiasContainer.innerHTML = ''; // Limpiar contenido previo
            
            if (data.length === 0) {
                const noMateriasMsg = document.createElement('p');
                noMateriasMsg.textContent = 'No hay materias disponibles';
                materiasContainer.appendChild(noMateriasMsg);
            } else {
                data.forEach(materia => {
                    // Crear un checkbox para cada materia
                    const checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = materia.idMateria;
                    checkbox.id = `materia-${materia.idMateria}`;
                    
                    // Crear etiqueta (label) asociada al checkbox
                    const label = document.createElement('label');
                    label.htmlFor = checkbox.id;
                    label.textContent = materia.nombre;
                    
                    // Contenedor para cada checkbox y etiqueta
                    const container = document.createElement('div');
                    container.appendChild(checkbox);
                    container.appendChild(label);
                    
                    // Agregar el contenedor al contenedor principal de materias
                    materiasContainer.appendChild(container);
                });
            }
        })
        .catch(error => console.error('Error al cargar materias:', error));
}

// Llamar a la función para cargar materias cuando la página esté lista
document.addEventListener('DOMContentLoaded', function() {
    cargarMaterias();

});


