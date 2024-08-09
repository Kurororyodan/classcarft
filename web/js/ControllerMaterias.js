let currentPage = 1;
let recordsPerPage = 10;
let materiaSeleccionada = null;

document.addEventListener("DOMContentLoaded", function () {
    cargarMateria(currentPage, recordsPerPage);
    document.querySelectorAll(".btn-submit").forEach(button => {
        button.addEventListener("click", ingresarMateria);
    });
});

function eliminarMateria() {
    Swal.fire({
        title: '¿Estás seguro?',
        text: "¿Deseas eliminar esta materia?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            let url = "http://localhost:8080/Estadias/api/materia/deleteMateria";
            let v_id = materiaSeleccionada.idMateria;  // Asegúrate de que es `idMateria` y no `idmateria`

            let materia = {
                idMateria: v_id
            };
            console.log("materia a enviar al servidor: ", materia);

            const requestOptions = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    materia: JSON.stringify(materia)
                })
            };

            fetch(url, requestOptions)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Error en la respuesta del servidor');
                    }
                    return response.json();
                })
                .then(json => {
                    console.log(json);
                    if (json.result === "Objeto eliminado") {
                        Swal.fire({
                            title: 'Materia Eliminado',
                            text: 'La materia ha sido eliminada exitosamente.',
                            icon: 'success'
                        }).then(() => {
                            $('#editModal').modal('hide');
                            cargarMateria(currentPage, recordsPerPage);
                        });
                    } else {
                        Swal.fire({
                            title: 'Error',
                            text: 'Hubo un error al eliminar la materia',
                            icon: 'error'
                        });
                    }
                })
                .catch(error => {
                    console.error('Error al eliminar materia:', error);
                    Swal.fire({
                        title: 'Error',
                        text: 'Hubo un error al eliminar la materia',
                        icon: 'error'
                    });
                });
        }
    });
}

function UpdateMateria() {
    let url = "http://localhost:8080/Estadias/api/materia/UpdateMateria";
    let v_id, v_nombre, v_clave, v_horas;

    // Obtener valores de los campos
    v_id = materiaSeleccionada.idMateria;
    v_nombre = document.getElementById("editNombreMateria").value;
    v_clave = document.getElementById("editClave").value;
    v_horas = document.getElementById("editHoras").value;

    // Crear objeto materia
    let materia = {
        idMateria: v_id,
        nombre: v_nombre,
        clave: v_clave,
        horas: v_horas
    };

    console.log("materia a enviar al servidor: ", materia);

    // Configurar opciones de la solicitud
    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            materia: JSON.stringify(materia)
        })
    };

    // Realizar la solicitud
    fetch(url, requestOptions)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            if (data.result === "Objeto actualizado") {
                Swal.fire({
                    icon: 'success',
                    title: 'Materia actualizada',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    $('#editModal').modal('hide');
                    cargarMateria(currentPage, recordsPerPage);
                });
            } else {
                Swal.fire({
                    title: 'Error',
                    text: 'Error al actualizar la materia',
                    icon: 'error'
                });
            }
        })
        .catch(error => {
            console.error('Error al actualizar materia:', error);
            Swal.fire({
                title: 'Error',
                text: 'Error al actualizar la materia',
                icon: 'error'
            });
        });
}

function limpiar() {
    document.getElementById("txtNombreMateria").value = "";
    document.getElementById("txtClave").value = "";
    document.getElementById("txtHoras").value = "";
}

function cargarMateria(page, pageSize) {
    const skip = pageSize * (page - 1);
    const url = `http://localhost:8080/Estadias/api/materia/getAll?limit=${pageSize}&skip=${skip}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log('Respuesta JSON:', data);
            actualizaMateria(data);
            const rangeInfo = `Mostrando ${skip + 1} a ${skip + data.length} materias`;
            document.getElementById('rango-registros').textContent = rangeInfo;
        })
        .catch(error => console.error('Error al obtener productos:', error));
}


function editarMateria(idMateria) {
    const row = document.querySelector(`#materia-table-body tr[data-id="${idMateria}"]`);
    if (!row) {
        console.error('Error: fila no encontrada.');
        return;
    }

    const nombre = row.cells[1].textContent;
    const clave = row.cells[2].textContent;
    const horas = row.cells[3].textContent;

    document.getElementById('editNombreMateria').value = nombre;
    document.getElementById('editClave').value = clave;
    document.getElementById('editHoras').value = horas;

    materiaSeleccionada = { idMateria: idMateria };

    $('#editModal').modal('show');
}

function cambiarPaginaMateria(delta) {
    currentPage += delta;
    if (currentPage < 1) {
        currentPage = 1;
    }
    cargarMateria(currentPage, recordsPerPage);
}

function mostrarDetalleMateria(idMateria) {
    const url = `http://localhost:8080/Estadias/api/materia/getMateriaDetails?id=${idMateria}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error al obtener detalles de la getMateriaDetails. Código: ${response.status}`);
            }
            return response.json();
        })
        .then(materia => {
            materiaSeleccionada = materia;
            console.log('Detalles de la materia:', materia);

            document.getElementById('editNombreMateria').value = materia.nombre;
            document.getElementById('editClave').value = materia.clave;
            document.getElementById('editHoras').value = materia.horas;
        })
        .catch(error => {
            console.error(`Error: ${error.message}`);
        });
}

function buscarMateria() {
    const query = document.getElementById('searchInput').value.trim();

    if (!query) {
        Swal.fire('Error', 'Debe ingresar un término de búsqueda', 'error');
        return;
    }

    const data = new URLSearchParams();
    data.append('query', query);

    fetch('http://localhost:8080/ClassCraft/api/materia/buscarMateria', {
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
    .then(materias => {
        actualizaMateria(materias); // Función que actualiza la tabla con los resultados
    })
    .catch(error => {
        console.error('Error al buscar materias:', error);
        Swal.fire('Error', 'Hubo un error al buscar las materias', 'error');
    });
}

function actualizaMateria(materias) {
    const tbody = document.getElementById('materia-table-body');
    if (!tbody) {
        console.error('Error: tbody no encontrado.');
        return;
    }

    tbody.innerHTML = '';
    materias.forEach(item => {
        const row = tbody.insertRow();
        row.dataset.id = item.idMateria;
        row.addEventListener('click', () => mostrarDetalleMateria(item.idMateria));
        row.insertCell(0).textContent = item.idMateria;
        row.insertCell(1).textContent = item.nombre;
        row.insertCell(2).textContent = item.clave;
        row.insertCell(3).textContent = item.horas;

        const editButtonCell = row.insertCell(4);
        const editButton = document.createElement('button');
        editButton.innerHTML = '<i class="bi bi-pencil-square"></i>';
        editButton.classList.add('btn', 'btn-sm', 'btn-primary');
        editButton.addEventListener('click', (event) => {
            event.stopPropagation();
            editarMateria(item.idMateria);
        });
        editButtonCell.appendChild(editButton);
    });
}


function recargarPagina() {
    location.reload();
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

function ingresarMateria(event) {
    event.preventDefault(); // Prevenir el comportamiento predeterminado del formulario

    let v_nombre = document.getElementById("txtNombreMateria").value;
    let v_clave = document.getElementById("txtClave").value;
    let v_horas = document.getElementById("txtHoras").value;

    // Asegúrate de que los campos no estén vacíos
    if (!v_nombre || !v_clave || !v_horas) {
        Swal.fire({
            title: 'Error',
            text: 'Todos los campos son obligatorios',
            icon: 'error'
        });
        return;
    }

    let materia = {
        nombre: v_nombre,
        clave: v_clave,
        horas: v_horas
    };

    let materiaJson = JSON.stringify(materia);

    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ materia: materiaJson })
    };

    fetch("http://localhost:8080/Estadias/api/materia/insertMateria", requestOptions)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }
            return response.json();
        })
        .then(json => {
            console.log(json);
            Swal.fire({
                title: 'Materia Registrada',
                text: `${v_nombre}`,
                icon: 'success'
            }).then(() => {
                limpiar();
                resetSteps();
                cargarMateria(currentPage, recordsPerPage);
            });
        })
        .catch(error => {
            console.error('Error al registrar materia:', error);
            Swal.fire({
                title: 'Error',
                text: 'Hubo un error al registrar la materia',
                icon: 'error'
            });
        });
}

function limpiar() {
    document.getElementById("txtNombreMateria").value = "";
    document.getElementById("txtClave").value = "";
    document.getElementById("txtHoras").value = "";
}
