package org.utl.dsm.controller;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.utl.dsm.db.ConexionMysql;
import org.utl.dsm.model.DiaDisponible;
import org.utl.dsm.model.DisponibilidadProfesor;
import org.utl.dsm.model.Materia;
import org.utl.dsm.model.Profesor;
import org.utl.dsm.model.Persona;

public class ControllerProfesor {

    public List<Profesor> getAll() throws SQLException {
    String sql = "SELECT "
               + "    p.idProfesor, "
               + "    p.codigo, "
               + "    p.disponibilidad_horaria, "
               + "    p.estatus, "
               + "    p.fechaIngreso, "
               + "    p.idPersona, "
               + "    GROUP_CONCAT(DISTINCT m.nombre ORDER BY m.nombre ASC) AS materias, "
               + "    GROUP_CONCAT(DISTINCT CONCAT(d.nombre, ' (', pd.horaInicio, '-', pd.horaFin, ')') ORDER BY d.nombre ASC) AS disponibilidad_dias, "
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
               + "    profesores p "
               + "LEFT JOIN "
               + "    profesores_materias pm ON p.idProfesor = pm.idProfesor "
               + "LEFT JOIN "
               + "    materia m ON pm.idMateria = m.idMateria "
               + "LEFT JOIN "
               + "    profesores_dias pd ON p.idProfesor = pd.idProfesor "
               + "LEFT JOIN "
               + "    dias_disponibles d ON pd.idDia = d.idDia "
               + "INNER JOIN "
               + "    persona pe ON p.idPersona = pe.idPersona "
               + "GROUP BY "
               + "    p.idProfesor, p.codigo, p.disponibilidad_horaria, p.estatus, p.fechaIngreso, p.idPersona, "
               + "    pe.idPersona, pe.nombre, pe.apellidoPaterno, pe.apellidoMaterno, pe.genero, "
               + "    pe.fechaNacimiento, pe.rfc, pe.curp, pe.domicilio, pe.codigoPostal, pe.ciudad, pe.estado, pe.telefono, pe.email;";

    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    PreparedStatement pstmt = conn.prepareStatement(sql);
    ResultSet rs = pstmt.executeQuery();
    List<Profesor> profesores = new ArrayList<>();
    while (rs.next()) {
        profesores.add(fillProfesor2(rs));
    }
    rs.close();
    pstmt.close();
    connMySQL.close();
    return profesores;
}


    public List<DisponibilidadProfesor> getDiasDisponibles(int idProfesor) throws SQLException {
    String sql = "SELECT d.idDia, d.nombre AS nombreDia, pd.horaInicio, pd.horaFin "
               + "FROM dias_disponibles d "
               + "JOIN profesores_dias pd ON d.idDia = pd.idDia "
               + "WHERE pd.idProfesor = ?";
    
    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, idProfesor);
    ResultSet rs = pstmt.executeQuery();
    List<DisponibilidadProfesor> diasDisponibles = new ArrayList<>();
    while (rs.next()) {
        DisponibilidadProfesor disponibilidad = new DisponibilidadProfesor();
        disponibilidad.setIdDia(rs.getInt("idDia"));
        disponibilidad.setNombreDia(rs.getString("nombreDia"));
        disponibilidad.setHoraInicio(rs.getString("horaInicio"));
        disponibilidad.setHoraFin(rs.getString("horaFin"));
        diasDisponibles.add(disponibilidad);
    }
    rs.close();
    pstmt.close();
    connMySQL.close();
    return diasDisponibles;
}
    public List<Materia> getMaterias(int idProfesor) throws SQLException {
    String sql = "SELECT m.idMateria, m.nombre, m.clave, m.horas "
               + "FROM materia m "
               + "JOIN profesores_materias pm ON m.idMateria = pm.idMateria "
               + "WHERE pm.idProfesor = ?";
    
    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, idProfesor);
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
    
    public List<Materia> getAllMaterias() throws Exception {
        String query = "SELECT idMateria, nombre, clave, horas FROM materia";
        
        List<Materia> materias = new ArrayList<>();
        
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        ResultSet rs = pstmt.executeQuery();
        
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
        conn.close();
        
        return materias;
    }


    
 public List<Profesor> getProfesoresByMateria(int idMateria) throws SQLException {
        String sql = "SELECT " +
                     "    p.idProfesor, " +
                     "    p.codigo, " +
                     "    p.disponibilidad_horaria, " +
                     "    p.estatus, " +
                     "    p.fechaIngreso, " +
                     "    p.idPersona, " +
                     "    pe.nombre, " +
                     "    pe.apellidoPaterno, " +
                     "    pe.apellidoMaterno, " +
                     "    pe.genero, " +
                     "    pe.fechaNacimiento, " +
                     "    pe.rfc, " +
                     "    pe.curp, " +
                     "    pe.domicilio, " +
                     "    pe.codigoPostal, " +
                     "    pe.ciudad, " +
                     "    pe.estado, " +
                     "    pe.telefono, " +
                     "    pe.email, " +
                     "    d.idDia, " +
                     "    d.nombre AS nombreDia, " +
                     "    pd.horaInicio, " +
                     "    pd.horaFin " +
                     "FROM " +
                     "    profesores p " +
                     "INNER JOIN " +
                     "    profesores_materias pm ON p.idProfesor = pm.idProfesor " +
                     "INNER JOIN " +
                     "    materia m ON pm.idMateria = m.idMateria " +
                     "INNER JOIN " +
                     "    profesores_dias pd ON p.idProfesor = pd.idProfesor " +
                     "INNER JOIN " +
                     "    dias_disponibles d ON pd.idDia = d.idDia " +
                     "INNER JOIN " +
                     "    persona pe ON p.idPersona = pe.idPersona " +
                     "WHERE " +
                     "    m.idMateria = ? " +
                     "ORDER BY p.idProfesor";

        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        pstmt.setInt(1, idMateria);
        ResultSet rs = pstmt.executeQuery();
        List<Profesor> profesores = new ArrayList<>();

        while (rs.next()) {
            Profesor profesor = new Profesor();
            Persona persona = new Persona();

            profesor.setIdProfesor(rs.getInt("idProfesor"));
            profesor.setCodigo(rs.getString("codigo"));
            profesor.setDisponibilidad_horaria(rs.getInt("disponibilidad_horaria"));
            profesor.setEstatus(rs.getInt("estatus"));
            profesor.setFechaIngreso(rs.getString("fechaIngreso"));

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

            profesor.setPersona(persona);

            // Obtener disponibilidad_dias
            List<DisponibilidadProfesor> disponibilidad_dias = new ArrayList<>();
            do {
                if (profesor.getIdProfesor() != rs.getInt("idProfesor")) {
                    rs.previous();
                    break;
                }

                DisponibilidadProfesor disponibilidad = new DisponibilidadProfesor();
                disponibilidad.setIdDia(rs.getInt("idDia"));
                disponibilidad.setNombreDia(rs.getString("nombreDia"));
                disponibilidad.setHoraInicio(rs.getString("horaInicio"));
                disponibilidad.setHoraFin(rs.getString("horaFin"));
                disponibilidad_dias.add(disponibilidad);
            } while (rs.next());

            profesor.setDisponibilidad_dias(disponibilidad_dias);
            profesores.add(profesor);
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return profesores;
    }

  
  private Profesor fillProfesor2(ResultSet rs) throws SQLException {
    Profesor profesor = new Profesor();
    Persona persona = new Persona();

    profesor.setIdProfesor(rs.getInt("idProfesor"));
    profesor.setCodigo(rs.getString("codigo"));
    profesor.setDisponibilidad_horaria(rs.getInt("disponibilidad_horaria"));
    profesor.setEstatus(rs.getInt("estatus"));
    profesor.setFechaIngreso(rs.getString("fechaIngreso"));

    // Obtener materias y disponibilidad_dias concatenados y separarlos en listas
    List<Materia> materias = new ArrayList<>();
    List<DisponibilidadProfesor> disponibilidad_dias = new ArrayList<>();

    String materiasStr = rs.getString("materias");
    if (materiasStr != null && !materiasStr.isEmpty()) {
        String[] materiasArr = materiasStr.split(",");
        for (String materiaNombre : materiasArr) {
            Materia materia = new Materia();
            materia.setNombre(materiaNombre.trim());
            materias.add(materia);
        }
    }

    String diasStr = rs.getString("disponibilidad_dias");
    if (diasStr != null && !diasStr.isEmpty()) {
        String[] diasArr = diasStr.split(",");
        for (String diaDisponibilidad : diasArr) {
            String[] partes = diaDisponibilidad.split(" \\(");
            String diaNombre = partes[0].trim();
            String[] horas = partes[1].replace(")", "").split("-");
            DisponibilidadProfesor dia = new DisponibilidadProfesor();
            dia.setNombreDia(diaNombre);
            dia.setHoraInicio(horas[0].trim());
            dia.setHoraFin(horas[1].trim());
            disponibilidad_dias.add(dia);
        }
    }

    profesor.setMaterias(materias);
    profesor.setDisponibilidad_dias(disponibilidad_dias);

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

    profesor.setPersona(persona);

    return profesor;
}
  
  public Materia getMateriaByNombre(String nombreMateria) throws SQLException {
        String sql = "SELECT * FROM materia WHERE nombre = ?";
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nombreMateria);
        ResultSet rs = pstmt.executeQuery();

        Materia materia = null;
        if (rs.next()) {
            materia = new Materia();
            materia.setIdMateria(rs.getInt("idMateria"));
            materia.setNombre(rs.getString("nombre"));
            materia.setClave(rs.getString("clave"));
            materia.setHoras(rs.getInt("horas"));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return materia;
    }

  public Profesor getProfesorByNombre(String nombreCompleto) throws SQLException {
    String sql = "SELECT p.idProfesor, p.codigo, p.disponibilidad_horaria, p.estatus, p.fechaIngreso, " +
                 "pe.idPersona, pe.nombre, pe.apellidoPaterno, pe.apellidoMaterno " +
                 "FROM profesores p " +
                 "INNER JOIN persona pe ON p.idPersona = pe.idPersona " +
                 "WHERE CONCAT(pe.nombre, ' ', pe.apellidoPaterno, ' ', pe.apellidoMaterno) = ?";
    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, nombreCompleto);
    ResultSet rs = pstmt.executeQuery();

    Profesor profesor = null;
    if (rs.next()) {
        profesor = new Profesor();
        profesor.setIdProfesor(rs.getInt("idProfesor"));
        profesor.setCodigo(rs.getString("codigo"));
        profesor.setDisponibilidad_horaria(rs.getInt("disponibilidad_horaria"));
        profesor.setEstatus(rs.getInt("estatus"));
        profesor.setFechaIngreso(rs.getString("fechaIngreso"));
        
        Persona persona = new Persona();
        persona.setIdPersona(rs.getInt("idPersona"));
        persona.setNombre(rs.getString("nombre"));
        persona.setApellidoPaterno(rs.getString("apellidoPaterno"));
        persona.setApellidoMaterno(rs.getString("apellidoMaterno"));
        profesor.setPersona(persona);
    }

    rs.close();
    pstmt.close();
    connMySQL.close();

    return profesor;
}



}
