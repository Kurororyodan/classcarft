package org.utl.dsm.controller;
import java.sql.CallableStatement;
import  java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.utl.dsm.db.ConexionMysql;
import org.utl.dsm.model.Horario;
/**
 *
 * @author betillo
 */
public class ControllerHorarios {
    public Horario insertHorario(Horario h) {
        String query = "{CALL InsertarHorarioEscolar(?, ?, ?, ?, ?, ?, ?)}";
        
        try {
            ConexionMysql connMySQL = new ConexionMysql();
            Connection conn = connMySQL.open();
            CallableStatement cstmt = conn.prepareCall(query);

            // Establecer los parámetros de entrada
            cstmt.setString(1, h.getIdentificador());
            cstmt.setString(2, h.getDiaSemana());
            cstmt.setString(3, h.getHoraInicio());
            cstmt.setString(4, h.getHoraFin());
            cstmt.setString(5, h.getCurso());
            cstmt.setString(6, h.getProfesor());
            cstmt.setString(7, h.getAula());

            // Ejecutar el procedimiento almacenado
            cstmt.execute();

            cstmt.close();
            conn.close();
            connMySQL.close();

            return h;

        } catch (Exception ex) {
            ex.printStackTrace();
            return h;
        }
    }
    
     public List<Horario>getAll() throws SQLException{
        String sql = "SELECT * FROM HorariosEscolares";
        ConexionMysql connMysql = new ConexionMysql();
        Connection conn = connMysql.open();
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();
        List<Horario>horario=new ArrayList<>();
        while(rs.next()){
            horario.add(fill(rs));
        }
                rs.close();connMysql.close();
return horario;
        
    }
    
     
     
     
     public List<Horario> getIdentificador(String identificador) throws SQLException {
    String sql = "SELECT * FROM HorariosEscolares WHERE identificador = ?";
    List<Horario> peliculas = new ArrayList<>();
    ConexionMysql connMysql = new ConexionMysql();
    try (Connection conn = connMysql.open();
         PreparedStatement pstm = conn.prepareStatement(sql)) {
        pstm.setString(1, identificador);
        try (ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                Horario horario = fill(rs);
                peliculas.add(horario);
            }
        }
    }
    return peliculas;
}
     
     public List<Horario> getByProfesor(String profesor) throws SQLException {
    String sql = "SELECT * FROM HorariosEscolares WHERE profesor LIKE ?";
    List<Horario> horarios = new ArrayList<>();
    ConexionMysql connMysql = new ConexionMysql();
    try (Connection conn = connMysql.open();
         PreparedStatement pstm = conn.prepareStatement(sql)) {
        pstm.setString(1, "%" + profesor + "%");
        try (ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                Horario horario = fill(rs);
                horarios.add(horario);
            }
        }
    }
    return horarios;
}
     
     public List<Horario> getHorariosByProfesorIdentificador(String profesor, String identificador) throws SQLException {
        String sql = "SELECT * FROM HorariosEscolares WHERE profesor = ? AND identificador = ?";
        List<Horario> horarios = new ArrayList<>();
        ConexionMysql connMysql = new ConexionMysql();
        try (Connection conn = connMysql.open();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, profesor);
            pstm.setString(2, identificador);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Horario horario = fill(rs);
                    horarios.add(horario);
                }
            }
        }
        return horarios;
    }


    public Horario fill(ResultSet rs) throws SQLException{
        Horario h = new Horario();
        h.setIdHorario(rs.getInt("idHorario"));
        h.setIdentificador(rs.getString("identificador"));
        h.setDiaSemana(rs.getString("diaSemana"));
        h.setHoraInicio(rs.getString("horaInicio"));
        h.setHoraFin(rs.getString("horaFin"));
        h.setCurso(rs.getString("curso"));
        h.setProfesor(rs.getString("profesor"));
        h.setAula(rs.getString("aula"));
        
        return h;
    }
    
    public Set<String> getAllIdentificadores() throws SQLException {
        String sql = "SELECT DISTINCT identificador FROM HorariosEscolares";
        ConexionMysql connMysql = new ConexionMysql();
        Connection conn = connMysql.open();
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();
        Set<String> identificadores = new HashSet<>();

        while (rs.next()) {
            identificadores.add(rs.getString("identificador"));
        }

        rs.close();
        pstm.close();
        connMysql.close();

        return identificadores;
    }
    
    public Horario updateHorario(Horario h) {
    String query = "{CALL ActualizarHorarioEscolar(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    
    try {
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(query);

        // Establecer los parámetros de entrada
        int i = 1;
        cstmt.setString(i++, h.getIdentificador());
        cstmt.setString(i++, h.getDiaSemana());
        cstmt.setString(i++, h.getHoraInicio());
        cstmt.setString(i++, h.getHoraFin());
        cstmt.setString(i++, h.getCurso());
        cstmt.setString(i++, h.getProfesor());
        cstmt.setString(i++, h.getAula());

        // Parámetros para actualizar
        cstmt.setString(i++, h.getNuevoCurso());
        cstmt.setString(i++, h.getNuevoProfesor());
        cstmt.setString(i++, h.getNuevoAula());

        // Ejecutar el procedimiento almacenado
        cstmt.execute();

        cstmt.close();
        conn.close();
        connMySQL.close();

        return h;

    } catch (Exception ex) {
        ex.printStackTrace();
        return h;
    }
}


}


