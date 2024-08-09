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
import org.utl.dsm.model.Salon;

/**
 *
 * @author macbookpro
 */
public class ControllerSalon {
    public Salon insertSalon(Salon s){
        String query ="CALL sp_insert_salon(?, ?)";
        
        try {
            ConexionMysql connMySQL = new ConexionMysql();
            Connection conn = connMySQL.open();
            CallableStatement cstmt = (CallableStatement) conn.prepareCall(query);

            Salon salon = s.getSalon();

            // Establecer los parámetros de entrada
            cstmt.setString(1, s.getNombre());
            cstmt.setString(2, s.getUbicacion());

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
    
    
    
    public List<Salon> getAll() throws SQLException {
        String sql = "SELECT * FROM vista_salones";
        // nos vamos a conectar a la Base de Datos:
        ConexionMysql connMySQL = new ConexionMysql();
        // Abrimos la conexion con la Base de Datos:
        Connection conn = connMySQL.open();
        // ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();
        List<Salon> salones = new ArrayList<>();
        while (rs.next()) {
            salones.add(fill(rs));
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return salones;
    }
    
    public Salon fill(ResultSet rs) throws SQLException{
        Salon s = new Salon();
        s.setIdSalon(rs.getInt("idSalon"));
        s.setNombre(rs.getString("nombre"));
        s.setUbicacion(rs.getString("ubicacion"));
        
        return s;
    }
    
    public List<Salon> getALLS(int limit, int skip) throws SQLException{
        String sql = "SELECT * FROM salon LIMIT ? OFFSET ?;";
        ConexionMysql connMysql = new ConexionMysql();
        Connection conn = connMysql.open();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Salon> salones = new ArrayList<>();

        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, skip);
            rs = pstmt.executeQuery();

            while(rs.next()){
                salones.add(fill(rs));
            }
            return salones;
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
    public Salon getSalonDetails(int id) throws SQLException{
      String sql = "SELECT * FROM salon WHERE idSalon = ?;";
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

    public int actualizarSalon(Salon s) throws SQLException{
        //1. Generar la sentencia SQL
        String query = "{call modificarSalon(?,?,?)}";
        //2. Crear la conexión a la BD
        ConexionMysql conMySQL = new ConexionMysql();
        //3. Se abre la conexión
        Connection conn = conMySQL.open();
        //4. Crear el statement que llevara la consulta
        java.sql.CallableStatement cstm = conn.prepareCall(query);
        //5. LLenar todos los parametros de la llamada al Procedure


        cstm.setInt(1, s.getIdSalon());
        cstm.setString(2, s.getNombre());
        cstm.setString(3, s.getUbicacion());

        cstm.execute();

        cstm.close();
        conn.close();
        conMySQL.close();

        return s.getIdSalon();


    }

    public int update(Salon s) throws SQLException{
        //1. Generar la sentencia SQL
        String query = "{call modificarSalon(?,?,?)}";
        //2. Crear la conexión a la BD
        ConexionMysql conMySQL = new ConexionMysql();
        //3. Se abre la conexión
        Connection conn = conMySQL.open();
        //4. Crear el statement que llevara la consulta
        java.sql.CallableStatement cstm = conn.prepareCall(query);
        //5. LLenar todos los parametros de la llamada al Procedure


        cstm.setInt(1, s.getIdSalon());
        cstm.setString(2, s.getNombre());
        cstm.setString(3, s.getUbicacion());

        cstm.execute();

        cstm.close();
        conn.close();
        conMySQL.close();

        return s.getIdSalon();


    }

     public int deleteSalon(Salon s) throws SQLException{
        //1. Generar la sentencia SQL
        String query = "{call sp_delete_salon(?)}";
        //2. Crear la conexión a la BD
        ConexionMysql conMySQL = new ConexionMysql();
        //3. Se abre la conexión
        Connection conn = conMySQL.open();
        //4. Crear el statement que llevara la consulta
        java.sql.CallableStatement cstm = conn.prepareCall(query);
        //5. LLenar todos los parametros de la llamada al Procedure


        cstm.setInt(1, s.getIdSalon());

        cstm.execute();

        cstm.close();
        conn.close();
        conMySQL.close();

        return s.getIdSalon();


    }

     public boolean upSalon(int idSalon) {
    String query = "UPDATE salon SET estatus = 1 WHERE idSalon = ?;";

    try {
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setInt(1, idSalon);

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
