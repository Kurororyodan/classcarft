/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.model;

import java.util.List;

/**
 *
 * @author betillo
 */
public class Alumno {
  private int  idAlumno;
  private String matricula;
  private int  estatus;
  private String modalidad;
  private String fechaIngreso;
  private Persona persona;
  private List<Materia> materias; 

    public Alumno() {}

    public Alumno(int idAlumno, String matricula, int estatus, String modalidad, String fechaIngreso, Persona persona, List<Materia> materias) {
        this.idAlumno = idAlumno;
        this.matricula = matricula;
        this.estatus = estatus;
        this.modalidad = modalidad;
        this.fechaIngreso = fechaIngreso;
        this.persona = persona;
        this.materias = materias;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Alumno{");
        sb.append("idAlumno:").append(idAlumno);
        sb.append(", matricula:").append(matricula);
        sb.append(", estatus:").append(estatus);
        sb.append(", modalidad:").append(modalidad);
        sb.append(", fechaIngreso:").append(fechaIngreso);
        sb.append(", persona:").append(persona);
        sb.append(", materias:").append(materias);
        sb.append('}');
        return sb.toString();
    }
    
  
}
    