document.addEventListener('DOMContentLoaded', () => {
    getAllIdentificadorHorario();
    getAllGrupos();

    const horarioSelect = document.getElementById('horarioSelect');
    const horarioInput = document.getElementById('horarioInput');

    horarioSelect.addEventListener('change', () => {
        if (horarioSelect.value) {
            horarioInput.disabled = true;
        } else {
            horarioInput.disabled = false;
        }
    });

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
            getMateriasByGrupo(grupoSelect.value);
        }
    });
});

function verificarToken() {
    let token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "http://localhost:8080/classcraft/index.html";
    }
}

function getAllIdentificadorHorario() {
    fetch('http://localhost:8080/classcraft/api/Horario/getAllIdentificadorHorario')
        .then(response => response.json())
        .then(data => {
            const horarioSelect = document.getElementById('horarioSelect');
            horarioSelect.innerHTML = '<option value="">Seleccione un horario previo</option>';
            data.forEach(identificador => {
                const option = document.createElement('option');
                option.value = identificador;
                option.textContent = identificador;
                horarioSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al obtener los identificadores:', error);
        });
}

function getMateriasByGrupo(idGrupo) {
    return fetch(`http://localhost:8080/classcraft/api/Grupo/getMateriasByGrupo/${idGrupo}`)
        .then(response => response.json())
        .then(data => data)
        .catch(error => {
            console.error('Error al obtener las materias:', error);
        });
}

function getAllGrupos() {
    fetch('http://localhost:8080/classcraft/api/Grupo/getAllGrupos')
        .then(response => response.json())
        .then(data => {
            const grupoSelect = document.getElementById('grupoSelect');
            grupoSelect.innerHTML = '<option value="">Seleccione un grupo</option>';
            data.forEach(grupo => {
                const option = document.createElement('option');
                option.value = grupo.id;
                option.textContent = grupo.nombreGrupo;
                option.dataset.modalidad = grupo.modalidad;
                grupoSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al obtener los grupos:', error);
        });
}

let profesores = [];

function horaStringToMinutes(hora) {
    const [hours, minutes] = hora.split(':').map(Number);
    return hours * 60 + minutes;
}

function formatHora(horaInicio) {
    let [hours, minutes, seconds] = horaInicio.split(':').map(Number);
    minutes += 50;
    if (minutes >= 60) {
        hours += 1;
        minutes -= 60;
    }
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
}

function esHoraValida(profesor, diaNombre, horaInicio, horariosFijos) {
    const horaInicioMin = horaStringToMinutes(horaInicio);
    const horaFinMin = horaInicioMin + 50;

    const disponibilidad = profesor.disponibilidad_dias.find(dia => dia.nombreDia === diaNombre);
    if (!disponibilidad) {
        return false;
    }

    const disponibilidadInicioMin = horaStringToMinutes(disponibilidad.horaInicio);
    const disponibilidadFinMin = horaStringToMinutes(disponibilidad.horaFin);

    if (!(disponibilidadInicioMin <= horaInicioMin && disponibilidadFinMin >= horaFinMin)) {
        return false;
    }

    for (const horario of horariosFijos) {
        if (horario.diaSemana === diaNombre) {
            const horarioInicioMin = horaStringToMinutes(horario.horaInicio);
            const horarioFinMin = horaStringToMinutes(horario.horaFin);
            if ((horaInicioMin >= horarioInicioMin && horaInicioMin < horarioFinMin) ||
                (horaFinMin > horarioInicioMin && horaFinMin <= horarioFinMin) ||
                (horaInicioMin <= horarioInicioMin && horaFinMin >= horarioFinMin)) {
                return false;
            }
        }
    }

    return true;
}

async function createSchedule() {
    console.log("Inicio de la función createSchedule");

    const horarioSelect = document.getElementById('horarioSelect');
    const horarioInput = document.getElementById('horarioInput');
    const grupoSelect = document.getElementById('grupoSelect');

    const identificador = horarioSelect.value || horarioInput.value;
    const idGrupo = grupoSelect.value;
    const modalidad = grupoSelect.options[grupoSelect.selectedIndex].dataset.modalidad;

    console.log(`Datos obtenidos: identificador: "${identificador}", idGrupo: "${idGrupo}", modalidad: "${modalidad}"`);

    if (!identificador || !idGrupo) {
        alert('Por favor, complete todos los campos requeridos.');
        return;
    }

    try {
        const materiasResponse = await fetch(`http://localhost:8080/classcraft/api/Grupo/getMateriasByGrupo/${idGrupo}`);
        const materias = await materiasResponse.json();
        console.log("Materias obtenidas: ", materias);

        const todasLasMateriasResponse = await fetch(`http://localhost:8080/classcraft/api/profesor/getAllMaterias`);
        const todasLasMaterias = await todasLasMateriasResponse.json();
        const materiasValidas = todasLasMaterias.map(materia => materia.nombre);
        console.log("Todas las materias válidas: ", materiasValidas);

        const modalidadHorarios = {
            '14 CUAT MAT': { startHour: 7, endHour: 10 },
            '9 CUAT MAT': { startHour: 7, endHour: 12 },
            '9 CUAT DIU': { startHour: 10, endHour: 15 },
            '8 CUAT MAT': { startHour: 7, endHour: 14 },
            '8 CUAT DIU': { startHour: 10, endHour: 16 },
            '8 CUAT VES': { startHour: 15, endHour: 21 },
            '14 CUAT VES': { startHour: 16, endHour: 21 },
            '9 CUAT VES': { startHour: 16, endHour: 21 },
            '13 CUAT NOC': { startHour: 17, endHour: 22 },
            '14 CUAT NOC': { startHour: 19, endHour: 22 },
            '13 CUAT MAT': { startHour: 7, endHour: 12 },
            '13 CUAT VES': { startHour: 16, endHour: 21 }
        };

        const { startHour, endHour } = modalidadHorarios[modalidad] || { startHour: 7, endHour: 21 };
        console.log("Horario para la modalidad: ", { startHour, endHour });

        let inserciones = [];
        let horariosFijos = [];

        let horariosFijosResponse = await fetch(`http://localhost:8080/classcraft/api/Horario/getByIdenti/${identificador}`);
        let horariosExistentes = await horariosFijosResponse.json();

        for (let horarioExistente of horariosExistentes) {
            if (!materiasValidas.includes(horarioExistente.curso)) {
                horariosFijos.push(horarioExistente);
            }
        }

        console.log("Horarios fijos existentes: ", horariosFijos);

        for (let materia of materias) {
            console.log("Procesando materia: ", materia);

            const profesoresResponse = await fetch(`http://localhost:8080/classcraft/api/profesor/getProfesoresByMateria/${materia.idMateria}`);
            let profesoresMateria = await profesoresResponse.json();
            console.log("Profesores para la materia: ", profesoresMateria);

            let profesorSeleccionado = null;
            profesoresMateria.forEach(profesor => {
                profesor.materia = materia;
                profesor.disponibilidad_dias = profesor.disponibilidad_dias.filter(dia => {
                    const diaInicio = parseInt(dia.horaInicio.split(':')[0]);
                    const diaFin = parseInt(dia.horaFin.split(':')[0]);
                    return diaInicio <= endHour && diaFin >= startHour;
                });

                if (profesor.disponibilidad_dias.length > 0) {
                    if (!profesorSeleccionado || profesor.disponibilidad_dias.length < profesorSeleccionado.disponibilidad_dias.length) {
                        profesorSeleccionado = profesor;
                    }
                }
            });

            if (profesorSeleccionado) {
                profesores.push(profesorSeleccionado);
            }
        }

        profesores.sort((a, b) => {
            let aDiasDisponibles = a.disponibilidad_dias.length;
            let bDiasDisponibles = b.disponibilidad_dias.length;

            if (aDiasDisponibles !== bDiasDisponibles) {
                return aDiasDisponibles - bDiasDisponibles;
            }

            let aHorasDisponibles = a.disponibilidad_dias.reduce((total, dia) => {
                const diaInicio = parseInt(dia.horaInicio.split(':')[0]);
                const diaFin = parseInt(dia.horaFin.split(':')[0]);
                return total + (diaFin - diaInicio);
            }, 0);

            let bHorasDisponibles = b.disponibilidad_dias.reduce((total, dia) => {
                const diaInicio = parseInt(dia.horaInicio.split(':')[0]);
                const diaFin = parseInt(dia.horaFin.split(':')[0]);
                return total + (diaFin - diaInicio);
            }, 0);

            return aHorasDisponibles - bHorasDisponibles;
        });

        console.log("Profesores ordenados: ", profesores);

        for (let profesor of profesores) {
            let horasRestantes = profesor.materia.horas;
            let horasAsignadasSeguidas = 0;

            for (let dia of profesor.disponibilidad_dias) {
                let availableStartHour = startHour;
                const availableEndHour = endHour;

                while (horasRestantes > 0 && availableStartHour < availableEndHour) {
                    let horaInicio = `${availableStartHour.toString().padStart(2, '0')}:00:00`;
                    let horaFin = formatHora(horaInicio);

                    if (esHoraValida(profesor, dia.nombreDia, horaInicio, horariosFijos)) {
                        let conflict = inserciones.some(insercion =>
                            insercion.diaSemana === dia.nombreDia &&
                            insercion.horaInicio === horaInicio &&
                            insercion.horaFin === horaFin
                        );

                        if (!conflict) {
                            console.log(`Agregando al Map: identificador: "${identificador}", día: "${dia.nombreDia}", horaInicio: "${horaInicio}", horaFin: "${horaFin}" debido a que el profesor ${profesor.persona.nombre} tiene disponibilidad de ${dia.horaInicio} a ${dia.horaFin}`);

                            inserciones.push({
                                identificador: identificador,
                                diaSemana: dia.nombreDia,
                                horaInicio: horaInicio,
                                horaFin: horaFin,
                                curso: profesor.materia.nombre,
                                profesor: `${profesor.persona.nombre} ${profesor.persona.apellidoPaterno} ${profesor.persona.apellidoMaterno}`,
                                profesorId: profesor.idProfesor,
                                aula: 'Aula Asignada'
                            });

                            horasRestantes--;
                            horasAsignadasSeguidas++;

                            if (horasAsignadasSeguidas >= 2 && horasRestantes > 0) {
                                let hayEspacio = false;
                                for (let h = startHour; h < endHour; h++) {
                                    let nextHoraInicio = `${h.toString().padStart(2, '0')}:00:00`;
                                    let nextHoraFin = formatHora(nextHoraInicio);

                                    if (esHoraValida(profesor, dia.nombreDia, nextHoraInicio, horariosFijos) && !inserciones.some(insercion =>
                                        insercion.diaSemana === dia.nombreDia &&
                                        insercion.horaInicio === nextHoraInicio &&
                                        insercion.horaFin === nextHoraFin
                                    )) {
                                        hayEspacio = true;
                                        break;
                                    }
                                }

                                if (!hayEspacio) {
                                    console.log('No hay más opciones disponibles, asignando más de 2 horas seguidas.');
                                    horasAsignadasSeguidas = 0;
                                } else {
                                    console.log('Más de 2 horas seguidas no permitidas. Terminando asignación para esta materia.');
                                    break;
                                }
                            }
                        } else {
                            console.log(`Conflicto o límite de horas alcanzado para el profesor ${profesor.persona.nombre}.`);
                        }
                    } else {
                        console.log(`Hora no válida para el profesor ${profesor.persona.nombre} el día ${dia.nombreDia}: ${horaInicio}`);
                    }

                    availableStartHour++;
                }

                if (horasRestantes <= 0) break;
            }
        }

        console.log("Inserciones finales: ", inserciones);

        for (let insercion of inserciones) {
            console.log(`Insertando horario: identificador: "${insercion.identificador}", día: "${insercion.diaSemana}", horaInicio: "${insercion.horaInicio}", horaFin: "${insercion.horaFin}"`);

            const horarioParams = new URLSearchParams();
            horarioParams.append('identificador', insercion.identificador);
            horarioParams.append('diaSemana', insercion.diaSemana);
            horarioParams.append('horaInicio', insercion.horaInicio);
            horarioParams.append('horaFin', insercion.horaFin);
            horarioParams.append('curso', insercion.curso);
            horarioParams.append('profesor', insercion.profesor);
            horarioParams.append('profesorId', insercion.profesorId);
            horarioParams.append('aula', insercion.aula);

            const response = await fetch('http://localhost:8080/classcraft/api/Horario/insertHorario', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: horarioParams.toString()
            });

            const data = await response.json();
            if (data.result !== 'Objeto insertado') {
                console.error('Error al insertar el horario');
                alert('Error al insertar el horario');
                break;
            }
        }

        alert('Horarios insertados correctamente');
    } catch (error) {
        console.error('Error al crear el horario:', error);
        alert('Error al crear el horario');
    }
}

document.querySelector('.btn-submit').addEventListener('click', createSchedule);
