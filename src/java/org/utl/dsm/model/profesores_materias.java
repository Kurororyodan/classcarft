/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.model;

/**
 *
 * @author bryan
 */
public class profesores_materias {
    private Profesor profesor;
    private Materia materia;

    public profesores_materias() {
    }

    
    
    public profesores_materias(Profesor profesor, Materia materia) {
        this.profesor = profesor;
        this.materia = materia;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("profesores_materias{");
        sb.append("profesor:").append(profesor);
        sb.append(", materia:").append(materia);
        sb.append('}');
        return sb.toString();
    }
    
    
    
    
}
