/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.controller;
import com.google.gson.Gson;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.util.Types;
import org.utl.dsm.model.Alumno;
import org.utl.dsm.model.Persona;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;
import org.utl.dsm.db.ConexionMysql;
import org.utl.dsm.model.Materia;

/**
 *
 * @author bryan
 */
public class ControllerAlumno {
     
    public List<Alumno> getAll() throws SQLException {
        String sql = "SELECT "
                   + "    a.idAlumno, "
                   + "    a.matricula, "
                   + "    a.estatus, "
                   + "    a.modalidad, "
                   + "    a.fechaIngreso, "
                   + "    a.idPersona, "
                   + "    GROUP_CONCAT(DISTINCT m.nombre ORDER BY m.nombre ASC) AS materias, "
                   + "    pe.idPersona, "
                   + "    pe.nombre, "
                   + "    pe.apellidoPaterno, "
                   + "    pe.apellidoMaterno, "
                   + "    pe.genero, "
                   + "    pe.fechaNacimiento, "
                   + "    pe.rfc, "
                   + "    pe.curp, "
                   + "    pe.domicilio, "
                   + "    pe.codigoPostal, "
                   + "    pe.ciudad, "
                   + "    pe.estado, "
                   + "    pe.telefono, "
                   + "    pe.email "
                   + "FROM "
                   + "    alumno a "
                   + "LEFT JOIN "
                   + "    alumnos_materias am ON a.idAlumno = am.idAlumno "
                   + "LEFT JOIN "
                   + "    materia m ON am.idMateria = m.idMateria "
                   + "INNER JOIN "
                   + "    persona pe ON a.idPersona = pe.idPersona "
                   + "GROUP BY "
                   + "    a.idAlumno, a.matricula, a.estatus, a.modalidad, a.fechaIngreso, a.idPersona, "
                   + "    pe.idPersona, pe.nombre, pe.apellidoPaterno, pe.apellidoMaterno, pe.genero, "
                   + "    pe.fechaNacimiento, pe.rfc, pe.curp, pe.domicilio, pe.codigoPostal, pe.ciudad, pe.estado, pe.telefono, pe.email;";

        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Alumno> alumnos = new ArrayList<>();
        while (rs.next()) {
            alumnos.add(fillAlumno(rs));
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return alumnos;
    }
    
    

    private Alumno fillAlumno(ResultSet rs) throws SQLException {
    Alumno alumno = new Alumno();
    Persona persona = new Persona();

    alumno.setIdAlumno(rs.getInt("idAlumno"));
    alumno.setMatricula(rs.getString("matricula"));
    alumno.setEstatus(rs.getInt("estatus"));
    alumno.setModalidad(rs.getString("modalidad"));
    alumno.setFechaIngreso(rs.getString("fechaIngreso"));

    // Obtener materias concatenadas y separarlas en una lista
    List<Materia> materias = new ArrayList<>();
    String materiasStr = rs.getString("materias");
    if (materiasStr != null && !materiasStr.isEmpty()) {
        String[] materiasArr = materiasStr.split(",");
        for (String materiaNombre : materiasArr) {
            Materia materia = new Materia();
            materia.setNombre(materiaNombre.trim());
            materias.add(materia);
        }
    }
    alumno.setMaterias(materias);

    persona.setIdPersona(rs.getInt("idPersona"));
    persona.setNombre(rs.getString("nombre"));
    persona.setApellidoPaterno(rs.getString("apellidoPaterno"));
    persona.setApellidoMaterno(rs.getString("apellidoMaterno"));
    persona.setGenero(rs.getString("genero"));
    persona.setFechaNacimiento(rs.getString("fechaNacimiento"));
    persona.setRfc(rs.getString("rfc"));
    persona.setCurp(rs.getString("curp"));
    persona.setDomicilio(rs.getString("domicilio"));
    persona.setCodigoPostal(rs.getString("codigoPostal"));
    persona.setCiudad(rs.getString("ciudad"));
    persona.setEstado(rs.getString("estado"));
    persona.setTelefono(rs.getString("telefono"));
    persona.setEmail(rs.getString("email"));

    alumno.setPersona(persona);

    return alumno;
}


 public void desactivarAlumno(int idAlumno) throws SQLException {
    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    String sql = "CALL EliminarAlumno(?)";

    try (java.sql.CallableStatement stmt = conn.prepareCall(sql)) {
        stmt.setInt(1, idAlumno);
        stmt.execute();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    } finally {
        connMySQL.close();
    }
}
    
 
  public void ActivarAlumno(int idAlumno) throws SQLException {
    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    String sql = "CALL ActivarAlumno(?)";

    try (java.sql.CallableStatement stmt = conn.prepareCall(sql)) {
        stmt.setInt(1, idAlumno);
        stmt.execute();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    } finally {
        connMySQL.close();
    }
}
  
     public List<Alumno> buscarAlumno(
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String genero,
        String fechaNacimiento,
        String rfc,
        String curp,
        String domicilio,
        Integer codigoPostal,
        String ciudad,
        String estado,
        String telefono,
        String email,
        String matricula,
        Integer estatus,
        String modalidad,
        String fechaIngreso
) throws SQLException {
    String sql = "CALL BuscarAlumno(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    java.sql.CallableStatement stmt = conn.prepareCall(sql);

    // Setear los parámetros del procedimiento almacenado
    stmt.setString(1, nombre);
    stmt.setString(2, apellidoPaterno);
    stmt.setString(3, apellidoMaterno);
    stmt.setString(4, genero);
    stmt.setString(5, fechaNacimiento);
    stmt.setString(6, rfc);
    stmt.setString(7, curp);
    stmt.setString(8, domicilio);
    if (codigoPostal != null) {
        stmt.setInt(9, codigoPostal);
    } else {
        stmt.setNull(9, java.sql.Types.INTEGER);
    }
    stmt.setString(10, ciudad);
    stmt.setString(11, estado);
    stmt.setString(12, telefono);
    stmt.setString(13, email);
    stmt.setString(14, matricula);
    if (estatus != null) {
        stmt.setInt(15, estatus);
    } else {
        stmt.setNull(15, java.sql.Types.INTEGER);
    }
    stmt.setString(16, modalidad);
    if (fechaIngreso != null) {
        stmt.setString(17, fechaIngreso);
    } else {
        stmt.setNull(17, java.sql.Types.DATE);
    }

    List<Alumno> alumnos = new ArrayList<>();
    try {
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Alumno alumno = fillAlumno2(rs);
            alumnos.add(alumno);
        }
        rs.close();
    } catch (SQLException e) {
        throw new SQLException("Error al ejecutar el procedimiento almacenado: " + e.getMessage(), e);
    } finally {
        stmt.close();
        connMySQL.close();
    }

    return alumnos;
}
     
     
  private Alumno fillAlumno2(ResultSet rs) throws SQLException {
    Alumno alumno = new Alumno();
    Persona persona = new Persona();

    // Llenar datos de la tabla alumno
    alumno.setIdAlumno(rs.getInt("idAlumno"));
    alumno.setMatricula(rs.getString("matricula"));
    alumno.setEstatus(rs.getInt("estatus"));
    alumno.setModalidad(rs.getString("modalidad"));
    alumno.setFechaIngreso(rs.getString("fechaIngreso"));

    // Llenar datos de la tabla persona
    persona.setIdPersona(rs.getInt("idPersona"));
    persona.setNombre(rs.getString("nombre"));
    persona.setApellidoPaterno(rs.getString("apellidoPaterno"));
    persona.setApellidoMaterno(rs.getString("apellidoMaterno"));
    persona.setGenero(rs.getString("genero"));
    persona.setFechaNacimiento(rs.getString("fechaNacimiento"));
    persona.setRfc(rs.getString("rfc"));
    persona.setCurp(rs.getString("curp"));
    persona.setDomicilio(rs.getString("domicilio"));
    persona.setCodigoPostal(rs.getString("codigoPostal"));
    persona.setCiudad(rs.getString("ciudad"));
    persona.setEstado(rs.getString("estado"));
    persona.setTelefono(rs.getString("telefono"));
    persona.setEmail(rs.getString("email"));

    alumno.setPersona(persona);

    return alumno;
}






    public Alumno insertarAlumno(Alumno alumno) throws SQLException {
    String query = "CALL InsertarAlumno(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    ConexionMysql connMysql = new ConexionMysql();
    Connection conn = null;
    CallableStatement cstmt = null;

    try {
        conn = connMysql.open();
        conn.setAutoCommit(false); // Iniciar la transacción

        cstmt = (CallableStatement) conn.prepareCall(query);

        Persona persona = alumno.getPersona();

        // Imprimir el objeto Alumno deserializado
        System.out.println("Alumno deserializado: " + alumno);
        System.out.println("Materias del alumno: " + alumno.getMaterias());

        // Parámetros del procedimiento almacenado InsertarAlumno
        cstmt.setString(1, persona.getNombre());
        cstmt.setString(2, persona.getApellidoPaterno());
        cstmt.setString(3, persona.getApellidoMaterno());
        cstmt.setString(4, persona.getGenero());
        cstmt.setString(5, persona.getFechaNacimiento());
        cstmt.setString(6, persona.getRfc());
        cstmt.setString(7, persona.getCurp());
        cstmt.setString(8, persona.getDomicilio());
        cstmt.setString(9, persona.getCodigoPostal());
        cstmt.setString(10, persona.getCiudad());
        cstmt.setString(11, persona.getEstado());
        cstmt.setString(12, persona.getTelefono());
        cstmt.setString(13, persona.getEmail());
        cstmt.setString(14, alumno.getMatricula());
        cstmt.setInt(15, alumno.getEstatus());
        cstmt.setString(16, alumno.getModalidad());
        cstmt.setString(17, alumno.getFechaIngreso());

        // Convertir la lista de materias a una cadena separada por comas
        String materias = alumno.getMaterias().stream()
                .map(materia -> String.valueOf(materia.getIdMateria()))
                .collect(Collectors.joining(","));
        cstmt.setString(18, materias);

        cstmt.execute(); // Ejecutar el procedimiento almacenado

        conn.commit(); // Confirmar la transacción

        return alumno;
    } catch (SQLException ex) {
        if (conn != null) {
            conn.rollback(); // Revertir la transacción en caso de error
        }
        ex.printStackTrace();
        throw ex;
    } finally {
        if (cstmt != null) {
            cstmt.close();
        }
        if (conn != null) {
            conn.setAutoCommit(true); // Restaurar el modo de autocommit
            conn.close();
        }
        connMysql.close();
    }
}




public void actualizarAlumno(Alumno alumno) throws SQLException {
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        CallableStatement stmt = null;

        try {
            conn.setAutoCommit(false);

            // Obtener idPersona
            String sqlPersona = "SELECT idPersona FROM alumno WHERE idAlumno = ?";
            stmt = conn.prepareCall(sqlPersona);
            stmt.setInt(1, alumno.getIdAlumno());
            stmt.execute();
            stmt.getResultSet().next();
            int idPersona = stmt.getResultSet().getInt("idPersona");

            // Llamar al stored procedure
            String sql = "{CALL ActualizarAlumno(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            stmt = conn.prepareCall(sql);
            Persona persona = alumno.getPersona();
            stmt.setInt(1, alumno.getIdAlumno());
            stmt.setString(2, persona.getNombre());
            stmt.setString(3, persona.getApellidoPaterno());
            stmt.setString(4, persona.getApellidoMaterno());
            stmt.setString(5, persona.getGenero());
            stmt.setString(6, persona.getFechaNacimiento());
            stmt.setString(7, persona.getRfc());
            stmt.setString(8, persona.getCurp());
            stmt.setString(9, persona.getDomicilio());
            stmt.setString(10, persona.getCodigoPostal());
            stmt.setString(11, persona.getCiudad());
            stmt.setString(12, persona.getEstado());
            stmt.setString(13, persona.getTelefono());
            stmt.setString(14, persona.getEmail());
            stmt.setString(15, alumno.getMatricula());
            stmt.setInt(16, alumno.getEstatus());
            stmt.setString(17, alumno.getModalidad());
            stmt.setString(18, alumno.getFechaIngreso());

            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                conn.rollback();
            }
            throw ex;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
            connMySQL.close();
        }
    }


public boolean existeMatricula(String matricula) throws SQLException {
        String sql = "SELECT COUNT(*) FROM alumno WHERE matricula = ?";
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, matricula);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        pstmt.close();
        connMySQL.close();
        return count > 0;
    }






    






     
     public Alumno getAlumnoById(int idAlumno) throws SQLException {
        String sql = "SELECT "
                   + "    a.idAlumno, "
                   + "    a.matricula, "
                   + "    a.estatus, "
                   + "    a.modalidad, "
                   + "    a.fechaIngreso, "
                   + "    a.idPersona, "
                   + "    GROUP_CONCAT(DISTINCT m.nombre ORDER BY m.nombre ASC) AS materias, "
                   + "    pe.idPersona, "
                   + "    pe.nombre, "
                   + "    pe.apellidoPaterno, "
                   + "    pe.apellidoMaterno, "
                   + "    pe.genero, "
                   + "    pe.fechaNacimiento, "
                   + "    pe.rfc, "
                   + "    pe.curp, "
                   + "    pe.domicilio, "
                   + "    pe.codigoPostal, "
                   + "    pe.ciudad, "
                   + "    pe.estado, "
                   + "    pe.telefono, "
                   + "    pe.email "
                   + "FROM "
                   + "    alumno a "
                   + "LEFT JOIN "
                   + "    alumnos_materias am ON a.idAlumno = am.idAlumno "
                   + "LEFT JOIN "
                   + "    materia m ON am.idMateria = m.idMateria "
                   + "INNER JOIN "
                   + "    persona pe ON a.idPersona = pe.idPersona "
                   + "WHERE "
                   + "    a.idAlumno = ? "
                   + "GROUP BY "
                   + "    a.idAlumno, a.matricula, a.estatus, a.modalidad, a.fechaIngreso, a.idPersona, "
                   + "    pe.idPersona, pe.nombre, pe.apellidoPaterno, pe.apellidoMaterno, pe.genero, "
                   + "    pe.fechaNacimiento, pe.rfc, pe.curp, pe.domicilio, pe.codigoPostal, pe.ciudad, pe.estado, pe.telefono, pe.email";

        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idAlumno);
        ResultSet rs = pstmt.executeQuery();

        Alumno alumno = null;
        if (rs.next()) {
            alumno = fillAlumno(rs);
        }

        rs.close();
        pstmt.close();
        connMySQL.close();
        return alumno;
    }

     
}
