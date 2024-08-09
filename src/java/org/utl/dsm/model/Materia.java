/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.model;


public class Materia {
    private int idMateria;
    private String nombre;
    private String clave;
    private int horas;

    public Materia() {}

    public Materia(int idMateria, String nombre, String clave, int horas) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.clave = clave;
        this.horas = horas;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public Materia getMateria() {
        return this;
    }
    @Override
    public String toString() {
        return "Materia{" +
                "idMateria:" + idMateria +
                ", nombre:'" + nombre + '\'' +
                ", clave:'" + clave + '\'' +
                ", horas:" + horas +
                '}';
    }
}

