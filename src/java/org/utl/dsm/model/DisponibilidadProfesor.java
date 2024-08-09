/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.model;

import java.time.LocalTime;
/**
 *
 * @author betillo
 */
public class DisponibilidadProfesor {
    private int idDia;
    private String nombreDia;
    private String horaInicio;
    private String horaFin;

    public DisponibilidadProfesor() {
    }

    public DisponibilidadProfesor(int idDia, String nombreDia, String horaInicio, String horaFin) {
        this.idDia = idDia;
        this.nombreDia = nombreDia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public int getIdDia() {
        return idDia;
    }

    public void setIdDia(int idDia) {
        this.idDia = idDia;
    }

    public String getNombreDia() {
        return nombreDia;
    }

    public void setNombreDia(String nombreDia) {
        this.nombreDia = nombreDia;
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
        sb.append("DisponibilidadProfesor{");
        sb.append("idDia:").append(idDia);
        sb.append(", nombreDia:").append(nombreDia);
        sb.append(", horaInicio:").append(horaInicio);
        sb.append(", horaFin:").append(horaFin);
        sb.append('}');
        return sb.toString();
    }
    
    
}
