let profesorSeleccionadoId = null;

// Función para seleccionar un profesor al hacer clic en la fila de la tabla
function seleccionarProfesor(idProfesor) {
    profesorSeleccionadoId = idProfesor;
    cargarDatosProfesorPorId(idProfesor);
}

// Función para cargar los datos de los profesores desde el servidor
async function cargarDatosProfesor() {
    try {
        const response = await fetch('http://localhost:8080/ClassCraft/api/profesor/getAllProfesores');
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
            const response = await fetch('http://localhost:8080/ClassCraft/api/profesor/desactivarProfesor', {
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
            const response = await fetch('http://localhost:8080/ClassCraft/api/profesor/activarProfesor', {
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
        const response = await fetch('http://localhost:8080/ClassCraft/api/profesor/buscarProfesor', {
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
    const url = 'http://localhost:8080/ClassCraft/api/materia/getAll?limit=10&skip=0';
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
                    
                    const label = document.createElement('label');
                    label.htmlFor = checkbox.id;
                    label.textContent = materia.nombre;
                    
                    const container = document.createElement('div');
                    container.appendChild(checkbox);
                    container.appendChild(label);
                    
                    materiasContainer.appendChild(container);
                });
            }
        })
        .catch(error => console.error('Error al cargar materias:', error));
}

// Función para cargar los días disponibles desde el servidor
function cargarDiasDisponibles() {
    const url = 'http://localhost:8080/ClassCraft/api/dia_disponible/getAllDiasDisponibles';
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const diasDisponiblesContainer = document.getElementById('dias_disponiblesContainer');
            diasDisponiblesContainer.innerHTML = '';
            
            if (data.length === 0) {
                const noDiasMsg = document.createElement('p');
                noDiasMsg.textContent = 'No hay días disponibles';
                diasDisponiblesContainer.appendChild(noDiasMsg);
            } else {
                data.forEach(dia => {
                    const checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = dia.idDia;
                    checkbox.id = `dia-${dia.idDia}`;
                    
                    const label = document.createElement('label');
                    label.htmlFor = checkbox.id;
                    label.textContent = dia.nombre;
                    
                    const container = document.createElement('div');
                    container.appendChild(checkbox);
                    container.appendChild(label);
                    
                    diasDisponiblesContainer.appendChild(container);
                });
            }
        })
        .catch(error => console.error('Error al cargar días disponibles:', error));
}

document.addEventListener('DOMContentLoaded', function() {
    cargarMaterias();
    cargarDiasDisponibles();
});


async function cargarDatosProfesorPorId(idProfesor) {
    try {
        const response = await fetch('http://localhost:8080/ClassCraft/api/profesor/getProfesorById', {
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

/// Función para llenar el modal 3 con los datos del profesor
function llenarModalProfesor(profesor) {
    document.getElementById('txtNombre').value = profesor.persona.nombre;
    document.getElementById('txtApellidoPaterno').value = profesor.persona.apellidoPaterno;
    document.getElementById('txtApellidoMaterno').value = profesor.persona.apellidoMaterno;
    document.getElementById('txtGenero').value = profesor.persona.genero;
    document.getElementById('txtFechaNacimiento').value = profesor.persona.fechaNacimiento;
    document.getElementById('RFC').value = profesor.persona.rfc;
    document.getElementById('CURP').value = profesor.persona.curp;
    document.getElementById('txtDomicilio').value = profesor.persona.domicilio;
    document.getElementById('txtCodigoPostal').value = profesor.persona.codigoPostal;
    document.getElementById('txtCiudad').value = profesor.persona.ciudad;
    document.getElementById('txtEstado').value = profesor.persona.estado;
    document.getElementById('txtTelefono').value = profesor.persona.telefono;
    document.getElementById('txtEmail').value = profesor.persona.email;
    document.getElementById('txtCodigo').value = profesor.codigo;
    document.getElementById('txtDisponibilidad_Horaria').value = profesor.disponibilidad_horaria;
    document.getElementById('txtEstatus').value = profesor.estatus;
    document.getElementById('txtFechaIngreso').value = profesor.fechaIngreso;

    // Llenar las materias seleccionadas
    const materiasSeleccionadas = document.getElementById('materiasSeleccionadas');
    materiasSeleccionadas.innerHTML = '';

    const materiasSet = new Set();
    profesor.materias.forEach(materia => {
        materiasSet.add(JSON.stringify(materia));
    });

    materiasSet.forEach(materiaStr => {
        const materia = JSON.parse(materiaStr);
        const option = document.createElement('option');
        option.textContent = materia.nombre;
        option.value = materia.idMateria;
        materiasSeleccionadas.appendChild(option);
    });

    // Llenar los días seleccionados
    const diasSeleccionados = document.getElementById('diasSeleccionados');
    diasSeleccionados.innerHTML = '';

    const diasSet = new Set();
    profesor.disponibilidad_dias.forEach(dia => {
        diasSet.add(JSON.stringify(dia));
    });

    diasSet.forEach(diaStr => {
        const dia = JSON.parse(diaStr);
        const option = document.createElement('option');
        option.textContent = dia.nombre;
        option.value = dia.idDia;
        diasSeleccionados.appendChild(option);
    });
}



// JavaScript

// Función para obtener las materias seleccionadas
function obtenerMateriasSeleccionadas() {
    const materias = document.querySelectorAll('#materiasContainer input[type="checkbox"]:checked');
    return Array.from(materias).map(materia => ({
        idMateria: parseInt(materia.value),
        nombre: materia.getAttribute('data-nombre')
    }));
}

// Función para obtener los días seleccionados
function obtenerDiasSeleccionados() {
    const dias = document.querySelectorAll('#dias_disponiblesContainer input[type="checkbox"]:checked');
    return Array.from(dias).map(dia => ({
        idDia: parseInt(dia.value),
        nombre: dia.getAttribute('data-nombre')
    }));
}

// Función para insertar profesor
async function insertarProfesor() {
    const nombre = document.getElementById('txtNombre').value.trim();
    const apellidoPaterno = document.getElementById('txtApellidoPaterno').value.trim();
    const apellidoMaterno = document.getElementById('txtApellidoMaterno').value.trim();
    const genero = document.getElementById('txtGenero').value.trim();
    const fechaNacimiento = document.getElementById('txtFechaNacimiento1').value.trim();
    const rfc = document.getElementById('RFC').value.trim();
    const curp = document.getElementById('CURP').value.trim();
    const domicilio = document.getElementById('txtDomicilio').value.trim();
    const codigoPostal = document.getElementById('txtCodigoPostal').value.trim();
    const ciudad = document.getElementById('txtCiudad').value.trim();
    const estado = document.getElementById('txtEstado').value.trim();
    const telefono = document.getElementById('txtTelefono').value.trim();
    const email = document.getElementById('txtEmail').value.trim();
    const codigo = document.getElementById('txtCodigo').value.trim();
    const disponibilidadHoraria = parseInt(document.getElementById('txtDisponibilidad_Horaria').value);
    const estatus = parseInt(document.getElementById('txtEstatus').value);
    const fechaIngreso = document.getElementById('txtFechaIngreso').value.trim();

    // Depuración de valores
    console.log({
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
        email,
        codigo,
        disponibilidadHoraria,
        estatus,
        fechaIngreso
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
        codigo,
        disponibilidad_horaria: disponibilidadHoraria,
        estatus,
        fechaIngreso,
        materias: obtenerMateriasSeleccionadas(),
        disponibilidad_dias: obtenerDiasSeleccionados()
    };

    try {
        const response = await fetch('http://localhost:8080/ClassCraft/api/profesor/insertarProfesor', {
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