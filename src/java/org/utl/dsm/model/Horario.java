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
public class Horario {
    private int idHorario;
    private String identificador;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private String curso;
    private String profesor;
    private String aula;
    
    private String nuevoCurso;
    private String nuevoProfesor;
    private String nuevoAula;
    public Horario() {
    }

    public Horario(int idHorario, String identificador, String diaSemana, String horaInicio, String horaFin, String curso, String profesor, String aula) {
        this.idHorario = idHorario;
        this.identificador = identificador;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.curso = curso;
        this.profesor = profesor;
        this.aula = aula;
    }

    public Horario(int idHorario, String identificador, String diaSemana, String horaInicio, String horaFin, String curso, String profesor, String aula, String nuevoCurso, String nuevoProfesor, String nuevoAula) {
        this.idHorario = idHorario;
        this.identificador = identificador;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.curso = curso;
        this.profesor = profesor;
        this.aula = aula;
        this.nuevoCurso = nuevoCurso;
        this.nuevoProfesor = nuevoProfesor;
        this.nuevoAula = nuevoAula;
    }
    
    
    

    

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
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

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getNuevoCurso() {
        return nuevoCurso;
    }

    public void setNuevoCurso(String nuevoCurso) {
        this.nuevoCurso = nuevoCurso;
    }

    public String getNuevoProfesor() {
        return nuevoProfesor;
    }

    public void setNuevoProfesor(String nuevoProfesor) {
        this.nuevoProfesor = nuevoProfesor;
    }

    public String getNuevoAula() {
        return nuevoAula;
    }

    public void setNuevoAula(String nuevoAula) {
        this.nuevoAula = nuevoAula;
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Horario{");
        sb.append("idHorario:").append(idHorario);
        sb.append(", identificador:").append(identificador);
        sb.append(", diaSemana:").append(diaSemana);
        sb.append(", horaInicio:").append(horaInicio);
        sb.append(", horaFin:").append(horaFin);
        sb.append(", curso:").append(curso);
        sb.append(", profesor:").append(profesor);
        sb.append(", aula:").append(aula);
        sb.append('}');
        return sb.toString();
    }
    
    
    }
