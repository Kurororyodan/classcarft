/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.model;


/**
 *
 * @author bryan
 */

import java.util.List;

public class Profesor {
    private int idProfesor;
    private String codigo;
    private int disponibilidad_horaria;
    private int estatus;
    private String fechaIngreso;
    private Persona persona;
    private List<Materia> materias; // Nueva lista de materias
    private List<DisponibilidadProfesor> disponibilidad_dias; // Nueva lista de días disponibles
    private List<profesores_dias> profesores_dias; // Nueva lista de días disponibles


    public Profesor() {}

    public Profesor(int idProfesor, String codigo, int disponibilidad_horaria, int estatus, String fechaIngreso, Persona persona, List<Materia> materias, List<DisponibilidadProfesor> disponibilidad_dias) {
        this.idProfesor = idProfesor;
        this.codigo = codigo;
        this.disponibilidad_horaria = disponibilidad_horaria;
        this.estatus = estatus;
        this.fechaIngreso = fechaIngreso;
        this.persona = persona;
        this.materias = materias;
        this.disponibilidad_dias = disponibilidad_dias;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getDisponibilidad_horaria() {
        return disponibilidad_horaria;
    }

    public void setDisponibilidad_horaria(int disponibilidad_horaria) {
        this.disponibilidad_horaria = disponibilidad_horaria;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public List<DisponibilidadProfesor> getDisponibilidad_dias() {
        return disponibilidad_dias;
    }

    public void setDisponibilidad_dias(List<DisponibilidadProfesor> disponibilidad_dias) {
        this.disponibilidad_dias = disponibilidad_dias;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Profesor{");
        sb.append("idProfesor:").append(idProfesor);
        sb.append(", codigo:").append(codigo);
        sb.append(", disponibilidad_horaria:").append(disponibilidad_horaria);
        sb.append(", estatus:").append(estatus);
        sb.append(", fechaIngreso:").append(fechaIngreso);
        sb.append(", persona:").append(persona);
        sb.append(", materias:").append(materias);
        sb.append(", disponibilidad_dias:").append(disponibilidad_dias);
        sb.append('}');
        return sb.toString();
    }

    
}
