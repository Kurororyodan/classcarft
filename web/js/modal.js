// Modal functionality for Create Schedule
const createModal = document.getElementById("createModal");
const openCreateModalBtn = document.getElementById("openCreateModal");
const closeCreateModalSpan = document.getElementById("closeCreateModal");

openCreateModalBtn.onclick = function() {
    createModal.style.display = "block";
}

closeCreateModalSpan.onclick = function() {
    createModal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target == createModal) {
        createModal.style.display = "none";
    }
}

// Modal functionality for showing existing schedules or schedules by professor
const showModal = document.getElementById("showModal");
const openShowModalBtn = document.getElementById("openShowModal");
const closeShowModalSpan = document.getElementById("closeShowModal");

openShowModalBtn.onclick = function() {
    showModal.style.display = "block";
    document.getElementById('optionExisting').checked = true; // Set default option
    showExistingScheduleSection();
}

closeShowModalSpan.onclick = function() {
    showModal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target == showModal) {
        showModal.style.display = "none";
    }
}

// Radio button functionality
document.querySelectorAll('input[name="scheduleOption"]').forEach((elem) => {
    elem.addEventListener("change", function(event) {
        if (event.target.value === 'existing') {
            showExistingScheduleSection();
        } else {
            showProfessorScheduleSection();
        }
    });
});

function showExistingScheduleSection() {
    document.getElementById('existingScheduleSection').style.display = 'block';
    document.getElementById('professorScheduleSection').style.display = 'none';
    getAllExistingSchedules(); // Fetch existing schedules to populate the dropdown
}

function showProfessorScheduleSection() {
    document.getElementById('existingScheduleSection').style.display = 'none';
    document.getElementById('professorScheduleSection').style.display = 'block';
    getAllProfessores(); // Fetch professors to populate the dropdown
}

// Function to fetch and populate existing schedules
function getAllExistingSchedules() {
    fetch('http://localhost:8080/classcraft/api/Horario/getAllIdentificadorHorario')
        .then(response => response.json())
        .then(data => {
            const existingSchedules = document.getElementById('existingSchedules');
            existingSchedules.innerHTML = '<option value="">Seleccione un horario</option>';
            data.forEach(identificador => {
                const option = document.createElement('option');
                option.value = identificador;
                option.textContent = identificador;
                existingSchedules.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al obtener los identificadores:', error);
        });
}

// Function to fetch and populate professors
function getAllProfessores() {
    fetch('http://localhost:8080/classcraft/api/profesor/getAllProfesores')
        .then(response => response.json())
        .then(data => {
            const professorList = document.getElementById('professorList');
            professorList.innerHTML = '<option value="">Seleccione un profesor</option>';
            data.forEach(profesor => {
                const nombreCompleto = `${profesor.persona.nombre} ${profesor.persona.apellidoPaterno} ${profesor.persona.apellidoMaterno}`;
                const option = document.createElement('option');
                option.value = nombreCompleto;
                option.textContent = nombreCompleto;
                professorList.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al obtener los profesores:', error);
        });
}

document.getElementById('professorList').addEventListener('change', () => {
    const selectedProfessor = document.getElementById('professorList').value;
    if (selectedProfessor) {
        getHorariosByProfesor(selectedProfessor);
    } else {
        document.getElementById('professorScheduleList').innerHTML = '<option value="">Seleccione un identificador</option>';
        document.getElementById('professorScheduleList').disabled = true;
    }
});

function getHorariosByProfesor(profesor) {
    fetch(`http://localhost:8080/classcraft/api/Horario/getByProfesor/${profesor}`)
        .then(response => response.json())
        .then(data => {
            const professorScheduleList = document.getElementById('professorScheduleList');
            professorScheduleList.innerHTML = '<option value="">Seleccione un identificador</option>';
            const uniqueIdentifiers = new Set();
            data.forEach(horario => {
                uniqueIdentifiers.add(horario.identificador);
            });
            uniqueIdentifiers.forEach(identificador => {
                const option = document.createElement('option');
                option.value = identificador;
                option.textContent = identificador;
                professorScheduleList.appendChild(option);
            });
            professorScheduleList.disabled = false;
        })
        .catch(error => {
            console.error('Error al obtener los horarios por profesor:', error);
        });
}

// Function to download the schedule based on selection
document.getElementById('downloadSchedule').addEventListener('click', () => {
    const selectedOption = document.querySelector('input[name="scheduleOption"]:checked').value;
    console.log(`Selected option: ${selectedOption}`);

    if (selectedOption === 'existing') {
        const selectedSchedule = document.getElementById('existingSchedules').value;
        console.log(`Selected existing schedule: ${selectedSchedule}`);
        if (selectedSchedule) {
            window.location.href = `http://localhost:8080/classcraft/api/Horario/getByIdentificador/${selectedSchedule}`;
        } else {
            alert('Por favor, seleccione un horario para descargar.');
        }
    } else if (selectedOption === 'professor') {
        const selectedProfessor = document.getElementById('professorList').value;
        const selectedSchedule = document.getElementById('professorScheduleList').value;
        console.log(`Selected professor: ${selectedProfessor}`);
        console.log(`Selected schedule identifier: ${selectedSchedule}`);
        if (selectedProfessor && selectedSchedule) {
            window.location.href = `http://localhost:8080/classcraft/api/Horario/getByProfesorIdentificador/${selectedProfessor}/${selectedSchedule}`;
        } else {
            alert('Por favor, seleccione un profesor y un identificador para descargar.');
        }
    }
});

// Steps functionality for Create Schedule
const nextButton = document.querySelector('.btn-next');
const prevButton = document.querySelector('.btn-prev');
const steps = document.querySelectorAll('.step');
const form_steps = document.querySelectorAll('.form-step');
const submitButton = document.querySelector('.btn-submit');
let active = 1;

nextButton.addEventListener('click', () => {
    const horarioSelect = document.getElementById('horarioSelect');
    const horarioInput = document.getElementById('horarioInput');
    const grupoSelect = document.getElementById('grupoSelect');

    if (active === 1 && !horarioSelect.value && !horarioInput.value) {
        alert('Por favor, seleccione o ingrese un horario');
        return;
    } else if (active === 2 && !grupoSelect.value) {
        alert('Por favor, seleccione un grupo');
        return;
    }

    if (active === 2) {
        const idGrupo = grupoSelect.value;
        getMateriasByGrupo(idGrupo); // Obtener materias y profesores automáticamente
    }

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

document.addEventListener('DOMContentLoaded', () => {
    getAllIdentificadorHorario();
    getAllGrupos();

    const horarioSelect = document.getElementById('horarioSelect');
    const horarioInput = document.getElementById('horarioInput');

    // Evento para deshabilitar el input de nuevo horario cuando se selecciona un horario previo
    horarioSelect.addEventListener('change', () => {
        if (horarioSelect.value) {
            horarioInput.disabled = true;
        } else {
            horarioInput.disabled = false;
        }
    });

    // Evento para deshabilitar el select de horario previo cuando se escribe un nuevo horario
    horarioInput.addEventListener('input', () => {
        if (horarioInput.value) {
            horarioSelect.disabled = true;
        } else {
            horarioSelect.disabled = false;
        }
    });

    const grupoSelect = document.getElementById('grupoSelect');
    grupoSelect.addEventListener('change', () => {
        if (grupoSelect.value) {
            getMateriasByGrupo(grupoSelect.value); // Obtener materias y profesores automáticamente
        }
    });
});
