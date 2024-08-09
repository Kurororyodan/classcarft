/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.controller;
import java.sql.CallableStatement;
import  java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.utl.dsm.db.ConexionMysql;
import org.utl.dsm.model.Usuario;

/**
 *
 * @author luis rosas
 */
public class ControllerLogin {
    public Usuario insertUsuario(Usuario u){
        String query ="CALL InsertarUsuario(?, ?)";
        
        try {
            ConexionMysql connMySQL = new ConexionMysql();
            Connection conn = connMySQL.open();
            CallableStatement cstmt = (CallableStatement) conn.prepareCall(query);

            
            
            

            // Establecer los parámetros de entrada
            cstmt.setString(1, u.getNombreUsuario());
            cstmt.setString(2, u.getContrasenia());

            

            // Registrar los parámetros de salida antes de ejecutar el procedimiento almacenado

            // Ejecutar el procedimiento almacenado
            cstmt.execute();

            // Aquí puedes acceder a los valores de salida si es necesario
            cstmt.close();
            conn.close();
            connMySQL.close();

            return u;

        } catch (Exception ex) {
            ex.printStackTrace();
            return u;
        }
    }
    
    public void login(Usuario u) throws SQLException{
        String query= """
                      SELECT idUsuario FROM usuario WHERE nombreUsuario="%S" && contrasenia="%S";
                      """;
        query = String.format(query, u.getNombreUsuario(),u.getContrasenia());
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if(rs.next()){
            u.setIdUsuario(rs.getInt("idUsuario"));
        }
        rs.close();
        stmt.close();
        conn.close();
        connMySQL.close();
    }
    
    public void saveToken(Usuario u) throws SQLException{
        String query = """
                       UPDATE usuario SET token ="%S" WHERE idUsuario=%S;
                       """;
        query = String.format(query, u.getToken(),u.getIdUsuario());
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        Statement stmt = conn.createStatement();
        stmt.execute(query);
        stmt.close();
        conn.close();
        connMySQL.close();
    }
    
    public void deleteToken(String token) throws SQLException{
        String query="""
                     UPDATE usuario SET token ="" WHERE token = "%S";                                                                                                                                                                                                                                                           
                     """;
        query =String.format(query, token);
        
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        Statement stmt = conn.createStatement();
        stmt.execute(query);
        stmt.close();
        conn.close();
        connMySQL.close();
    }
    
    public boolean authToken(String token) throws SQLException{
        boolean result=false;
        String query = "SELECT * FROM usuario WHERE token=?";
        
        ConexionMysql connMySQL = new ConexionMysql();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1,token);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            result=true;
        }
        
        rs.close();
        pstmt.close();
        conn.close();
        connMySQL.close();
        
        return result;
    }
    
}
