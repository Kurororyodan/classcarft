package org.utl.dsm.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.db.ConexionMysql;
import org.utl.dsm.model.Modalidad;

public class ControllerModalidad {

    public List<Modalidad> getAllModalidades() throws SQLException {
        String sql = "SELECT * FROM modalidad";
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Modalidad> modalidades = new ArrayList<>();
        while (rs.next()) {
            Modalidad modalidad = new Modalidad();
            modalidad.setIdModalidad(rs.getInt("idModalidad"));
            modalidad.setNombre(rs.getString("nombre"));
            modalidades.add(modalidad);
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return modalidades;
    }

    public Modalidad fill(ResultSet rs) throws SQLException {
        Modalidad modalidad = new Modalidad();
        modalidad.setIdModalidad(rs.getInt("idModalidad"));
        modalidad.setNombre(rs.getString("nombre"));
        return modalidad;
    }
}
