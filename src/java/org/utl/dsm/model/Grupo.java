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
public class Grupo {
    private int id;
    private String nombreGrupo;
    private int capacidad;
    private String modalidad;
    private List<Materia> materias;

    public Grupo() {
    }

    public Grupo(int id, String nombreGrupo, int capacidad, String modalidad, List<Materia> materias) {
        this.id = id;
        this.nombreGrupo = nombreGrupo;
        this.capacidad = capacidad;
        this.modalidad = modalidad;
        this.materias = materias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grupo{");
        sb.append("id:").append(id);
        sb.append(", nombreGrupo:").append(nombreGrupo);
        sb.append(", capacidad:").append(capacidad);
        sb.append(", modalidad:").append(modalidad);
        sb.append(", materias:").append(materias);
        sb.append('}');
        return sb.toString();
    }
    
    
    
}
