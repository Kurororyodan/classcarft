package org.utl.dsm.controller;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.db.ConexionMysql;
import org.utl.dsm.model.Grupo;
import org.utl.dsm.model.Materia;
import java.util.stream.Collectors;

public class ControllerGrupo {

     public void insertGrupo(Grupo grupo) throws SQLException {
        ConexionMysql conexion = new ConexionMysql();
        Connection conn = null;
        try {
            conn = conexion.open();
            conn.setAutoCommit(false);

            String sql = "INSERT INTO grupo (nombre_grupo, capacidad, modalidad) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, grupo.getNombreGrupo());
            pstmt.setInt(2, grupo.getCapacidad());
            pstmt.setString(3, grupo.getModalidad());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                grupo.setId(rs.getInt(1));
            }

            sql = "INSERT INTO grupo_materia (idGrupo, idMateria) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            for (Materia materia : grupo.getMaterias()) {
                pstmt.setInt(1, grupo.getId());
                pstmt.setInt(2, materia.getIdMateria());
                pstmt.addBatch();
            }
            pstmt.executeBatch();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            conexion.close();
        }
    }
    public List<Materia> getAllMaterias() throws SQLException {
        String sql = "SELECT * FROM materia";
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Materia> materias = new ArrayList<>();
        while (rs.next()) {
            Materia materia = new Materia();
            materia.setIdMateria(rs.getInt("idMateria"));
            materia.setNombre(rs.getString("nombre"));
            materias.add(materia);
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return materias;
    }

    public List<Grupo> getAll() throws SQLException {
        String sql = "SELECT * FROM vista_grupos";
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Grupo> grupos = new ArrayList<>();
        while (rs.next()) {
            grupos.add(fill(rs));
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return grupos;
    }

    public Grupo fill(ResultSet rs) throws SQLException {
        Grupo grupo = new Grupo();
        grupo.setId(rs.getInt("id"));
        grupo.setNombreGrupo(rs.getString("nombreGrupo"));
        grupo.setCapacidad(rs.getInt("capacidad"));
        grupo.setModalidad(rs.getString("modalidad"));
        
        // Obtener las materias del grupo
        String sqlMaterias = "SELECT m.idMateria, m.nombre FROM grupo_materia gm " +
                             "JOIN materia m ON gm.idMateria = m.idMateria WHERE gm.idGrupo = ?";
        PreparedStatement pstmtMaterias = rs.getStatement().getConnection().prepareStatement(sqlMaterias);
        pstmtMaterias.setInt(1, grupo.getId());
        ResultSet rsMaterias = pstmtMaterias.executeQuery();
        List<Materia> materias = new ArrayList<>();
        while (rsMaterias.next()) {
            Materia materia = new Materia();
            materia.setIdMateria(rsMaterias.getInt("idMateria"));
            materia.setNombre(rsMaterias.getString("nombre"));
            materias.add(materia);
        }
        grupo.setMaterias(materias);
        rsMaterias.close();
        pstmtMaterias.close();

        return grupo;
    }

    public int updateGrupo(Grupo grupo) throws SQLException {
    String query = "UPDATE grupo SET nombre_grupo = ?, capacidad = ? WHERE id = ?";
    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    
    try {
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, grupo.getNombreGrupo());
        pstmt.setInt(2, grupo.getCapacidad());
        pstmt.setInt(3, grupo.getId());
        int affectedRows = pstmt.executeUpdate();
        return affectedRows;
    } finally {
        conn.close();
    }
}



    public int deleteGrupo(int id) throws SQLException {
    String query = "{call sp_delete_grupo(?)}";
    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    
    try {
        java.sql.CallableStatement cstmt = conn.prepareCall(query);
        cstmt.setInt(1, id);
        int rowsAffected = cstmt.executeUpdate();
        cstmt.close();
        return rowsAffected; // Retorna el número de filas afectadas
    } finally {
        conn.close();
        connMySQL.close();
    }
}


public Grupo getGrupoDetails(int id) throws SQLException {
    ConexionMysql conexion = new ConexionMysql();
    Connection conn = null;
    Grupo grupo = null;
    try {
        conn = conexion.open();
        String sql = "SELECT id, nombre_grupo, capacidad, modalidad FROM grupo WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            grupo = new Grupo();
            grupo.setId(rs.getInt("id"));
            grupo.setNombreGrupo(rs.getString("nombre_grupo"));
            grupo.setCapacidad(rs.getInt("capacidad"));
            grupo.setModalidad(rs.getString("modalidad"));

            // Obtener las materias del grupo
            String sqlMaterias = "SELECT m.idMateria, m.nombre FROM grupo_materia gm " +
                    "JOIN materia m ON gm.idMateria = m.idMateria WHERE gm.idGrupo = ?";
            PreparedStatement pstmtMaterias = conn.prepareStatement(sqlMaterias);
            pstmtMaterias.setInt(1, grupo.getId());
            ResultSet rsMaterias = pstmtMaterias.executeQuery();
            List<Materia> materias = new ArrayList<>();
            while (rsMaterias.next()) {
                Materia materia = new Materia();
                materia.setIdMateria(rsMaterias.getInt("idMateria"));
                materia.setNombre(rsMaterias.getString("nombre"));
                materias.add(materia);
            }
            grupo.setMaterias(materias);
        }
    } finally {
        conexion.close();
    }
    return grupo;
}


    public List<Grupo> getALLS(int limit, int offset) throws SQLException {
        ConexionMysql conexion = new ConexionMysql();
        Connection conn = null;
        List<Grupo> grupos = new ArrayList<>();
        try {
            conn = conexion.open();
            String sql = "SELECT g.id, g.nombre_grupo, g.capacidad, g.modalidad FROM grupo g LIMIT ? OFFSET ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Grupo grupo = new Grupo();
                grupo.setId(rs.getInt("id"));
                grupo.setNombreGrupo(rs.getString("nombre_grupo"));
                grupo.setCapacidad(rs.getInt("capacidad"));
                grupo.setModalidad(rs.getString("modalidad"));

                // Obtener las materias del grupo
                String sqlMaterias = "SELECT m.idMateria, m.nombre FROM grupo_materia gm " +
                        "JOIN materia m ON gm.idMateria = m.idMateria WHERE gm.idGrupo = ?";
                PreparedStatement pstmtMaterias = conn.prepareStatement(sqlMaterias);
                pstmtMaterias.setInt(1, grupo.getId());
                ResultSet rsMaterias = pstmtMaterias.executeQuery();
                List<Materia> materias = new ArrayList<>();
                while (rsMaterias.next()) {
                    Materia materia = new Materia();
                    materia.setIdMateria(rsMaterias.getInt("idMateria"));
                    materia.setNombre(rsMaterias.getString("nombre"));
                    materias.add(materia);
                }
                grupo.setMaterias(materias);

                grupos.add(grupo);
            }
        } finally {
            conexion.close();
        }
        return grupos;
    }
    
    public List<Grupo> buscarGrupo(String query) throws SQLException {
    String sql = "CALL BuscarGrupo(?)";
    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    java.sql.CallableStatement stmt = conn.prepareCall(sql);

    stmt.setString(1, query);

    List<Grupo> grupos = new ArrayList<>();
    try {
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Grupo grupo = new Grupo();
            grupo.setId(rs.getInt("idGrupo"));
            grupo.setNombreGrupo(rs.getString("nombreGrupo"));
            grupo.setCapacidad(rs.getInt("capacidad"));
            grupo.setModalidad(rs.getString("modalidad"));
            grupos.add(grupo);
        }
        rs.close();
    } catch (SQLException e) {
        throw new SQLException("Error al ejecutar el procedimiento almacenado: " + e.getMessage(), e);
    } finally {
        stmt.close();
        connMySQL.close();
    }

    return grupos;
}



}
