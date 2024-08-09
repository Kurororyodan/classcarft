let todasMaterias = []; // Arreglo para almacenar todas las materias
let currentPage = 1;
let recordsPerPage = 10;
let grupoSeleccionado = {};

document.addEventListener('DOMContentLoaded', function() {
    cargarTodasMaterias(); // Cargar todas las materias al cargar la página
    cargarModalidades(); 
    cargarGrupo(currentPage, recordsPerPage);
});

function cargarTodasMaterias() {
    fetch('http://localhost:8080/classcraft/api/materia/getAll?limit=1000&skip=0') // Ajusta los parámetros si es necesario
        .then(response => response.json())
        .then(data => {
            todasMaterias = data; // Almacenar todas las materias en el arreglo
        })
        .catch(error => console.error('Error al cargar materias:', error));
}

function buscarMaterias() {
    const searchInput = document.getElementById('materiasSearch').value.toLowerCase();
    const seleccionadas = Array.from(document.getElementById('listaMateriasSeleccionadas').children)
        .map(li => parseInt(li.dataset.id)); // Obtener IDs de materias seleccionadas

    const resultados = todasMaterias.filter(materia =>
        materia.nombre.toLowerCase().includes(searchInput) &&
        !seleccionadas.includes(materia.idMateria) // Filtrar materias ya seleccionadas
    );

    mostrarResultadosBusqueda(resultados);
}


function mostrarResultadosBusqueda(resultados) {
    const resultadosContainer = document.getElementById('searchResults');
    resultadosContainer.innerHTML = ''; // Limpiar resultados anteriores

    if (resultados.length === 0) {
        resultadosContainer.innerHTML = '<p>No se encontraron materias</p>';
    } else {
        resultados.forEach(materia => {
            const div = document.createElement('div');
            div.classList.add('autocomplete-item');
            div.textContent = materia.nombre;
            div.onclick = () => seleccionarMateria(materia);
            resultadosContainer.appendChild(div);
        });
    }
}

function seleccionarMateria(materia) {
    const lista = document.getElementById('listaMateriasSeleccionadas');
    const listItem = document.createElement('li');
    listItem.textContent = materia.nombre;
    listItem.dataset.id = materia.idMateria;

    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = 'X';
    deleteBtn.classList.add('delete-btn'); // Añadir clase delete-btn
    deleteBtn.onclick = () => {
        listItem.remove();
        buscarMaterias(); // Actualizar resultados de búsqueda al eliminar una materia seleccionada
    };

    listItem.appendChild(deleteBtn);
    lista.appendChild(listItem);

    // Limpiar campo de búsqueda y resultados
    document.getElementById('materiasSearch').value = '';
    document.getElementById('searchResults').innerHTML = '';
}




function cargarModalidades() {
    fetch('http://localhost:8080/classcraft/api/modalidad/getAllModalidades')
        .then(response => response.json())
        .then(data => {
            const selectModalidad = document.getElementById('selectModalidad');
            selectModalidad.innerHTML = ''; 
            data.forEach(modalidad => {
                const option = document.createElement('option');
                option.value = modalidad.idModalidad;
                option.text = modalidad.nombre;
                selectModalidad.appendChild(option);
            });
        })
        .catch(error => console.error('Error al cargar modalidades:', error));
}
// Modal functionality
const modal = document.getElementById("myModal");
const btn = document.getElementById("openModal");
const span = document.getElementsByClassName("close")[0];

btn.onclick = function() {
    resetSteps();
    limpiar();
    modal.style.display = "block";
}

span.onclick = function() {
    modal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// Form steps functionality
const nextButton = document.querySelector('.btn-next');
const prevButton = document.querySelector('.btn-prev');
const steps = document.querySelectorAll('.step');
const form_steps = document.querySelectorAll('.form-step');
const submitButton = document.querySelector('.btn-submit');
let active = 1;

nextButton.addEventListener('click', () => {
    active++;
    if (active > steps.length) {
        active = steps.length;
    }
    updateProgress();
});

prevButton.addEventListener('click', () => {
    active--;
    if (active < 1) {
        active = 1;
    }
    updateProgress();
});

const updateProgress = () => {
    steps.forEach((step, i) => {
        if (i === (active - 1)) {
            step.classList.add('active');
            form_steps[i].classList.add('active');
        } else {
            step.classList.remove('active');
            form_steps[i].classList.remove('active');
        }
    });

    if (active === 1) {
        prevButton.disabled = true;
        nextButton.style.display = 'block';
        submitButton.style.display = 'none';
    } else if (active === steps.length) {
        nextButton.style.display = 'none';
        submitButton.style.display = 'block';
        prevButton.disabled = false;
    } else {
        prevButton.disabled = false;
        nextButton.style.display = 'block';
        submitButton.style.display = 'none';
    }
};

// Initialize progress on page load
updateProgress();

function resetSteps() {
    // Reiniciar pasos del progreso
    active = 1; // Resetear a primer paso
    updateProgress(); // Actualizar progreso
}

function ingresarGrupo(event) {
    event.preventDefault();

    const nombre = document.getElementById("txtNombreGrupo").value.trim();
    const capacidad = parseInt(document.getElementById("txtCapacidad").value.trim());
    const modalidadSelect = document.getElementById("selectModalidad");
    const modalidad = modalidadSelect.options[modalidadSelect.selectedIndex].text;
    const materiasSeleccionadas = Array.from(document.getElementById('listaMateriasSeleccionadas').children)
        .map(li => li.dataset.id);

    console.log("Datos a enviar:");
    console.log("nombreGrupo:", nombre);
    console.log("capacidad:", capacidad);
    console.log("modalidad:", modalidad);
    console.log("idMaterias:", materiasSeleccionadas.join(","));

    if (nombre === "" || isNaN(capacidad) || capacidad <= 0 || modalidad === "" || materiasSeleccionadas.length === 0) {
        Swal.fire('Error', 'Todos los campos son obligatorios y deben ser válidos', 'error');
        return;
    }

    if (capacidad > 40) {
        Swal.fire('Error', 'La capacidad máxima es de 40', 'error');
        return;
    }

    const data = new URLSearchParams();
    data.append('nombreGrupo', nombre);
    data.append('capacidad', capacidad);
    data.append('modalidad', modalidad);
    data.append('idMaterias', materiasSeleccionadas.join(","));

    fetch('http://localhost:8080/classcraft/api/Grupo/insertGrupo', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data.toString()
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        if (data.result === "Grupo insertado exitosamente") {
            Swal.fire('Grupo registrado', '', 'success').then(() => {
                limpiar(); // Limpiar el formulario después de insertar
                resetSteps(); // Volver al paso 1
                cargarGrupo(currentPage, recordsPerPage); // Recargar los grupos
            });
        } else {
            Swal.fire('Error al registrar grupo', data.error || 'Error desconocido', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        Swal.fire('Error al registrar grupo', error.message, 'error');
    });
}

function limpiar() {
    document.getElementById("txtNombreGrupo").value = "";
    document.getElementById("txtCapacidad").value = "";
    document.getElementById("materiasSearch").value = "";
    document.getElementById("searchResults").innerHTML = "";
    document.getElementById("listaMateriasSeleccionadas").innerHTML = "";
    document.getElementById("selectModalidad").value = "";
}

function cerrarModal() {
    var modal = document.getElementById("myModal");
    modal.style.display = "none";
}

window.onclick = function(event) {
    var modal = document.getElementById("myModal");
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

document.querySelector(".close").onclick = function() {
    var modal = document.getElementById("myModal");
    modal.style.display = "none";
}

function cargarGrupo(page, pageSize) {
    const skip = pageSize * (page - 1);
    const url = `http://localhost:8080/classcraft/api/Grupo/getAll?limit=${pageSize}&skip=${skip}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log('Respuesta JSON:', data);
            actualizaGrupo(data);
            const rangeInfo = `Mostrando ${skip + 1} a ${skip + data.length} grupos`;
            document.getElementById('rango-registros').textContent = rangeInfo;
        })
        .catch(error => console.error('Error al obtener grupos:', error));
}

function actualizaGrupo(grupos) {
    const tbody = document.getElementById('grupo-table-body');
    if (!tbody) {
        console.error('Error: tbody no encontrado.');
        return;
    } 

    tbody.innerHTML = '';
    grupos.forEach(item => {
        const row = tbody.insertRow();
        row.dataset.id = item.id;
        row.insertCell(0).textContent = item.id;
        row.insertCell(1).textContent = item.nombreGrupo;
        row.insertCell(2).textContent = item.capacidad;
        
        const actionCell = row.insertCell(3);

        const editButton = document.createElement('button');
        editButton.innerHTML = '<i class="bi bi-pencil-square"></i>';
        editButton.classList.add('btn', 'btn-sm', 'btn-primary', 'mr-2');
        editButton.addEventListener('click', (event) => {
            event.stopPropagation();
            editarGrupo(item.id);
        });

        const viewButton = document.createElement('button');
        viewButton.innerHTML = '<i class="bi bi-eye"></i>';
        viewButton.classList.add('btn', 'btn-sm', 'btn-secondary');
        viewButton.addEventListener('click', (event) => {
            event.stopPropagation();
            mostrarDetalleGrupo(item.id);
        });

        actionCell.appendChild(editButton);
        actionCell.appendChild(viewButton);
    });
}

function editarGrupo(idGrupo) {
    const row = document.querySelector(`#grupo-table-body tr[data-id="${idGrupo}"]`);
    if (!row) {
        console.error('Error: fila no encontrada.');
        return;
    }

    const nombre = row.cells[1].textContent;
    const capacidad = row.cells[2].textContent;

    document.getElementById('editNombreGrupo').value = nombre;
    document.getElementById('editCapacidad').value = capacidad;

    grupoSeleccionado = { idGrupo: idGrupo };

    $('#editModal').modal('show');
}

function eliminarGrupo() {
    Swal.fire({
        title: '¿Estás seguro?',
        text: "¡No podrás revertir esto!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminarlo',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            const idGrupo = grupoSeleccionado.idGrupo;

            fetch('http://localhost:8080/classcraft/api/Grupo/deleteGrupo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({ idGrupo: idGrupo }).toString()
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                if (data.result) {
                    Swal.fire("Éxito", data.result, "success").then(() => {
                        $('#editModal').modal('hide');
                        cargarGrupo(currentPage, recordsPerPage); // Recargar la lista de grupos después de eliminar
                    });
                } else {
                    Swal.fire("Error", data.error || "Error en la transacción", "error");
                }
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire("Error", "No se pudo eliminar el grupo", "error");
            });
        }
    });
}






function cambiarPaginaGrupo(delta) {
    currentPage += delta;
    if (currentPage < 1) {
        currentPage = 1;
    }
    cargarGrupo(currentPage, recordsPerPage);
}
function mostrarDetalleGrupo(idGrupo) {
    const url = `http://localhost:8080/classcraft/api/Grupo/getGrupoDetails?id=${idGrupo}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error al obtener detalles del grupo. Código: ${response.status}`);
            }
            return response.json();
        })
        .then(grupo => {
            document.getElementById('viewNombreGrupo').textContent = grupo.nombreGrupo;
            document.getElementById('viewCapacidad').textContent = grupo.capacidad;
            document.getElementById('viewModalidad').textContent = grupo.modalidad;
            
            // Mostrar las materias seleccionadas
            const viewMateriasElement = document.getElementById('viewMaterias');
            viewMateriasElement.innerHTML = ''; // Limpiar contenido anterior
            grupo.materias.forEach(materia => {
                const materiaElement = document.createElement('div');
                materiaElement.textContent = materia.nombre;
                viewMateriasElement.appendChild(materiaElement);
            });

            $('#viewModal').modal('show');
        })
        .catch(error => {
            console.error(`Error: ${error.message}`);
        });
}

function buscarGrupo() {
    const query = document.getElementById('searchInput').value;

    const data = new URLSearchParams();
    data.append('query', query);

    fetch('http://localhost:8080/classcraft/api/Grupo/buscarGrupo', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: data.toString()
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log(data);
        actualizaGrupo(data); // Llamada a tu función actualizaGrupo con los datos recibidos
    })
    .catch(error => {
        console.error('Error al buscar grupos:', error);
    });
}

function recargarPagina() {
    location.reload();
}



function updateGrupo() {
    const nombreGrupo = document.getElementById('editNombreGrupo').value.trim();
    const capacidad = parseInt(document.getElementById('editCapacidad').value.trim());

    if (nombreGrupo === "" || isNaN(capacidad) || capacidad <= 0) {
        Swal.fire('Error', 'Todos los campos son obligatorios y deben ser válidos', 'error');
        return;
    }

    if (!grupoSeleccionado.idGrupo) {
        Swal.fire('Error', 'No se ha seleccionado ningún grupo para actualizar', 'error');
        return;
    }

    Swal.fire({
        title: '¿Estás seguro?',
        text: "¡Se actualizarán los datos del grupo!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, actualizar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            const data = new URLSearchParams();
            data.append('idGrupo', grupoSeleccionado.idGrupo);
            data.append('nombreGrupo', nombreGrupo);
            data.append('capacidad', capacidad);

            fetch('http://localhost:8080/classcraft/api/Grupo/updateGrupo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: data.toString()
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                if (data.result === "Grupo actualizado exitosamente") {
                    Swal.fire('Grupo actualizado', '', 'success');
                    $('#editModal').modal('hide');
                    cargarGrupo(currentPage, recordsPerPage); // Recargar la lista de grupos
                } else {
                    Swal.fire('Error al actualizar grupo', data.error || 'Error desconocido', 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire('Error al actualizar grupo', error.message, 'error');
            });
        }
    });
}



