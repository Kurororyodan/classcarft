package org.utl.dsm.controller;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.db.ConexionMysql;
import org.utl.dsm.model.DiaDisponible;
import org.utl.dsm.model.Materia;
import org.utl.dsm.model.Profesor;
import org.utl.dsm.model.Persona;
import java.sql.Connection;
import java.sql.SQLException;
import org.utl.dsm.db.ConexionMysql;
import org.utl.dsm.model.DiaDisponible;
import org.utl.dsm.model.Materia;
import org.utl.dsm.model.Persona;
import org.utl.dsm.model.Profesor;
import org.utl.dsm.model.Profesor2;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Controllerprofebrian {
 public List<Profesor2> getAll() throws SQLException {
        String sql = "SELECT "
                   + "    p.idProfesor, "
                   + "    p.codigo, "
                   + "    p.disponibilidad_horaria, "
                   + "    p.estatus, "
                   + "    p.fechaIngreso, "
                   + "    p.idPersona, "
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
                   + "    pe.email, "
                   + "    GROUP_CONCAT(DISTINCT CONCAT(m.idMateria, ':', m.nombre, ':', m.horas) SEPARATOR '|') AS materias, "
                   + "    GROUP_CONCAT(DISTINCT CONCAT(d.idDia, ':', d.nombre, ':', pd.horaInicio, ':', pd.horaFin) SEPARATOR '|') AS diasDisponibles "
                   + "FROM "
                   + "    profesores p "
                   + "INNER JOIN "
                   + "    persona pe ON p.idPersona = pe.idPersona "
                   + "LEFT JOIN "
                   + "    profesores_materias pm ON p.idProfesor = pm.idProfesor "
                   + "LEFT JOIN "
                   + "    materia m ON pm.idMateria = m.idMateria "
                   + "LEFT JOIN "
                   + "    profesores_dias pd ON p.idProfesor = pd.idProfesor "
                   + "LEFT JOIN "
                   + "    dias_disponibles d ON pd.idDia = d.idDia "
                   + "GROUP BY "
                   + "    p.idProfesor, p.codigo, p.disponibilidad_horaria, p.estatus, p.fechaIngreso, p.idPersona, "
                   + "    pe.nombre, pe.apellidoPaterno, pe.apellidoMaterno, pe.genero, pe.fechaNacimiento, pe.rfc, pe.curp, "
                   + "    pe.domicilio, pe.codigoPostal, pe.ciudad, pe.estado, pe.telefono, pe.email;";

        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Profesor2> profesores = new ArrayList<>();
        while (rs.next()) {
            profesores.add(fillProfesor(rs));
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return profesores;
    }

    private Profesor2 fillProfesor(ResultSet rs) throws SQLException {
        Profesor2 profesor = new Profesor2();
        Persona persona = new Persona();

        profesor.setIdProfesor(rs.getInt("idProfesor"));
        profesor.setCodigo(rs.getString("codigo"));
        profesor.setDisponibilidad_horaria(rs.getInt("disponibilidad_horaria"));
        profesor.setEstatus(rs.getInt("estatus"));
        profesor.setFechaIngreso(rs.getString("fechaIngreso"));

        // Asignar datos de persona
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

        // Procesar materias
        List<Materia> materias = new ArrayList<>();
        String materiasStr = rs.getString("materias");
        if (materiasStr != null && !materiasStr.isEmpty()) {
            String[] materiasArray = materiasStr.split("\\|");
            for (String matStr : materiasArray) {
                String[] matData = matStr.split(":");
                Materia materia = new Materia();
                materia.setIdMateria(Integer.parseInt(matData[0]));
                materia.setNombre(matData[1]);
                materia.setHoras(Integer.parseInt(matData[2]));
                materias.add(materia);
            }
        }
        profesor.setMaterias(materias);

List<DiaDisponible> diasDisponibles = new ArrayList<>();
String diasDisponiblesStr = rs.getString("diasDisponibles");
System.out.println("diasDisponiblesStr: " + diasDisponiblesStr); // Debugging line

if (diasDisponiblesStr != null && !diasDisponiblesStr.isEmpty()) {
    String[] diasArray = diasDisponiblesStr.split("\\|");
    System.out.println("diasArray length: " + diasArray.length); // Debugging line
    
    for (String diaStr : diasArray) {
        System.out.println("diaStr: " + diaStr); // Debugging line
        String[] diaData = diaStr.split(":");
        
        if (diaData.length >= 6) { // Al menos debe haber 6 elementos
            try {
                DiaDisponible dia = new DiaDisponible();
                dia.setIdDia(Integer.parseInt(diaData[0]));
                dia.setNombre(diaData[1]);

                // Unir las partes de horaInicio y horaFin adecuadamente
                dia.setHoraInicio(diaData[2] + ":" + diaData[3] + ":" + diaData[4]);
                dia.setHoraFin(diaData[5] + ":" + diaData[6] + ":" + diaData[7]);

                // Log para verificar valores
                System.out.println("Procesando Dia: " + dia.getNombre() + ", Inicio: " + dia.getHoraInicio() + ", Fin: " + dia.getHoraFin()); // Debugging line

                diasDisponibles.add(dia);
            } catch (NumberFormatException e) {
                System.err.println("Error de formato numérico en: " + diaStr);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Error en el formato de horas para: " + diaStr);
            }
        } else {
            System.err.println("Formato inesperado para dia: " + diaStr); // Debugging line
        }
    }
} else {
    System.err.println("diasDisponiblesStr es nulo o vacío"); // Debugging line
}
profesor.setDisponibilidad_dias(diasDisponibles);

return profesor;

    }

    public List<Materia> getMateriasByProfesor(int idProfesor) throws SQLException {
    String sql = "SELECT m.idMateria, m.nombre, m.clave, m.horas FROM materia m " +
                 "INNER JOIN profesores_materias pm ON m.idMateria = pm.idMateria " +
                 "WHERE pm.idProfesor = ?";
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

    
    
    
     public void activarProfesor(int idProfesor) throws SQLException {
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        String sql = "CALL ActivarProfesor(?)"; // Nombre del procedimiento almacenado

        try (java.sql.CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idProfesor);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            connMySQL.close();
        }
    }
    
 
     
  public void DesactivarProfesor(int idProfesor) throws SQLException {
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        String sql = "CALL EliminarProfesor(?)"; // Nombre del procedimiento almacenado

        try (java.sql.CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idProfesor);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            connMySQL.close();
        }
    }
  
   public List<Profesor2> buscarProfesor(
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
        String codigo,
        Integer disponibilidad_horaria, // Cambiado a Integer
        Integer estatus,
        String fechaIngreso
) throws SQLException {
    String sql = "CALL BuscarProfesor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
        stmt.setNull(9, Types.INTEGER);
    }
    stmt.setString(10, ciudad);
    stmt.setString(11, estado);
    stmt.setString(12, telefono);
    stmt.setString(13, email);
    stmt.setString(14, codigo);
    if (disponibilidad_horaria != null) {
        stmt.setInt(15, disponibilidad_horaria);
    } else {
        stmt.setNull(15, Types.INTEGER);
    }
    if (estatus != null) {
        stmt.setInt(16, estatus);
    } else {
        stmt.setNull(16, Types.INTEGER);
    }
    if (fechaIngreso != null) {
        stmt.setString(17, fechaIngreso);
    } else {
        stmt.setNull(17, Types.DATE);
    }

    ResultSet rs = stmt.executeQuery();
    List<Profesor2> profesores = new ArrayList<>();
    while (rs.next()) {
        Profesor2 profesor = fillProfesornumero2(rs);
        profesores.add(profesor);
    }

    rs.close();
    stmt.close();
    connMySQL.close();

    return profesores;
}

    
     private Profesor2 fillProfesornumero2(ResultSet rs) throws SQLException {
        Profesor2 profesor = new Profesor2();
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

        return profesor;
    }
     
      public boolean codigoExiste(String codigo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM profesores WHERE codigo = ?";
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, codigo);
        ResultSet rs = pstmt.executeQuery();
        boolean exists = false;
        if (rs.next()) {
            exists = rs.getInt(1) > 0;
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return exists;
    }
     
      
 public Profesor2 insertarProfesor(Profesor2 profesor) throws SQLException {
    if (codigoExiste(profesor.getCodigo())) {
        throw new SQLException("Duplicate entry for codigo: " + profesor.getCodigo());
    }

    String query = "CALL InsertarProfesor(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    ConexionMysql connMysql = new ConexionMysql();
    Connection conn = null;
    CallableStatement cstmt = null;

    try {
        conn = connMysql.open();
        conn.setAutoCommit(false); // Iniciar la transacción

        cstmt = (CallableStatement) conn.prepareCall(query);

        Persona persona = profesor.getPersona();

        // Parámetros del procedimiento almacenado InsertarProfesor
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
        cstmt.setString(14, profesor.getCodigo());
        cstmt.setInt(15, profesor.getDisponibilidad_horaria());
        cstmt.setInt(16, profesor.getEstatus());
        cstmt.setString(17, profesor.getFechaIngreso());

        // Convertir la lista de materias a una cadena separada por comas
        String materias = profesor.getMaterias().stream()
                .map(m -> String.valueOf(m.getIdMateria()))
                .collect(Collectors.joining(","));
        cstmt.setString(18, materias);

        // Convertir la lista de días disponibles a una cadena separada por comas
        String diasDisponibles = profesor.getDisponibilidad_dias().stream()
                .map(DiaDisponible::getNombre)
                .collect(Collectors.joining(","));
        cstmt.setString(19, diasDisponibles);
        System.out.println("Dias Disponibles: " + diasDisponibles); // Depuración

        // Convertir la lista de horas de inicio a una cadena separada por comas
        String horasInicio = profesor.getDisponibilidad_dias().stream()
                .map(DiaDisponible::getHoraInicio)
                .collect(Collectors.joining(","));
        cstmt.setString(20, horasInicio);
        System.out.println("Horas Inicio: " + horasInicio); // Depuración

        // Convertir la lista de horas de fin a una cadena separada por comas
        String horasFin = profesor.getDisponibilidad_dias().stream()
                .map(DiaDisponible::getHoraFin)
                .collect(Collectors.joining(","));
        cstmt.setString(21, horasFin);
        System.out.println("Horas Fin: " + horasFin); // Depuración

        // Ejecutar el procedimiento almacenado
        cstmt.execute();

        // Confirmar la transacción
        conn.commit();

        return profesor;
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


  public void actualizarProfesor(Profesor2 profesor) throws SQLException {
    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    CallableStatement stmt = null;

    try {
        conn.setAutoCommit(false);

        // Llamar al stored procedure para actualizar el profesor
        String sql = "{CALL ActualizarProfesor(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        stmt = (CallableStatement) conn.prepareCall(sql);
        Persona persona = profesor.getPersona();
        stmt.setString(1, persona.getNombre());
        stmt.setString(2, persona.getApellidoPaterno());
        stmt.setString(3, persona.getApellidoMaterno());
        stmt.setString(4, persona.getGenero());
        stmt.setString(5, persona.getFechaNacimiento());
        stmt.setString(6, persona.getRfc());
        stmt.setString(7, persona.getCurp());
        stmt.setString(8, persona.getDomicilio());
        stmt.setString(9, persona.getCodigoPostal());
        stmt.setString(10, persona.getCiudad());
        stmt.setString(11, persona.getEstado());
        stmt.setString(12, persona.getTelefono());
        stmt.setString(13, persona.getEmail());
        stmt.setInt(14, profesor.getIdProfesor());
        stmt.setString(15, profesor.getCodigo());
        stmt.setInt(16, profesor.getDisponibilidad_horaria());
        stmt.setInt(17, profesor.getEstatus());
        stmt.setString(18, profesor.getFechaIngreso());

        // Convertir listas de materias y días disponibles a cadenas separadas por comas
        String materias = profesor.getMaterias().stream()
                .map(m -> String.valueOf(m.getIdMateria()))
                .collect(Collectors.joining(","));
        stmt.setString(19, materias);

        // Convertir la lista de días disponibles y sus horarios a cadenas separadas por comas
        List<String> dias = new ArrayList<>();
        List<String> horasInicio = new ArrayList<>();
        List<String> horasFin = new ArrayList<>();

        for (DiaDisponible dia : profesor.getDisponibilidad_dias()) {
            dias.add(dia.getNombre());
            horasInicio.add(dia.getHoraInicio());
            horasFin.add(dia.getHoraFin());
        }

        stmt.setString(20, String.join(",", dias));
        stmt.setString(21, String.join(",", horasInicio));
        stmt.setString(22, String.join(",", horasFin));

        // Ejecutar el procedimiento almacenado
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



    public Profesor2 getProfesorById(int idProfesor) throws SQLException {
    String sql = "SELECT "
               + "    p.idProfesor, "
               + "    p.codigo, "
               + "    p.disponibilidad_horaria, "
               + "    p.estatus, "
               + "    p.fechaIngreso, "
               + "    p.idPersona, "
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
               + "    pe.email, "
               + "    GROUP_CONCAT(DISTINCT CONCAT(m.idMateria, ':', m.nombre, ':', m.horas) SEPARATOR '|') AS materias, "
               + "    GROUP_CONCAT(DISTINCT CONCAT(d.idDia, ':', d.nombre, ':', pd.horaInicio, ':', pd.horaFin) SEPARATOR '|') AS diasDisponibles "
               + "FROM "
               + "    profesores p "
               + "INNER JOIN "
               + "    persona pe ON p.idPersona = pe.idPersona "
               + "LEFT JOIN "
               + "    profesores_materias pm ON p.idProfesor = pm.idProfesor "
               + "LEFT JOIN "
               + "    materia m ON pm.idMateria = m.idMateria "
               + "LEFT JOIN "
               + "    profesores_dias pd ON p.idProfesor = pd.idProfesor "
               + "LEFT JOIN "
               + "    dias_disponibles d ON pd.idDia = d.idDia "
               + "WHERE "
               + "    p.idProfesor = ? "
               + "GROUP BY "
               + "    p.idProfesor, p.codigo, p.disponibilidad_horaria, p.estatus, p.fechaIngreso, p.idPersona, "
               + "    pe.nombre, pe.apellidoPaterno, pe.apellidoMaterno, pe.genero, pe.fechaNacimiento, pe.rfc, pe.curp, "
               + "    pe.domicilio, pe.codigoPostal, pe.ciudad, pe.estado, pe.telefono, pe.email;";

    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    pstmt.setInt(1, idProfesor);
    ResultSet rs = pstmt.executeQuery();

    Profesor2 profesor = null;
    if (rs.next()) {
        profesor = fillProfesor(rs);
    }

    rs.close();
    pstmt.close();
    connMySQL.close();
    return profesor;
}


    
}