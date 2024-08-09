package org.utl.dsm.controller;

import org.utl.dsm.model.DiaDisponible;
import org.utl.dsm.db.ConexionMysql;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControllerDia_Disponible {
    
    public List<DiaDisponible> getAll() throws SQLException {
        String sql = "SELECT * FROM dias_disponibles";  // Cambia 'dia_disponible' al nombre real de tu tabla
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<DiaDisponible> diasDisponibles = new ArrayList<>();
        while (rs.next()) {
            diasDisponibles.add(fill(rs));
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return diasDisponibles;
    }
    
    public DiaDisponible fill(ResultSet rs) throws SQLException {
        DiaDisponible dia = new DiaDisponible();
        dia.setIdDia(rs.getInt("idDia"));
        dia.setNombre(rs.getString("nombre"));
        return dia;
    }
}