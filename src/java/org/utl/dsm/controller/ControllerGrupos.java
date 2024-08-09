/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.utl.dsm.db.ConexionMysql;

import java.util.List;
import org.utl.dsm.model.Grupo;
import org.utl.dsm.model.Materia;

/**
 *
 * @author betillo
 */
public class ControllerGrupos {
    public List<Grupo> getAll() throws SQLException {
        String sql = "SELECT g.id, g.nombre_grupo, g.capacidad, g.modalidad, "
                   + "m.idMateria, m.nombre AS materiaNombre, m.clave, m.horas "
                   + "FROM grupo g "
                   + "LEFT JOIN grupo_materia gm ON g.id = gm.idGrupo "
                   + "LEFT JOIN materia m ON gm.idMateria = m.idMateria";

        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        List<Grupo> grupos = new ArrayList<>();
        while (rs.next()) {
            int idGrupo = rs.getInt("id");
            Grupo grupo = findGrupoById(grupos, idGrupo);

            if (grupo == null) {
                grupo = new Grupo();
                grupo.setId(idGrupo);
                grupo.setNombreGrupo(rs.getString("nombre_grupo"));
                grupo.setCapacidad(rs.getInt("capacidad"));
                grupo.setModalidad(rs.getString("modalidad"));
                grupo.setMaterias(new ArrayList<>());
                grupos.add(grupo);
            }

            int idMateria = rs.getInt("idMateria");
            if (idMateria > 0) {
                Materia materia = new Materia();
                materia.setIdMateria(idMateria);
                materia.setNombre(rs.getString("materiaNombre"));
                materia.setClave(rs.getString("clave"));
                materia.setHoras(rs.getInt("horas"));
                grupo.getMaterias().add(materia);
            }
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return grupos;
    }

    private Grupo findGrupoById(List<Grupo> grupos, int id) {
        for (Grupo grupo : grupos) {
            if (grupo.getId() == id) {
                return grupo;
            }
        }
        return null;
    }
    public List<Materia> getMateriasByGrupo(int idGrupo) throws SQLException {
        String sql = "SELECT m.idMateria, m.nombre, m.clave, m.horas "
                   + "FROM materia m "
                   + "JOIN grupo_materia gm ON m.idMateria = gm.idMateria "
                   + "WHERE gm.idGrupo = ?";

        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idGrupo);
        ResultSet rs = pstmt.executeQuery();

        List<Materia> materias = new ArrayList<>();
        while (rs.next()) {
            Materia materia = new Materia();
            materia.setIdMateria(rs.getInt("idMateria"));
            materia.setNombre(rs.getString("nombre"));
            materia.setClave(rs.getString("clave"));
            materia.setHoras(rs.getInt("horas"));
            materias.add(materia);
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return materias;
    }
    
    public String getModalidadByGrupo(int idGrupo) throws SQLException {
        String sql = "SELECT modalidad FROM grupo WHERE id = ?";
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idGrupo);
        ResultSet rs = pstmt.executeQuery();

        String modalidad = null;
        if (rs.next()) {
            modalidad = rs.getString("modalidad");
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return modalidad;
    }
    
}
