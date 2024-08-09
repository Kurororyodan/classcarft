/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.controller.ControllerMateria;
import org.utl.dsm.controller.ControllerSalon;
import org.utl.dsm.model.Materia;
import org.utl.dsm.model.Salon;


/**
 *
 * @author macbookpro
 */
@Path("salon")
public class RestSalon {
    
    
    @Path("insertSalon")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertSalon(@FormParam("salon") @DefaultValue("{}") String s){
        String out = "";
        
        System.out.println(s);
        ControllerSalon cs = new ControllerSalon();
        Gson gson = new Gson();
        try {
            //Desfra
            Salon salon =  gson.fromJson(s, Salon.class);
            cs.insertSalon(salon);
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
    
    @Path("getAll")
@Produces(MediaType.APPLICATION_JSON)
@GET
public Response getALL(@QueryParam("limit") int limit, @QueryParam("skip") int skip) {
    String out;
    List<Salon> salones;

    try {
        ControllerSalon cs = new ControllerSalon();
        salones = cs.getALLS(limit, skip);
        out = new Gson().toJson(salones);
        return Response.status(Response.Status.OK).entity(out).build();
    } catch (SQLException sqlException) {
        sqlException.printStackTrace();
        out = "{\"error\":\"Error de SQL al obtener.\"}\n";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    } catch (Exception e) {
        e.printStackTrace();
        out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    }
}


@Path("UpdateSalon")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProd(@FormParam("salon") @DefaultValue("{}") String s) {
        String out = "";

        System.out.println(s);
        ControllerSalon cs = new ControllerSalon();
        Gson gson = new Gson();
        try {
            // Deserializar el objeto Producto desde el JSON
            Salon salon = gson.fromJson(s, Salon.class);

            // Llamar al método de actualización en lugar de inserción
            cs.actualizarSalon(salon);

            out = """
            {"result":"Objeto actualizado"}
            """;
        } catch (Exception ex) {
            ex.printStackTrace();
            out = """
            {"result":"Error en la transacción"}
            """;
        }
        return Response.ok(out).build();
    }
    
    
    
    
    
    @Path("update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@FormParam("s")  @DefaultValue("0") String salon) {
        Gson objGson = new Gson();
        Salon s = objGson.fromJson(salon, Salon.class);

        String out;
        ControllerSalon objCE = new ControllerSalon();

        try {
            int resultado = objCE.update(s);

            if (resultado > 0) {
                out = "{\"result\":\"Salon actualizada exitosamente\"}";
            } else {
                out = "{\"error\":\"No se encontró el salon a actualizar\"}";
            }

        } catch (SQLException ex) {
            out = "{\"error\":\"Error al actualizar el salon: " + ex.getMessage() + "\"}";
        }

        return Response.ok(out).build();
    }

     @Path("deleteSalon")
@POST
@Produces(MediaType.APPLICATION_JSON)
public Response deleteSalon(@FormParam("salon") @DefaultValue("{}") String s) {
    String out = "";
    System.out.println(s);
    ControllerSalon cs = new ControllerSalon();
    Gson gson = new Gson();
    try {
        // Deserializar el objeto Materia desde el JSON
        Salon salon = gson.fromJson(s, Salon.class);

        // Llamar al método de eliminación
        int idEliminado = cs.deleteSalon(salon);

        if (idEliminado > 0) {
            out = """
            {"result":"Objeto eliminado"}
            """;
        } else {
            out = """
            {"result":"Error en la transacción"}
            """;
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        out = """
        {"result":"Error en la transacción"}
        """;
    }
    return Response.ok(out).build();
}
    
    
    @Path("upSalon")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response upSalon(@FormParam("idSalon") int idSalon) {
        ControllerSalon cs = new ControllerSalon();
        String out = "";

        try {
            // Llamar al método de actualización lógica
            cs.upSalon(idSalon);

            out = """
            {"result":"Objeto actualizado"}
        """;
        } catch (Exception ex) {
            ex.printStackTrace();
            out = """
            {"result":"Error en la transacción"}
        """;
            return Response.serverError().entity(out).build();
        }
        return Response.ok(out).build();
    }
    
    
    
     @Path("getSalonDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getSalonDetails(@QueryParam("id") int id) {
        String out;
        Salon salon;

        try {
            ControllerSalon cs = new ControllerSalon();
            salon = cs.getSalonDetails(id);

            if (salon != null) {
                out = new Gson().toJson(salon);
                return Response.status(Response.Status.OK).entity(out).build();
            } else {
                out = "{\"error\":\"Producto no encontrado.\"}\n";
                return Response.status(Response.Status.NOT_FOUND).entity(out).build();
            }
        } catch (SQLException sQLException) {
            sQLException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener detalles del producto.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
}