let currentPage = 1;
let recordsPerPage = 10;
let salonSeleccionada = null;

document.addEventListener("DOMContentLoaded", function () {
    cargarSalon(currentPage, recordsPerPage);
    document.querySelectorAll(".btn-submit").forEach(button => {
        button.addEventListener("click", ingresarSalon);
    });
});

function eliminarSalon() {
    Swal.fire({
        title: '¿Estás seguro?',
        text: "¿Deseas eliminar este salón?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            let url = "http://localhost:8080/Estadias/api/salon/deleteSalon";
            let v_id = salonSeleccionada.idSalon;

            let salon = {
                idSalon: v_id
            };
            console.log("salón a enviar al servidor: ", salon);

            const requestOptions = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    salon: JSON.stringify(salon)
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
                            title: 'Salón Eliminado',
                            text: 'El salón ha sido eliminado exitosamente.',
                            icon: 'success'
                        }).then(() => {
                            $('#editModal').modal('hide');
                            cargarSalon(currentPage, recordsPerPage);
                        });
                    } else {
                        Swal.fire({
                            title: 'Error',
                            text: 'Hubo un error al eliminar el salón',
                            icon: 'error'
                        });
                    }
                })
                .catch(error => {
                    console.error('Error al eliminar salón:', error);
                    Swal.fire({
                        title: 'Error',
                        text: 'Hubo un error al eliminar el salón',
                        icon: 'error'
                    });
                });
        }
    });
}

function UpdateSalon() {
    let url = "http://localhost:8080/Estadias/api/salon/UpdateSalon";
    let v_id, v_nombre, v_ubicacion;

    // Obtener valores de los campos
    v_id = salonSeleccionada.idSalon;
    v_nombre = document.getElementById("editNombreSalon").value;
    v_ubicacion = document.getElementById("editUbicacion").value;

    // Crear objeto salón
    let salon = {
        idSalon: v_id,
        nombre: v_nombre,
        ubicacion: v_ubicacion
    };

    console.log("salón a enviar al servidor: ", salon);

    // Configurar opciones de la solicitud
    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            salon: JSON.stringify(salon)
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
                    title: 'Salón actualizado',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    $('#editModal').modal('hide');
                    cargarSalon(currentPage, recordsPerPage);
                });
            } else {
                Swal.fire({
                    title: 'Error',
                    text: 'Error al actualizar el salón',
                    icon: 'error'
                });
            }
        })
        .catch(error => {
            console.error('Error al actualizar salón:', error);
            Swal.fire({
                title: 'Error',
                text: 'Error al actualizar el salón',
                icon: 'error'
            });
        });
}

function limpiar() {
    document.getElementById("txtNombreSalon").value = "";
    document.getElementById("txtUbicacion").value = "";
}

function cargarSalon(page, pageSize) {
    const skip = pageSize * (page - 1);
    const url = `http://localhost:8080/Estadias/api/salon/getAll?limit=${pageSize}&skip=${skip}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log('Respuesta JSON:', data);
            actualizaSalon(data);
            const rangeInfo = `Mostrando ${skip + 1} a ${skip + data.length} salones`;
            document.getElementById('rango-registros').textContent = rangeInfo;
        })
        .catch(error => console.error('Error al obtener salones:', error));
}

function actualizaSalon(salones) {
    const tbody = document.getElementById('salon-table-body');
    if (!tbody) {
        console.error('Error: tbody no encontrado.');
        return;
    }

    tbody.innerHTML = '';
    salones.forEach(item => {
        const row = tbody.insertRow();
        row.dataset.id = item.idSalon;
        row.addEventListener('click', () => mostrarDetalleSalon(item.idSalon));
        row.insertCell(0).textContent = item.idSalon;
        row.insertCell(1).textContent = item.nombre;
        row.insertCell(2).textContent = item.ubicacion;

        const editButtonCell = row.insertCell(3);
        const editButton = document.createElement('button');
        editButton.innerHTML = '<i class="bi bi-pencil-square"></i>';
        editButton.classList.add('btn', 'btn-sm', 'btn-primary');
        editButton.addEventListener('click', (event) => {
            event.stopPropagation();
            editarSalon(item.idSalon);
        });
        editButtonCell.appendChild(editButton);
    });
}

function editarSalon(idSalon) {
    const row = document.querySelector(`#salon-table-body tr[data-id="${idSalon}"]`);
    if (!row) {
        console.error('Error: fila no encontrada.');
        return;
    }

    const nombre = row.cells[1].textContent;
    const ubicacion = row.cells[2].textContent;

    document.getElementById('editNombreSalon').value = nombre;
    document.getElementById('editUbicacion').value = ubicacion;

    salonSeleccionada = { idSalon: idSalon };

    $('#editModal').modal('show');
}

function cambiarPaginaSalon(delta) {
    currentPage += delta;
    if (currentPage < 1) {
        currentPage = 1;
    }
    cargarSalon(currentPage, recordsPerPage);
}

function mostrarDetalleSalon(idSalon) {
    const url = `http://localhost:8080/Estadias/api/salon/getSalonDetails?id=${idSalon}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error al obtener detalles del salón. Código: ${response.status}`);
            }
            return response.json();
        })
        .then(salon => {
            salonSeleccionada = salon;
            console.log('Detalles del salón:', salon);

            document.getElementById('editNombreSalon').value = salon.nombre;
            document.getElementById('editUbicacion').value = salon.ubicacion;
        })
        .catch(error => {
            console.error(`Error: ${error.message}`);
        });
}

function buscarSalon() {
    const searchInput = document.getElementById('searchInput').value.toLowerCase();
    const tableBody = document.getElementById('salon-table-body');
    const rows = tableBody.getElementsByTagName('tr');
    for (let i = 0; i < rows.length; i++) {
        const cells = rows[i].getElementsByTagName('td');
        const nombreSalon = cells[1].textContent.toLowerCase();
        const ubicacion = cells[2].textContent.toLowerCase();
        if (nombreSalon.startsWith(searchInput) || ubicacion.startsWith(searchInput)) {
            rows[i].style.display = '';
        } else {
            rows[i].style.display = 'none';
        }
    }
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

function ingresarSalon(event) {
    event.preventDefault(); // Prevenir el comportamiento predeterminado del formulario

    let v_nombre = document.getElementById("txtNombreSalon").value;
    let v_ubicacion = document.getElementById("txtUbicacion").value;

    // Asegúrate de que los campos no estén vacíos
    if (!v_nombre || !v_ubicacion) {
        Swal.fire({
            title: 'Error',
            text: 'Todos los campos son obligatorios',
            icon: 'error'
        });
        return;
    }

    let salon = {
        nombre: v_nombre,
        ubicacion: v_ubicacion
    };

    let salonJson = JSON.stringify(salon);

    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ salon: salonJson })
    };

    fetch("http://localhost:8080/Estadias/api/salon/insertSalon", requestOptions)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }
            return response.json();
        })
        .then(json => {
            console.log(json);
            Swal.fire({
                title: 'Salón Registrado',
                text: `${v_nombre}`,
                icon: 'success'
            }).then(() => {
                limpiar();
                resetSteps();
                cargarSalon(currentPage, recordsPerPage);
            });
        })
        .catch(error => {
            console.error('Error al registrar salón:', error);
            Swal.fire({
                title: 'Error',
                text: 'Hubo un error al registrar el salón',
                icon: 'error'
            });
        });
}

function limpiar() {
    document.getElementById("txtNombreSalon").value = "";
    document.getElementById("txtUbicacion").value = "";
}
