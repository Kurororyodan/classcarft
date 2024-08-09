/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.model;

/**
 *
 * @author betillo
 */
public class GrupoMateria {
    private int idGrupo;
    private int idMateria;

    public GrupoMateria() {
    }

    public GrupoMateria(int idGrupo, int idMateria) {
        this.idGrupo = idGrupo;
        this.idMateria = idMateria;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GrupoMateria{");
        sb.append("idGrupo:").append(idGrupo);
        sb.append(", idMateria:").append(idMateria);
        sb.append('}');
        return sb.toString();
    }
    
    
}
