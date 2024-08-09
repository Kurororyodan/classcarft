/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.controller;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.db.ConexionMysql;
import org.utl.dsm.model.Materia;
/**
 *
 * @author macbookpro
 */
public class ControllerMateria {
    public Materia insertMateria(Materia s){
        String query ="CALL sp_insert_materia(?, ?, ?)";
        
        try {
            ConexionMysql connMySQL = new ConexionMysql();
            Connection conn = connMySQL.open();
            CallableStatement cstmt = (CallableStatement) conn.prepareCall(query);

            Materia materia = s.getMateria();

            // Establecer los parámetros de entrada
            cstmt.setString(1, s.getNombre());
            cstmt.setString(2, s.getClave());
            cstmt.setInt(3, s.getHoras());


            // Registrar los parámetros de salida antes de ejecutar el procedimiento almacenado

            // Ejecutar el procedimiento almacenado
            cstmt.execute();

            // Aquí puedes acceder a los valores de salida si es necesario
            cstmt.close();
            conn.close();
            connMySQL.close();

            return s;

        } catch (Exception ex) {
            ex.printStackTrace();
            return s;
        }
    }
    
  public List<Materia> buscarMateria(String query) throws SQLException {
    String sql = "CALL BuscarMateria(?)";
    ConexionMysql connMySQL = new ConexionMysql();
    Connection conn = connMySQL.open();
    java.sql.CallableStatement stmt = conn.prepareCall(sql);

    stmt.setString(1, query);

    List<Materia> materias = new ArrayList<>();
    try {
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Materia materia = new Materia();
            materia.setIdMateria(rs.getInt("idMateria"));
            materia.setNombre(rs.getString("nombre"));
            materia.setClave(rs.getString("clave"));
            materia.setHoras(rs.getInt("horas"));
            materias.add(materia);
        }
        rs.close();
    } catch (SQLException e) {
        throw new SQLException("Error al ejecutar el procedimiento almacenado: " + e.getMessage(), e);
    } finally {
        stmt.close();
        connMySQL.close();
    }

    return materias;
}




    
    public List<Materia> getAll() throws SQLException {
        String sql = "SELECT * FROM vista_materias";
        // nos vamos a conectar a la Base de Datos:
        ConexionMysql connMySQL = new ConexionMysql();
        // Abrimos la conexion con la Base de Datos:
        Connection conn = connMySQL.open();
        // ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        List<Materia> materias = new ArrayList<>();
        while (rs.next()) {
            materias.add(fill(rs));
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return materias;
    }
    
    public Materia fill(ResultSet rs) throws SQLException{
        Materia s = new Materia();
        s.setIdMateria(rs.getInt("idMateria"));
        s.setNombre(rs.getString("nombre"));
        s.setClave(rs.getString("clave"));
        s.setHoras(rs.getInt("horas"));

        
        return s;
    }
    
    public List<Materia> getALLS(int limit, int skip) throws SQLException{
        String sql = "SELECT * FROM materia LIMIT ? OFFSET ?;";
        ConexionMysql connMysql = new ConexionMysql();
        Connection conn = connMysql.open();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Materia> materias = new ArrayList<>();

        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, skip);
            rs = pstmt.executeQuery();

            while(rs.next()){
                materias.add(fill(rs));
            }
            return materias;
        } catch (SQLException e) {
        e.printStackTrace();
        throw e;
    } finally {
        if (rs != null) {
            rs.close();
        }
        if (pstmt != null) {
            pstmt.close();
        }
        connMysql.close();
    }
    }
    public Materia getMateriaDetails(int id) throws SQLException{
      String sql = "SELECT * FROM materia WHERE idMateria = ?;";
      ConexionMysql connMysql = new ConexionMysql();
      Connection conn = connMysql.open();
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      try{
          pstmt = conn.prepareStatement(sql);
          pstmt.setInt(1, id);
          rs = pstmt.executeQuery();

          if(rs.next()){
              return fill(rs);
          } else {
              return  null;
          }
      }catch(SQLException e){
          e.printStackTrace();
          throw e;
      } finally{
          if (rs != null){
              rs.close();
          }
          if ( pstmt != null){
              pstmt.close();
          }
          connMysql.close();
      }
    }

    public int actualizarMateria(Materia s) throws SQLException{
        //1. Generar la sentencia SQL
        String query = "{call modificarMateria(?,?,?,?)}";
        //2. Crear la conexión a la BD
        ConexionMysql conMySQL = new ConexionMysql();
        //3. Se abre la conexión
        Connection conn = conMySQL.open();
        //4. Crear el statement que llevara la consulta
        java.sql.CallableStatement cstm = conn.prepareCall(query);
        //5. LLenar todos los parametros de la llamada al Procedure


        cstm.setInt(1, s.getIdMateria());
        cstm.setString(2, s.getNombre());
        cstm.setString(3, s.getClave());
        cstm.setInt(4, s.getHoras());


        cstm.execute();

        cstm.close();
        conn.close();
        conMySQL.close();

        return s.getIdMateria();


    }

    public int update(Materia s) throws SQLException {
    //1. Generar la sentencia SQL
    String query = "{call modificarMateria(?,?,?,?)}";
    //2. Crear la conexión a la BD
    ConexionMysql conMySQL = new ConexionMysql();
    //3. Se abre la conexión
    Connection conn = conMySQL.open();
    //4. Crear el statement que llevará la consulta
    java.sql.CallableStatement cstm = conn.prepareCall(query);
    //5. Llenar todos los parámetros de la llamada al procedimiento

    cstm.setInt(1, s.getIdMateria());
    cstm.setString(2, s.getNombre());
    cstm.setString(3, s.getClave());
    cstm.setInt(4, s.getHoras());

    cstm.execute();

    cstm.close();
    conn.close();
    conMySQL.close();

    return s.getIdMateria();
}


    public int deleteMateria(Materia s) throws SQLException{
        //1. Generar la sentencia SQL
        String query = "{call sp_delete_materia(?)}";
        //2. Crear la conexión a la BD
        ConexionMysql conMySQL = new ConexionMysql();
        //3. Se abre la conexión
        Connection conn = conMySQL.open();
        //4. Crear el statement que llevara la consulta
        java.sql.CallableStatement cstm = conn.prepareCall(query);
        //5. LLenar todos los parametros de la llamada al Procedure


        cstm.setInt(1, s.getIdMateria());

        cstm.execute();

        cstm.close();
        conn.close();
        conMySQL.close();

        return s.getIdMateria();


    }







     public boolean upMateria(int idMateria) {
    String query = "UPDATE materia SET estatus = 1 WHERE idMateria = ?;";

    try {
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setInt(1, idMateria);

        int rowsAffected = pstmt.executeUpdate();

        pstmt.close();
        conn.close();
        connMySQL.close();

        // Si se ha afectado al menos una fila, la eliminación lógica fue exitosa
        return rowsAffected > 0;

    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
    }

    }
