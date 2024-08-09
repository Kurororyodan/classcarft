/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.rest;
import com.google.gson.Gson;
import static com.google.gson.internal.bind.TypeAdapters.URI;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.dsm.controller.ControllerLogin;

import java.net.URI;
import java.sql.SQLException;
import org.utl.dsm.model.Usuario;

/**
 *
 * @author luis rosas
 */
@Path("login")
public class RestLogin {
    @Path("insertUsuario")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertUsuario(@FormParam("usuario") @DefaultValue("{}") String u){
        String out = "";
        
        
        System.out.println(u);
        ControllerLogin cs = new ControllerLogin();
        Gson gson = new Gson();
        try {
            //Desfra
            Usuario usuario =  gson.fromJson(u, Usuario.class);
           
            cs.insertUsuario(usuario);
            out="""
                {"result":"Objeto insertado"}
                """;
        } catch (Exception ex) {
            ex.printStackTrace();
            out="""
                {"result":"Error en la transaccion"}
                """;
        }
        return Response.ok(out).build();
   }
    
   @Path("login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(@FormParam("usuario") @DefaultValue("") String usuario){
        Gson objGS = new Gson();
        Usuario u = objGS.fromJson(usuario,Usuario.class);
        String out="";
        
        
        
        ControllerLogin objCA = new ControllerLogin();
        try {
            objCA.login(u);
            if(u.getIdUsuario()!=0){
                u.setToken();
                objCA.saveToken(u);
                
            }
            out=objGS.toJson(u);
        } catch (SQLException ex) {
            out="""
                {"error":"Problemas en el servidor de base de datos"}
                """;
        }
        return Response.ok(out).build();
    }
    
    @Path("logout")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response logOut(@FormParam("t") @DefaultValue("") String t){
        ControllerLogin objCA = new ControllerLogin();
        String out="";
        try {
            objCA.deleteToken(t);
            out="""
                {"result":"OK"}
                """;
        } catch (SQLException ex) {
            out="""
                {"error":"Error al cerrar sesión, consulta al administrador del Sistema"}
                """;
        }
        return Response.ok(out).build();
    }
    
}
