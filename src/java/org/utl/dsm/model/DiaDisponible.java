/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.model;

/**
 *
 * @author betillo
 */

public class DiaDisponible {
    private int idDia;
    private String nombre;
    private String horaInicio; // Campo nuevo
    private String horaFin;

    public DiaDisponible() {}

    public DiaDisponible(int idDia, String nombre) {
        this.idDia = idDia;
        this.nombre = nombre;
    }

    public DiaDisponible(int idDia, String nombre, String horaInicio, String horaFin) {
        this.idDia = idDia;
        this.nombre = nombre;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public int getIdDia() {
        return idDia;
    }

    public void setIdDia(int idDia) {
        this.idDia = idDia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DiaDisponible{");
        sb.append("idDia:").append(idDia);
        sb.append(", nombre:").append(nombre);
        sb.append(", horaInicio:").append(horaInicio);
        sb.append(", horaFin:").append(horaFin);
        sb.append('}');
        return sb.toString();
    }
}

