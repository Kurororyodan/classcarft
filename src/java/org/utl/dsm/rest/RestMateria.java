/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
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
import org.utl.dsm.model.Materia;


/**
 *
 * @author macbookpro
 */
@Path("materia")
public class RestMateria {
    
    
    @Path("insertMateria")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertMateria(@FormParam("materia") @DefaultValue("{}") String s){
        String out = "";
        
        System.out.println(s);
        ControllerMateria cs = new ControllerMateria();
        Gson gson = new Gson();
        try {
            //Desfra
            Materia materia =  gson.fromJson(s, Materia.class);
            cs.insertMateria(materia);
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
    List<Materia> materias;

    try {
        ControllerMateria cs = new ControllerMateria();
        materias = cs.getALLS(limit, skip);
        out = new Gson().toJson(materias);
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


@Path("UpdateMateria")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProd(@FormParam("materia") @DefaultValue("{}") String s) {
        String out = "";

        System.out.println(s);
        ControllerMateria cs = new ControllerMateria();
        Gson gson = new Gson();
        try {
            // Deserializar el objeto Producto desde el JSON
            Materia materia = gson.fromJson(s, Materia.class);

            // Llamar al método de actualización en lugar de inserción
            cs.actualizarMateria(materia);

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
    public Response update(@FormParam("s")  @DefaultValue("0") String materia) {
        Gson objGson = new Gson();
        Materia s = objGson.fromJson(materia, Materia.class);

        String out;
        ControllerMateria objCE = new ControllerMateria();

        try {
            int resultado = objCE.update(s);

            if (resultado > 0) {
                out = "{\"result\":\"Materia actualizada exitosamente\"}";
            } else {
                out = "{\"error\":\"No se encontró el empleado a actualizar\"}";
            }

        } catch (SQLException ex) {
            out = "{\"error\":\"Error al actualizar el empleado: " + ex.getMessage() + "\"}";
        }

        return Response.ok(out).build();
    }




    @Path("deleteMateria")
@POST
@Produces(MediaType.APPLICATION_JSON)
public Response deleteMateria(@FormParam("materia") @DefaultValue("{}") String s) {
    String out = "";
    System.out.println(s);
    ControllerMateria cs = new ControllerMateria();
    Gson gson = new Gson();
    try {
        // Deserializar el objeto Materia desde el JSON
        Materia materia = gson.fromJson(s, Materia.class);

        // Llamar al método de eliminación
        int idEliminado = cs.deleteMateria(materia);

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






    
    
    @Path("upMateria")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response upMateria(@FormParam("idMateria") int idMateria) {
        ControllerMateria cs = new ControllerMateria();
        String out = "";

        try {
            // Llamar al método de actualización lógica
            cs.upMateria(idMateria);

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
    
    
    
     @Path("getMateriaDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getMateriaDetails(@QueryParam("id") int id) {
        String out;
        Materia materia;

        try {
            ControllerMateria cs = new ControllerMateria();
            materia = cs.getMateriaDetails(id);

            if (materia != null) {
                out = new Gson().toJson(materia);
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
    
 
@Path("buscarMateria")
@POST
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public Response buscarMateria(@FormParam("query") String query) {
    String out;
    List<Materia> materias;

    try {
        ControllerMateria controller = new ControllerMateria();
        materias = controller.buscarMateria(query);
        
        out = new Gson().toJson(materias);
        return Response.status(Response.Status.OK).entity(out).build();
    } catch (SQLException sqlException) {
        sqlException.printStackTrace();
        out = "{\"error\":\"Error de SQL al buscar materias: " + sqlException.getMessage() + "\"}\n";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    } catch (Exception e) {
        e.printStackTrace();
        out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    }
}





}