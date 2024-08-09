let profesorSeleccionadoId = null;

// Función para seleccionar un profesor al hacer clic en la fila de la tabla
function seleccionarProfesor(idProfesor) {
    profesorSeleccionadoId = idProfesor;
    cargarDatosProfesorPorId(idProfesor);
}

// Función para cargar los datos de los profesores desde el servidor
async function cargarDatosProfesor() {
    try {
        const response = await fetch('http://localhost:8080/classcraft/api/profesor2/getAllProfesores');
        if (!response.ok) {
            throw new Error('Error al cargar los datos del profesor');
        }
        const data = await response.json();
        mostrarDatosEnTabla(data); // Mostrar los datos en la tabla
    } catch (error) {
        console.error('Error:', error);
    }
}


function mostrarDatosEnTabla(profesores) {
    const tbody = document.getElementById('tbody-profesor');
    tbody.innerHTML = ''; // Limpiar contenido anterior de la tabla

    profesores.forEach(profesor => {
        const row = document.createElement('tr');
        row.dataset.id = profesor.idProfesor; // Almacenar el ID del profesor en un atributo data-

        row.innerHTML = `
            <td>${profesor.idProfesor}</td>
            <td>${profesor.codigo}</td>
            <td>${profesor.persona.nombre}</td>
            <td>${profesor.persona.apellidoPaterno}</td>
            <td>${profesor.estatus}</td>
        `;

        // Agregar evento click para seleccionar el profesor al hacer clic en la fila
        row.addEventListener('click', () => {
            // Remover la clase de fila seleccionada de todas las filas
            const filas = tbody.querySelectorAll('tr');
            filas.forEach(fila => fila.classList.remove('fila-seleccionada'));

            // Agregar la clase de fila seleccionada a la fila actual
            row.classList.add('fila-seleccionada');

            // Llamar a la función para seleccionar el profesor
            seleccionarProfesor(profesor.idProfesor);
        });

        tbody.appendChild(row);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    cargarDatosProfesor();
});
    
// Función para desactivar (eliminar) un profesor
async function handleEliminarProfesor() {
    if (!profesorSeleccionadoId) {
        // Mostrar mensaje de error si no se ha seleccionado ningún profesor
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Primero seleccione un profesor.'
        });
        return;
    }

    // Mostrar SweetAlert de confirmación
    const result = await Swal.fire({
        title: '¿Está seguro?',
        text: "¡Se desactivará al profesor!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, desactivar'
    });

    if (result.isConfirmed) {
        try {
            const response = await fetch('http://localhost:8080/classcraft/api/profesor2/desactivarProfesor', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `idProfesor=${profesorSeleccionadoId}`
            });

            if (!response.ok) {
                throw new Error('Error al intentar desactivar el profesor');
            }

            // Mostrar SweetAlert de confirmación
            Swal.fire({
                icon: 'success',
                title: 'Profesor desactivado',
                text: 'El profesor se ha desactivado correctamente.',
                showConfirmButton: false,
                timer: 1500 // Cerrar automáticamente el SweetAlert después de 1.5 segundos
            });

            // Limpiar la selección de profesor después de desactivarlo
            profesorSeleccionadoId = null;

            // Recargar los datos de la tabla de profesores después de eliminar uno
            cargarDatosProfesor();
        } catch (error) {
            console.error('Error:', error);
            // Mostrar SweetAlert de error si ocurre algún problema al desactivar al profesor
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ocurrió un error al intentar desactivar el profesor. Intente más tarde.'
            });
        }
    }
}

// Función para activar un profesor
async function handleActivarProfesor() {
    if (!profesorSeleccionadoId) {
        // Mostrar mensaje de error si no se ha seleccionado ningún profesor
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Primero seleccione un profesor.'
        });
        return;
    }

    // Mostrar SweetAlert de confirmación
    const result = await Swal.fire({
        title: '¿Está seguro?',
        text: "¡El profesor será activado!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, activar'
    });

    if (result.isConfirmed) {
        try {
            const response = await fetch('http://localhost:8080/classcraft/api/profesor2/activarProfesor', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `idProfesor=${profesorSeleccionadoId}`
            });

            if (!response.ok) {
                throw new Error('Error al intentar activar el profesor');
            }

            // Mostrar SweetAlert de confirmación
            Swal.fire({
                icon: 'success',
                title: 'Profesor activado',
                text: 'El profesor se ha activado correctamente.',
                showConfirmButton: false,
                timer: 1500 // Cerrar automáticamente el SweetAlert después de 1.5 segundos
            });

            // Limpiar la selección de profesor después de activarlo
            profesorSeleccionadoId = null;

            // Recargar los datos de la tabla de profesores después de activar uno
            cargarDatosProfesor();
        } catch (error) {
            console.error('Error:', error);
            // Mostrar SweetAlert de error si ocurre algún problema al activar al profesor
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ocurrió un error al intentar activar el profesor. Intente más tarde.'
            });
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    cargarDatosProfesor();

    // Event listener que llama a handleEliminarProfesor cuando se hace clic en el botón de eliminar
    const btnEliminarProfesor = document.querySelector('.btn-eliminar-profesor');
    btnEliminarProfesor.addEventListener('click', handleEliminarProfesor);

    // Event listener que llama a handleActivarProfesor cuando se hace clic en el botón de activar
    const btnActivarProfesor = document.querySelector('.btn-activar-profesor');
    btnActivarProfesor.addEventListener('click', handleActivarProfesor);

    // Event listener que llama a buscarAlumno cuando se hace clic en el botón de buscar
    const btnBuscarProfesor = document.getElementById('btn-buscar');
    btnBuscarProfesor.addEventListener('click', buscarProfesor);
});

async function buscarProfesor() {
    const inputBusqueda = document.getElementById('input-busqueda').value.trim();

    try {
        const response = await fetch('http://localhost:8080/classcraft/api/profesor2/buscarProfesor', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `codigo=${inputBusqueda}`
        });

        if (!response.ok) {
            throw new Error('Error al buscar profesor');
        }

        const data = await response.json();
        mostrarDatosEnTabla(data); // Mostrar resultados en la tabla
    } catch (error) {
        console.error('Error:', error);
        // Mostrar mensaje de error usando SweetAlert u otro método
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Ocurrió un error al buscar el profesor. Intente más tarde.'
        });
    }
}



// Función para obtener las materias seleccionadas
function obtenerMateriasSeleccionadas() {
    const materias = document.querySelectorAll('input[name="materia"]:checked');
    const materiasSeleccionadas = [];
    materias.forEach(materia => {
        materiasSeleccionadas.push(materia.value);
    });
    return materiasSeleccionadas;
}

/// Función para cargar las materias desde el servidor
function cargarMaterias() {
    const url = 'http://localhost:8080/classcraft/api/materia/getAll?limit=100&skip=0';
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const materiasContainer = document.getElementById('materiasContainer');
            materiasContainer.innerHTML = '';

            if (data.length === 0) {
                const noMateriasMsg = document.createElement('p');
                noMateriasMsg.textContent = 'No hay materias disponibles';
                materiasContainer.appendChild(noMateriasMsg);
            } else {
                data.forEach(materia => {
                    const checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = materia.idMateria;
                    checkbox.id = `materia-${materia.idMateria}`;
                    checkbox.setAttribute('data-nombre', materia.nombre);
                    
                    const label = document.createElement('label');
                    label.htmlFor = checkbox.id;
                    label.textContent = materia.nombre;
                    
                    const container = document.createElement('div');
                    container.classList.add('materia-item');
                    container.appendChild(checkbox);
                    container.appendChild(label);
                    
                    materiasContainer.appendChild(container);
                });
            }
        })
        .catch(error => console.error('Error al cargar materias:', error));
}

function filtrarMaterias() {
    const input = document.getElementById('buscarMateria').value.toLowerCase();
    const items = document.querySelectorAll('.materia-item');

    items.forEach(item => {
        const nombreMateria = item.querySelector('label').textContent.toLowerCase();
        if (nombreMateria.includes(input)) {
            item.style.display = '';
        } else {
            item.style.display = 'none';
        }
    });
}

document.addEventListener('DOMContentLoaded', function() {
    cargarMaterias();
    cargarDiasDisponibles();
});


//function cargarDiasDisponibles() {
//    const url = 'http://localhost:8080/classcraft/api/dia_disponible/getAllDiasDisponibles';
//    fetch(url)
//        .then(response => response.json())
//        .then(data => {
//            const diasDisponiblesContainer = document.getElementById('dias_disponiblesContainer');
//            diasDisponiblesContainer.innerHTML = '';
//
//            if (data.length === 0) {
//                const noDiasMsg = document.createElement('p');
//                noDiasMsg.textContent = 'No hay días disponibles';
//                diasDisponiblesContainer.appendChild(noDiasMsg);
//            } else {
//                data.forEach(dia => {
//                    // Crear elementos HTML para cada día y sus horas
//                    const checkbox = document.createElement('input');
//                    checkbox.type = 'checkbox';
//                    checkbox.name = 'dia_disponible';
//                    checkbox.value = dia.idDia;
//                    checkbox.setAttribute('data-nombre', dia.nombre);
//                    checkbox.id = `dia-${dia.idDia}`;
//                    
//                    const label = document.createElement('label');
//                    label.htmlFor = checkbox.id;
//                    label.textContent = dia.nombre;
//                    
//                    // Crear input para Hora Entrada
//                    const horaEntrada = document.createElement('input');
//                    horaEntrada.type = 'text'; // Cambiado a 'text' para formato HH:mm:ss
//                    horaEntrada.className = 'form-control';
//                    horaEntrada.id = `horaEntrada-${dia.idDia}`;
//                    horaEntrada.name = `horaEntrada-${dia.idDia}`;
//                    horaEntrada.placeholder = 'HH:mm:ss';
//                    horaEntrada.style.marginLeft = '10px';
//                    
//                    // Crear input para Hora Salida
//                    const horaSalida = document.createElement('input');
//                    horaSalida.type = 'text'; // Cambiado a 'text' para formato HH:mm:ss
//                    horaSalida.className = 'form-control';
//                    horaSalida.id = `horaSalida-${dia.idDia}`;
//                    horaSalida.name = `horaSalida-${dia.idDia}`;
//                    horaSalida.placeholder = 'HH:mm:ss';
//                    horaSalida.style.marginLeft = '10px';
//
//                    // Contenedor para el día, hora entrada y hora salida
//                    const container = document.createElement('div');
//                    container.className = 'dia-container';
//                    container.style.display = 'flex';
//                    container.style.alignItems = 'center';
//                    container.style.marginBottom = '10px';
//                    
//                    container.appendChild(checkbox);
//                    container.appendChild(label);
//                    container.appendChild(horaEntrada);
//                    container.appendChild(horaSalida);
//                    
//                    diasDisponiblesContainer.appendChild(container);
//                });
//            }
//        })
//        .catch(error => console.error('Error al cargar días disponibles:', error));
//}

document.addEventListener('DOMContentLoaded', function() {
    cargarMaterias();
    cargarDiasDisponibles();
});

// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

/// Función para cargar las materias desde el servidor
function cargarMateriasactu() {
    const url = 'http://localhost:8080/classcraft/api/materia/getAll?limit=100&skip=0';
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const materiasContainer = document.getElementById('materiasContaineractualizar');
            materiasContainer.innerHTML = '';
            
            if (data.length === 0) {
                const noMateriasMsg = document.createElement('p');
                noMateriasMsg.textContent = 'No hay materias disponibles';
                materiasContainer.appendChild(noMateriasMsg);
            } else {
                data.forEach(materia => {
                    const checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = materia.idMateria;
                    checkbox.id = `materia-${materia.idMateria}`;
                    checkbox.setAttribute('data-nombre', materia.nombre);
                    
                    const label = document.createElement('label');
                    label.htmlFor = checkbox.id;
                    label.textContent = materia.nombre;
                    
                    const container = document.createElement('div');
                    container.classList.add('materia-item');
                    container.appendChild(checkbox);
                    container.appendChild(label);
                    
                    materiasContainer.appendChild(container);
                });
            }
        })
        .catch(error => console.error('Error al cargar materias:', error));
}

// Función para filtrar las materias en la sección de actualización
function filtrarMateriasActu() {
    const input = document.getElementById('buscarMateriaActu').value.toLowerCase();
    const items = document.querySelectorAll('#materiasContaineractualizar .materia-item');

    items.forEach(item => {
        const nombreMateria = item.querySelector('label').textContent.toLowerCase();
        if (nombreMateria.includes(input)) {
            item.style.display = '';
        } else {
            item.style.display = 'none';
        }
    });
}

// Llamada a las funciones para cargar las materias y los días disponibles
document.addEventListener('DOMContentLoaded', function() {
    cargarMaterias();
    cargarDiasDisponibles();
    cargarMateriasactu();
});


async function toggleMateriaDias() {
    const result = await Swal.fire({
        title: '¿Está seguro?',
        text: "¡Modificará días y materias!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí'
    });

    if (result.isConfirmed) {
        const container = document.getElementById('materiasDiasContaineractu');
        if (container.style.display === 'none' || container.style.display === '') {
            container.style.display = 'block';
            cargarMateriasactu();
            cargarDiasDisponibles();
        } else {
            container.style.display = 'none';
        }
    }
}
// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//function cargarDiasDisponibles() {
//    const url = 'http://localhost:8080/classcraft/api/dia_disponible/getAllDiasDisponibles';
//    fetch(url)
//        .then(response => response.json())
//        .then(data => {
//            const diasDisponiblesContainer = document.getElementById('dias_disponiblesContainer');
//            diasDisponiblesContainer.innerHTML = '';
//
//            if (data.length === 0) {
//                const noDiasMsg = document.createElement('p');
//                noDiasMsg.textContent = 'No hay días disponibles';
//                diasDisponiblesContainer.appendChild(noDiasMsg);
//            } else {
//                data.forEach(dia => {
//                    // Crear elementos HTML para cada día y sus horas
//                    const checkbox = document.createElement('input');
//                    checkbox.type = 'checkbox';
//                    checkbox.name = 'dia_disponible';
//                    checkbox.value = dia.idDia;
//                    checkbox.setAttribute('data-nombre', dia.nombre);
//                    checkbox.id = `dia-${dia.idDia}`;
//                    
//                    const label = document.createElement('label');
//                    label.htmlFor = checkbox.id;
//                    label.textContent = dia.nombre;
//                    
//                    // Crear input para Hora Entrada
//                    const horaEntrada = document.createElement('input');
//                    horaEntrada.type = 'text'; // Cambiado a 'text' para formato HH:mm:ss
//                    horaEntrada.className = 'form-control';
//                    horaEntrada.id = `horaEntrada-${dia.idDia}`;
//                    horaEntrada.name = `horaEntrada-${dia.idDia}`;
//                    horaEntrada.placeholder = 'HH:mm:ss';
//                    horaEntrada.style.marginLeft = '10px';
//                    
//                    // Crear input para Hora Salida
//                    const horaSalida = document.createElement('input');
//                    horaSalida.type = 'text'; // Cambiado a 'text' para formato HH:mm:ss
//                    horaSalida.className = 'form-control';
//                    horaSalida.id = `horaSalida-${dia.idDia}`;
//                    horaSalida.name = `horaSalida-${dia.idDia}`;
//                    horaSalida.placeholder = 'HH:mm:ss';
//                    horaSalida.style.marginLeft = '10px';
//
//                    // Contenedor para el día, hora entrada y hora salida
//                    const container = document.createElement('div');
//                    container.className = 'dia-container';
//                    container.style.display = 'flex';
//                    container.style.alignItems = 'center';
//                    container.style.marginBottom = '10px';
//                    
//                    container.appendChild(checkbox);
//                    container.appendChild(label);
//                    container.appendChild(horaEntrada);
//                    container.appendChild(horaSalida);
//                    
//                    diasDisponiblesContainer.appendChild(container);
//                });
//            }
//        })
//        .catch(error => console.error('Error al cargar días disponibles:', error));
//}
// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
async function cargarDatosProfesorPorId(idProfesor) {
    try {
        const response = await fetch('http://localhost:8080/classcraft/api/profesor2/getProfesorById', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `idProfesor=${idProfesor}`
        });

        if (!response.ok) {
            throw new Error('Error al cargar los datos del profesor');
        }

        const profesor = await response.json();
        llenarModalProfesor(profesor); // Llenar el modal con los datos del profesor
    } catch (error) {
        console.error('Error:', error);
        // Mostrar mensaje de error usando SweetAlert u otro método
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Ocurrió un error al cargar los datos del profesor. Intente más tarde.'
        });
    }
}

function llenarModalProfesor(profesor) {
    // Rellena los campos del modal con los datos del profesor
    document.getElementById('txtNombre').value = profesor.persona.nombre || '';
    document.getElementById('txtApellidoPaterno').value = profesor.persona.apellidoPaterno || '';
    document.getElementById('txtApellidoMaterno').value = profesor.persona.apellidoMaterno || '';
    document.getElementById('txtGenero').value = profesor.persona.genero || '';
    document.getElementById('txtFechaNacimiento').value = profesor.persona.fechaNacimiento || '';
    document.getElementById('RFC').value = profesor.persona.rfc || '';
    document.getElementById('CURP').value = profesor.persona.curp || '';
    document.getElementById('txtDomicilio').value = profesor.persona.domicilio || '';
    document.getElementById('txtCodigoPostal').value = profesor.persona.codigoPostal || '';
    document.getElementById('txtCiudad').value = profesor.persona.ciudad || '';
    document.getElementById('txtEstado').value = profesor.persona.estado || '';
    document.getElementById('txtTelefono').value = profesor.persona.telefono || '';
    document.getElementById('txtEmail').value = profesor.persona.email || '';
    document.getElementById('txtidProfesor').value = profesor.idProfesor || '';
    document.getElementById('txtCodigo').value = profesor.codigo || '';
    document.getElementById('txtDisponibilidad_Horaria').value = profesor.disponibilidad_horaria || '';
    document.getElementById('txtFechaIngreso').value = profesor.fechaIngreso || '';

    // Establecer el valor del campo de selección de estatus
    const estatusSelect = document.getElementById('txtEstatus');
    estatusSelect.value = profesor.estatus !== undefined ? profesor.estatus : '';

    // Rellenar días disponibles
    const diasContainer = document.getElementById('dias_disponiblesContainerNuevo');
    diasContainer.innerHTML = '';
    if (profesor.disponibilidad_dias && profesor.disponibilidad_dias.length > 0) {
        profesor.disponibilidad_dias.forEach(dia => {
            const diaDiv = document.createElement('div');
            diaDiv.className = 'form-group';

            const diaLabel = document.createElement('label');
            diaLabel.textContent = 'Día:';
            const diaInput = document.createElement('input');
            diaInput.type = 'text';
            diaInput.className = 'form-control';
            diaInput.value = dia.nombre || '';

            const horaInicioLabel = document.createElement('label');
            horaInicioLabel.textContent = 'Hora Entrada:';
            const horaInicioInput = document.createElement('input');
            horaInicioInput.type = 'text';
            horaInicioInput.className = 'form-control';
            horaInicioInput.value = dia.horaInicio || '';

            const horaFinLabel = document.createElement('label');
            horaFinLabel.textContent = 'Hora Salida:';
            const horaFinInput = document.createElement('input');
            horaFinInput.type = 'text';
            horaFinInput.className = 'form-control';
            horaFinInput.value = dia.horaFin || '';

            diaDiv.appendChild(diaLabel);
            diaDiv.appendChild(diaInput);
            diaDiv.appendChild(horaInicioLabel);
            diaDiv.appendChild(horaInicioInput);
            diaDiv.appendChild(horaFinLabel);
            diaDiv.appendChild(horaFinInput);
            diasContainer.appendChild(diaDiv);
        });
    } else {
        const noDiasMsg = document.createElement('p');
        noDiasMsg.textContent = 'No hay días disponibles para este profesor';
        diasContainer.appendChild(noDiasMsg);
    }

    // Rellenar materias seleccionadas
    const materiasSeleccionadas = document.getElementById('materiasSeleccionadas');
    materiasSeleccionadas.innerHTML = '';
    if (profesor.materias && profesor.materias.length > 0) {
        profesor.materias.forEach(materia => {
            const option = document.createElement('option');
            option.textContent = materia.nombre;
            option.value = materia.idMateria;
            materiasSeleccionadas.appendChild(option);
        });
    }
}

async function insertarProfesor() {
    const nombre = document.getElementById('txtNombreProf').value.trim();
    const apellidoPaterno = document.getElementById('txtApellidoPaternoProf').value.trim();
    const apellidoMaterno = document.getElementById('txtApellidoMaternoProf').value.trim();
    const genero = document.getElementById('txtGeneroProf').value.trim();
    const fechaNacimiento = document.getElementById('txtFechaNacimiento1').value.trim();
    const rfc = document.getElementById('RFCProf').value.trim();
    const curp = document.getElementById('CURPProf').value.trim();
    const domicilio = document.getElementById('txtDomicilioProf').value.trim();
    const codigoPostal = document.getElementById('txtCodigoPostalProf').value.trim();
    const ciudad = document.getElementById('txtCiudadProf').value.trim();
    const estado = document.getElementById('txtEstadoProf').value.trim();
    const telefono = document.getElementById('txtTelefonoProf').value.trim();
    const email = document.getElementById('txtEmailProf').value.trim();
    const codigo = document.getElementById('txtCodigoProf').value.trim();
    const disponibilidadHoraria = parseInt(document.getElementById('txtDisponibilidad_HorariaProf').value);
    const estatus = parseInt(document.getElementById('txtEstatusProf').value);
    const fechaIngreso = document.getElementById('txtFechaIngresoProf').value.trim();

    // Validaciones
    if (!validarTextoSinSimbolos(nombre) || !validarTextoSinSimbolos(apellidoPaterno) || !validarTextoSinSimbolos(apellidoMaterno) || !validarTextoSinSimbolos(ciudad) || !validarTextoSinSimbolos(estado)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Nombre, Apellido Paterno, Apellido Materno, Ciudad y Estado no deben contener símbolos raros.'
        });
        return;
    }

    if (!validarGenero(genero)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Género debe ser H, M o O.'
        });
        return;
    }

    if (!validarCodigoPostal(codigoPostal)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Código Postal solo debe contener números.'
        });
        return;
    }

    if (!validarTelefono(telefono)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Teléfono solo debe contener números.'
        });
        return;
    }

    if (!validarEstatus(estatus)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Estatus debe ser 0 o 1.'
        });
        return;
    }

    if (!validarDisponibilidadHoraria(disponibilidadHoraria)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Disponibilidad horaria debe ser un número entre 1 y 25.'
        });
        return;
    }

    const materiasSeleccionadas = obtenerMateriasSeleccionadas();
    const diasSeleccionados = obtenerDiasSeleccionados();

    const profesor = {
        persona: {
            nombre,
            apellidoPaterno,
            apellidoMaterno,
            genero,
            fechaNacimiento,
            rfc,
            curp,
            domicilio,
            codigoPostal,
            ciudad,
            estado,
            telefono,
            email
        },
        codigo,
        disponibilidad_horaria: disponibilidadHoraria,
        estatus,
        fechaIngreso,
        materias: materiasSeleccionadas,
        disponibilidad_dias: diasSeleccionados
    };

    try {
        const response = await fetch('http://localhost:8080/classcraft/api/profesor2/insertarProfesor', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(profesor)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.result || 'Error al insertar el profesor');
        }

        const result = await response.json();
        Swal.fire({
            icon: 'success',
            title: 'Profesor insertado',
            text: result.result,
            showConfirmButton: false,
            timer: 1500
        });

        limpiarCampos(); // Limpiar los campos después de insertar
        cargarDatosProfesor(); // Recargar la tabla de registros después de insertar
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: error.message || 'Ocurrió un error al intentar insertar el profesor. Intente más tarde.'
        });
    }
}


document.querySelector('.btn-submit').addEventListener('click', insertarProfesor);

// Función para obtener las materias seleccionadas
function obtenerMateriasSeleccionadas() {
    const materias = document.querySelectorAll('#materiasContainer input[type="checkbox"]:checked');
    return Array.from(materias).map(materia => ({
        idMateria: parseInt(materia.value),
        nombre: materia.getAttribute('data-nombre')
    }));
}

function obtenerDiasSeleccionados() {
    const diasSeleccionados = [];
    document.querySelectorAll('#dias_disponiblesContainer .dia-container').forEach(container => {
        const checkbox = container.querySelector('input[type="checkbox"]');
        if (checkbox.checked) {
            const nombre = checkbox.getAttribute('data-nombre');
            const horaInicio = container.querySelector(`#horaEntrada-${checkbox.value}`).value;
            const horaFin = container.querySelector(`#horaSalida-${checkbox.value}`).value;

            // Validar el formato de la hora
            const regexHora = /^([01]\d|2[0-3]):([0-5]\d):([0-5]\d)$/;
            if (!regexHora.test(horaInicio) || !regexHora.test(horaFin)) {
                Swal.fire({
                    icon: 'error',
                    title: 'Formato de hora incorrecto',
                    text: 'Asegúrese de que las horas tengan el formato HH:mm:ss'
                });
                return;
            }

            diasSeleccionados.push({
                nombre: nombre,
                horaInicio: horaInicio,
                horaFin: horaFin
            });
        }
    });
    return diasSeleccionados;
}


// -- ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

function obtenerMateriasSeleccionadasactu() {
    const materias = document.querySelectorAll('#materiasContaineractualizar input[type="checkbox"]:checked');
    return Array.from(materias).map(materia => ({
        idMateria: parseInt(materia.value),
        nombre: materia.getAttribute('data-nombre')
    }));
}

// Función para obtener los días seleccionados
function obtenerDiasSeleccionadosactu() {
    const dias = document.querySelectorAll('#dias_disponiblesContaineractualizar input[type="checkbox"]:checked');
    return Array.from(dias).map(dia => ({
        nombre: dia.getAttribute('data-nombre')
    }));
}




function toggleMateriaDias() {
    const container = document.getElementById('materiasDiasContainer');
    if (container.style.display === 'none') {
        container.style.display = 'block';
        cargarMaterias();
        cargarDiasDisponibles();
    } else {
        container.style.display = 'none';
    }
}

// Función para mostrar u ocultar las materias y días seleccionados
async function toggleMateriaDias() {
    const result = await Swal.fire({
        title: '¿Está seguro?',
        text: "¡Modificará días y materias!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí'
    });

    if (result.isConfirmed) {
    const container = document.getElementById('materiasDiasContaineractu');
    if (container.style.display === 'none' || container.style.display === '') {
        container.style.display = 'block';
        cargarMateriasactu();
        cargarDiasDisponiblesactu();
    } else {
        container.style.display = 'none';
    }
}

}

async function actualizarProfesor() {
    const nombre = document.getElementById('txtNombre')?.value.trim();
    const apellidoPaterno = document.getElementById('txtApellidoPaterno')?.value.trim();
    const apellidoMaterno = document.getElementById('txtApellidoMaterno')?.value.trim();
    const genero = document.getElementById('txtGenero')?.value.trim();
    const fechaNacimiento = document.getElementById('txtFechaNacimiento')?.value.trim();
    const rfc = document.getElementById('RFC')?.value.trim();
    const curp = document.getElementById('CURP')?.value.trim();
    const domicilio = document.getElementById('txtDomicilio')?.value.trim();
    const codigoPostal = document.getElementById('txtCodigoPostal')?.value.trim();
    const ciudad = document.getElementById('txtCiudad')?.value.trim();
    const estado = document.getElementById('txtEstado')?.value.trim();
    const telefono = document.getElementById('txtTelefono')?.value.trim();
    const email = document.getElementById('txtEmail')?.value.trim();
    const codigo = document.getElementById('txtCodigo')?.value.trim();
    const disponibilidadHoraria = parseInt(document.getElementById('txtDisponibilidad_Horaria')?.value);
    const estatus = parseInt(document.getElementById('txtEstatus')?.value);
    const fechaIngreso = document.getElementById('txtFechaIngreso')?.value.trim();

    // Validaciones
    if (!validarTextoSinSimbolos(nombre) || !validarTextoSinSimbolos(apellidoPaterno) || !validarTextoSinSimbolos(apellidoMaterno) || !validarTextoSinSimbolos(ciudad) || !validarTextoSinSimbolos(estado)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Nombre, Apellido Paterno, Apellido Materno, Ciudad y Estado no deben contener símbolos raros.'
        });
        return;
    }

    if (!validarGenero(genero)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Género debe ser H, M o O.'
        });
        return;
    }

    if (!validarCodigoPostal(codigoPostal)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Código Postal solo debe contener números.'
        });
        return;
    }

    if (!validarTelefono(telefono)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Teléfono solo debe contener números.'
        });
        return;
    }

    if (!validarEstatus(estatus)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Estatus debe ser 0 o 1.'
        });
        return;
    }

    if (!validarDisponibilidadHoraria(disponibilidadHoraria)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Disponibilidad horaria debe ser un número entre 1 y 25.'
        });
        return;
    }

    let materiasSeleccionadas = obtenerMateriasSeleccionadasactu();

    if (materiasSeleccionadas.length === 0) {
        materiasSeleccionadas = Array.from(document.getElementById('materiasSeleccionadas')?.options || [])
            .map(option => ({ idMateria: parseInt(option.value), nombre: option.text }));
    }

    const diasDisponiblesContainerNuevo = document.getElementById('dias_disponiblesContainerNuevo');
    if (!diasDisponiblesContainerNuevo) {
        console.error('El contenedor de días disponibles no se encontró.');
        return;
    }

    const diasDisponibles = Array.from(diasDisponiblesContainerNuevo.querySelectorAll('.form-group')).map(diaDiv => {
        const nombre = diaDiv.querySelector('input[type="text"]')?.value;
        const horaInicio = diaDiv.querySelectorAll('input[type="text"]')[1]?.value;
        const horaFin = diaDiv.querySelectorAll('input[type="text"]')[2]?.value;

        return {
            nombre,
            horaInicio,
            horaFin
        };
    });

    const profesor = {
        persona: {
            nombre,
            apellidoPaterno,
            apellidoMaterno,
            genero,
            fechaNacimiento,
            rfc,
            curp,
            domicilio,
            codigoPostal,
            ciudad,
            estado,
            telefono,
            email
        },
        idProfesor: profesorSeleccionadoId,
        codigo,
        disponibilidad_horaria: disponibilidadHoraria,
        estatus,
        fechaIngreso,
        disponibilidad_dias: diasDisponibles,
        materias: materiasSeleccionadas
    };

    try {
        const response = await fetch('http://localhost:8080/classcraft/api/profesor2/actualizarProfesor', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(profesor)
        });

        if (!response.ok) {
            throw new Error('Error al actualizar el profesor');
        }

        const data = await response.json();
        Swal.fire({
            icon: 'success',
            title: 'Profesor actualizado',
            text: data.result,
            showConfirmButton: false,
            timer: 1500
        });

        cargarDatosProfesor(); // Recargar los datos de los profesores en la tabla
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Ocurrió un error al intentar actualizar el profesor. Intente más tarde.'
        });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    cargarDatosProfesor();
});

document.addEventListener('DOMContentLoaded', () => {
    const btnActualizarProfesor = document.querySelector('.btn-actualizar-profesor');
    btnActualizarProfesor.addEventListener('click', actualizarProfesor);
});


// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 //                                                                     VALIDACIONES
 function validarTextoSinSimbolos(texto) {
    const regex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
    return regex.test(texto);
}

function validarCodigoPostal(codigoPostal) {
    const regex = /^\d+$/;
    return regex.test(codigoPostal);
}

function validarTelefono(telefono) {
    const regex = /^\d+$/;
    return regex.test(telefono);
}

function validarGenero(genero) {
    const valoresValidos = ['H', 'M', 'O'];
    return valoresValidos.includes(genero);
}

function validarEstatus(estatus) {
    return estatus === 0 || estatus === 1;
}

function validarDisponibilidadHoraria(disponibilidadHoraria) {
    return disponibilidadHoraria >= 1 && disponibilidadHoraria <= 25;
}


function limpiarCampos() {
    document.getElementById('txtNombreProf').value = '';
    document.getElementById('txtApellidoPaternoProf').value = '';
    document.getElementById('txtApellidoMaternoProf').value = '';
    document.getElementById('txtGeneroProf').value = '';
    document.getElementById('txtFechaNacimiento1').value = '';
    document.getElementById('RFCProf').value = '';
    document.getElementById('CURPProf').value = '';
    document.getElementById('txtDomicilioProf').value = '';
    document.getElementById('txtCodigoPostalProf').value = '';
    document.getElementById('txtCiudadProf').value = '';
    document.getElementById('txtEstadoProf').value = '';
    document.getElementById('txtTelefonoProf').value = '';
    document.getElementById('txtEmailProf').value = '';
    document.getElementById('txtCodigoProf').value = '';
    document.getElementById('txtDisponibilidad_HorariaProf').value = '';
    document.getElementById('txtEstatusProf').value = '';
    document.getElementById('txtFechaIngresoProf').value = '';
}


// Función para cargar los días disponibles desde el servidor
function cargarDiasDisponibles1() {
    const url = 'http://localhost:8080/classcraft/api/dia_disponible/getAllDiasDisponibles';
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const diasDisponiblesContainer = document.getElementById('dias_disponiblesContainer1');
            diasDisponiblesContainer.innerHTML = '';

            if (data.length === 0) {
                const noDiasMsg = document.createElement('p');
                noDiasMsg.textContent = 'No hay días disponibles';
                diasDisponiblesContainer.appendChild(noDiasMsg);
            } else {
                const opcionesHoras = generarOpcionesHoras();

                data.forEach(dia => {
                    const diaContainer = document.createElement('div');
                    diaContainer.className = 'dia-container';
                    diaContainer.style.marginBottom = '20px';
                    diaContainer.innerHTML = `
                        <input type="checkbox" name="dia_disponible" value="${dia.idDia}" data-nombre="${dia.nombre}" id="dia-${dia.idDia}">
                        <label for="dia-${dia.idDia}">${dia.nombre}</label>
                        <div class="horarios-container" id="horarios-container-${dia.idDia}"></div>
                        <button type="button" class="btn btn-sm btn-primary" onclick="agregarHorario(${dia.idDia})">Agregar Horario</button>
                    `;
                    diasDisponiblesContainer.appendChild(diaContainer);
                    agregarHorario(dia.idDia);  // Agregar un horario inicial por defecto
                });
            }
        })
        .catch(error => console.error('Error al cargar días disponibles:', error));
}

function agregarHorario(diaId) {
    const horariosContainer = document.getElementById(`horarios-container-${diaId}`);
    const horarioDiv = document.createElement('div');
    horarioDiv.className = 'horario-div';
    horarioDiv.style.display = 'flex';
    horarioDiv.style.alignItems = 'center';
    horarioDiv.style.marginBottom = '10px';

    const opcionesHoras = generarOpcionesHoras();
    const horaEntradaSelect = crearSelectHorario(opcionesHoras, `horaEntrada-${diaId}`);
    const horaSalidaSelect = crearSelectHorario(opcionesHoras, `horaSalida-${diaId}`);

    horarioDiv.appendChild(horaEntradaSelect);
    horarioDiv.appendChild(horaSalidaSelect);

    horariosContainer.appendChild(horarioDiv);
}

function crearSelectHorario(opciones, name) {
    const select = document.createElement('select');
    select.className = 'form-control';
    select.name = name;
    select.style.marginLeft = '10px';
    opciones.forEach(hora => {
        const option = document.createElement('option');
        option.value = hora;
        option.textContent = hora;
        select.appendChild(option);
    });
    return select;
}

function obtenerDiasSeleccionados() {
    const diasSeleccionados = [];
    document.querySelectorAll('#dias_disponiblesContainer1 .dia-container').forEach(container => {
        const checkbox = container.querySelector('input[type="checkbox"]');
        if (checkbox.checked) {
            const nombre = checkbox.getAttribute('data-nombre');
            const horarios = container.querySelectorAll('.horarios-container .horario-div');
            horarios.forEach(horario => {
                const horaInicio = horario.querySelector('select[name^="horaEntrada"]').value;
                const horaFin = horario.querySelector('select[name^="horaSalida"]').value;

                diasSeleccionados.push({
                    nombre: nombre,
                    horaInicio: horaInicio,
                    horaFin: horaFin
                });
            });
        }
    });
    return diasSeleccionados;
}

async function actualizarProfesor() {
    const nombre = document.getElementById('txtNombre')?.value.trim();
    const apellidoPaterno = document.getElementById('txtApellidoPaterno')?.value.trim();
    const apellidoMaterno = document.getElementById('txtApellidoMaterno')?.value.trim();
    const genero = document.getElementById('txtGenero')?.value.trim();
    const fechaNacimiento = document.getElementById('txtFechaNacimiento')?.value.trim();
    const rfc = document.getElementById('RFC')?.value.trim();
    const curp = document.getElementById('CURP')?.value.trim();
    const domicilio = document.getElementById('txtDomicilio')?.value.trim();
    const codigoPostal = document.getElementById('txtCodigoPostal')?.value.trim();
    const ciudad = document.getElementById('txtCiudad')?.value.trim();
    const estado = document.getElementById('txtEstado')?.value.trim();
    const telefono = document.getElementById('txtTelefono')?.value.trim();
    const email = document.getElementById('txtEmail')?.value.trim();
    const codigo = document.getElementById('txtCodigo')?.value.trim();
    const disponibilidadHoraria = parseInt(document.getElementById('txtDisponibilidad_Horaria')?.value);
    const estatus = parseInt(document.getElementById('txtEstatus')?.value);
    const fechaIngreso = document.getElementById('txtFechaIngreso')?.value.trim();

    // Validaciones
    if (!validarTextoSinSimbolos(nombre) || !validarTextoSinSimbolos(apellidoPaterno) || !validarTextoSinSimbolos(apellidoMaterno) || !validarTextoSinSimbolos(ciudad) || !validarTextoSinSimbolos(estado)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Nombre, Apellido Paterno, Apellido Materno, Ciudad y Estado no deben contener símbolos raros.'
        });
        return;
    }

    if (!validarGenero(genero)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Género debe ser H, M o O.'
        });
        return;
    }

    if (!validarCodigoPostal(codigoPostal)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Código Postal solo debe contener números.'
        });
        return;
    }

    if (!validarTelefono(telefono)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Teléfono solo debe contener números.'
        });
        return;
    }

    if (!validarEstatus(estatus)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Estatus debe ser 0 o 1.'
        });
        return;
    }

    if (!validarDisponibilidadHoraria(disponibilidadHoraria)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Disponibilidad horaria debe ser un número entre 1 y 25.'
        });
        return;
    }

    let materiasSeleccionadas = obtenerMateriasSeleccionadasactu();

    if (materiasSeleccionadas.length === 0) {
        materiasSeleccionadas = Array.from(document.getElementById('materiasSeleccionadas')?.options || [])
            .map(option => ({ idMateria: parseInt(option.value), nombre: option.text }));
    }

    const diasDisponiblesContainerNuevo = document.getElementById('dias_disponiblesContainerNuevo');
    if (!diasDisponiblesContainerNuevo) {
        console.error('El contenedor de días disponibles no se encontró.');
        return;
    }

    const diasDisponibles = obtenerDiasSeleccionados(); // Cambia esta línea para obtener los días y horarios

    const profesor = {
        persona: {
            nombre,
            apellidoPaterno,
            apellidoMaterno,
            genero,
            fechaNacimiento,
            rfc,
            curp,
            domicilio,
            codigoPostal,
            ciudad,
            estado,
            telefono,
            email
        },
        idProfesor: profesorSeleccionadoId,
        codigo,
        disponibilidad_horaria: disponibilidadHoraria,
        estatus,
        fechaIngreso,
        disponibilidad_dias: diasDisponibles,
        materias: materiasSeleccionadas
    };

    try {
        const response = await fetch('http://localhost:8080/classcraft/api/profesor2/actualizarProfesor', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(profesor)
        });

        if (!response.ok) {
            throw new Error('Error al actualizar el profesor');
        }

        const data = await response.json();
        Swal.fire({
            icon: 'success',
            title: 'Profesor actualizado',
            text: data.result,
            showConfirmButton: false,
            timer: 1500
        });

        cargarDatosProfesor(); // Recargar los datos de los profesores en la tabla

        // Recargar los datos del modal con la función llenarModalProfesor
        llenarModalProfesor(profesor);

    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Ocurrió un error al intentar actualizar el profesor. Intente más tarde.'
        });
    }
}


document.addEventListener('DOMContentLoaded', () => {
    cargarDatosProfesor();
});

function toggleEditarHorario() {
    const editarHorarioContainer = document.getElementById('editarHorarioContainer');
    if (editarHorarioContainer.style.display === 'none') {
        editarHorarioContainer.style.display = 'block';
    } else {
        editarHorarioContainer.style.display = 'none';
    }
}

// Función para generar las opciones de horas
function generarOpcionesHoras() {
    const horas = [];
    for (let i = 6; i <= 22; i++) {
        const hora = i.toString().padStart(2, '0') + ':00:00';
        horas.push(hora);
    }
    return horas;
}

// Inicializar el componente
document.addEventListener('DOMContentLoaded', () => {
    cargarDiasDisponibles1();
});





// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
// Función para cargar los días disponibles desde el servidor
function cargarDiasDisponiblesNuevo() {
    const url = 'http://localhost:8080/classcraft/api/dia_disponible/getAllDiasDisponibles';
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const diasDisponiblesContainerNuevo = document.getElementById('diasDisponiblesContainerNuevo');
            diasDisponiblesContainerNuevo.innerHTML = '';

            if (data.length === 0) {
                const noDiasMsg = document.createElement('p');
                noDiasMsg.textContent = 'No hay días disponibles';
                diasDisponiblesContainerNuevo.appendChild(noDiasMsg);
            } else {
                data.forEach(dia => {
                    const diaContainerNuevo = document.createElement('div');
                    diaContainerNuevo.className = 'dia-container-nuevo';
                    diaContainerNuevo.style.marginBottom = '20px';
                    diaContainerNuevo.innerHTML = `
                        <input type="checkbox" name="dia_disponible_nuevo" value="${dia.idDia}" data-nombre="${dia.nombre}" id="dia-nuevo-${dia.idDia}">
                        <label for="dia-nuevo-${dia.idDia}">${dia.nombre}</label>
                        <div class="horariosContainerNuevo" id="horariosContainerNuevo-${dia.idDia}"></div>
                        <button type="button" class="btn btn-sm btn-primary" onclick="agregarHorarioNuevo(${dia.idDia})">Agregar Horas</button>
                    `;
                    diasDisponiblesContainerNuevo.appendChild(diaContainerNuevo);
                    agregarHorarioNuevo(dia.idDia);  // Agregar un horario inicial por defecto
                });
            }
        })
        .catch(error => console.error('Error al cargar días disponibles:', error));
}

function agregarHorarioNuevo(diaId) {
    const horariosContainerNuevo = document.getElementById(`horariosContainerNuevo-${diaId}`);
    if (!horariosContainerNuevo) {
        console.error(`No se encontró el contenedor de horarios para el día con id ${diaId}`);
        return;
    }

    const horarioDivNuevo = document.createElement('div');
    horarioDivNuevo.className = 'horario-div-nuevo';
    horarioDivNuevo.style.display = 'flex';
    horarioDivNuevo.style.alignItems = 'center';
    horarioDivNuevo.style.marginBottom = '10px';

    const opcionesHorasNuevo = generarOpcionesHorasNuevo();
    const horaEntradaSelectNuevo = crearSelectHorarioNuevo(opcionesHorasNuevo, `horaEntradaNuevo-${diaId}-${horariosContainerNuevo.children.length}`);
    const horaSalidaSelectNuevo = crearSelectHorarioNuevo(opcionesHorasNuevo, `horaSalidaNuevo-${diaId}-${horariosContainerNuevo.children.length}`);

    horarioDivNuevo.appendChild(horaEntradaSelectNuevo);
    horarioDivNuevo.appendChild(horaSalidaSelectNuevo);

    horariosContainerNuevo.appendChild(horarioDivNuevo);
}

function crearSelectHorarioNuevo(opciones, name) {
    const select = document.createElement('select');
    select.className = 'form-control';
    select.name = name;
    select.style.marginLeft = '10px';
    opciones.forEach(hora => {
        const option = document.createElement('option');
        option.value = hora;
        option.textContent = hora;
        select.appendChild(option);
    });
    return select;
}

function generarOpcionesHorasNuevo() {
    const horas = [];
    for (let i = 6; i <= 22; i++) {
        const hora = i.toString().padStart(2, '0') + ':00:00';
        horas.push(hora);
    }
    return horas;
}

function obtenerDiasSeleccionadosNuevo() {
    const diasSeleccionadosNuevo = [];
    document.querySelectorAll('#diasDisponiblesContainerNuevo .dia-container-nuevo').forEach(container => {
        const checkbox = container.querySelector('input[type="checkbox"]');
        if (checkbox.checked) {
            const nombre = checkbox.getAttribute('data-nombre');
            const horarios = container.querySelectorAll('.horariosContainerNuevo .horario-div-nuevo');
            horarios.forEach(horario => {
                const horaInicio = horario.querySelector('select[name^="horaEntradaNuevo"]').value;
                const horaFin = horario.querySelector('select[name^="horaSalidaNuevo"]').value;

                diasSeleccionadosNuevo.push({
                    nombre: nombre,
                    horaInicio: horaInicio,
                    horaFin: horaFin
                });
            });
        }
    });
    return diasSeleccionadosNuevo;
}

async function insertarProfesor() {
    const nombre = document.getElementById('txtNombreProf').value.trim();
    const apellidoPaterno = document.getElementById('txtApellidoPaternoProf').value.trim();
    const apellidoMaterno = document.getElementById('txtApellidoMaternoProf').value.trim();
    const genero = document.getElementById('txtGeneroProf').value.trim();
    const fechaNacimiento = document.getElementById('txtFechaNacimiento1').value.trim();
    const rfc = document.getElementById('RFCProf').value.trim();
    const curp = document.getElementById('CURPProf').value.trim();
    const domicilio = document.getElementById('txtDomicilioProf').value.trim();
    const codigoPostal = document.getElementById('txtCodigoPostalProf').value.trim();
    const ciudad = document.getElementById('txtCiudadProf').value.trim();
    const estado = document.getElementById('txtEstadoProf').value.trim();
    const telefono = document.getElementById('txtTelefonoProf').value.trim();
    const email = document.getElementById('txtEmailProf').value.trim();
    const codigo = document.getElementById('txtCodigoProf').value.trim();
    const disponibilidadHoraria = parseInt(document.getElementById('txtDisponibilidad_HorariaProf').value);
    const estatus = parseInt(document.getElementById('txtEstatusProf').value);
    const fechaIngreso = document.getElementById('txtFechaIngresoProf').value.trim();

    // Validaciones
    if (!validarTextoSinSimbolos(nombre) || !validarTextoSinSimbolos(apellidoPaterno) || !validarTextoSinSimbolos(apellidoMaterno) || !validarTextoSinSimbolos(ciudad) || !validarTextoSinSimbolos(estado)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Nombre, Apellido Paterno, Apellido Materno, Ciudad y Estado no deben contener símbolos raros.'
        });
        return;
    }

    if (!validarGenero(genero)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Género debe ser H, M o O.'
        });
        return;
    }

    if (!validarCodigoPostal(codigoPostal)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Código Postal solo debe contener números.'
        });
        return;
    }

    if (!validarTelefono(telefono)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Teléfono solo debe contener números.'
        });
        return;
    }

    if (!validarEstatus(estatus)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Estatus debe ser 0 o 1.'
        });
        return;
    }

    if (!validarDisponibilidadHoraria(disponibilidadHoraria)) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Disponibilidad horaria debe ser un número entre 1 y 25.'
        });
        return;
    }

    const materiasSeleccionadas = obtenerMateriasSeleccionadas();
    const diasSeleccionados = obtenerDiasSeleccionadosNuevo();

    const profesor = {
        persona: {
            nombre,
            apellidoPaterno,
            apellidoMaterno,
            genero,
            fechaNacimiento,
            rfc,
            curp,
            domicilio,
            codigoPostal,
            ciudad,
            estado,
            telefono,
            email
        },
        codigo,
        disponibilidad_horaria: disponibilidadHoraria,
        estatus,
        fechaIngreso,
        materias: materiasSeleccionadas,
        disponibilidad_dias: diasSeleccionados
    };

    try {
        const response = await fetch('http://localhost:8080/classcraft/api/profesor2/insertarProfesor', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(profesor)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.result || 'Error al insertar el profesor');
        }

        const result = await response.json();
        Swal.fire({
            icon: 'success',
            title: 'Profesor insertado',
            text: result.result,
            showConfirmButton: false,
            timer: 1500
        });

        limpiarCampos(); // Limpiar los campos después de insertar
        cargarDatosProfesor(); // Recargar la tabla de registros después de insertar
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: error.message || 'Ocurrió un error al intentar insertar el profesor. Intente más tarde.'
        });
    }
}

// Inicializar el componente
document.addEventListener('DOMContentLoaded', () => {
    cargarDiasDisponiblesNuevo();
});
