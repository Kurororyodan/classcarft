/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.rest;

/**
 *
 * @author betillo
 */
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
import java.util.List;
import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PathParam;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;
import org.apache.poi.ss.util.CellRangeAddress;
import org.utl.dsm.controller.ControllerGrupo;
import org.utl.dsm.controller.ControllerGrupos;
import org.utl.dsm.model.Grupo;
import org.utl.dsm.model.Materia;

@Path("Grupo")

public class RestGrupo {
    
@Path("updateGrupo")
@POST
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public Response updateGrupo(@FormParam("idGrupo") int idGrupo,
                            @FormParam("nombreGrupo") String nombreGrupo,
                            @FormParam("capacidad") int capacidad) {
    String out = "";
    ControllerGrupo cs = new ControllerGrupo();
    try {
        // Convertir los parámetros recibidos en un objeto Grupo
        Grupo grupo = new Grupo();
        grupo.setId(idGrupo);
        grupo.setNombreGrupo(nombreGrupo);
        grupo.setCapacidad(capacidad);
        
        // Actualizar el grupo
        int affectedRows = cs.updateGrupo(grupo);
        if (affectedRows > 0) {
            out = "{\"result\":\"Grupo actualizado exitosamente\"}";
        } else {
            out = "{\"error\":\"No se encontró el grupo o no se realizaron cambios\"}";
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        out = "{\"error\":\"Error en la transacción\"}";
    }
    return Response.ok(out).build();
}

    
@Path("deleteGrupo")
@POST
@Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Asegurar el tipo de contenido correcto
@Produces(MediaType.APPLICATION_JSON)
public Response deleteGrupo(@FormParam("idGrupo") int idGrupo) {
    String out = "";
    ControllerGrupo cs = new ControllerGrupo();
    try {
        int idEliminado = cs.deleteGrupo(idGrupo);
        if (idEliminado > 0) {
            out = "{\"result\":\"Grupo eliminado exitosamente\"}";
        } else {
            out = "{\"result\":\"Error en la transacción\"}";
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        out = "{\"result\":\"Error en la transacción\"}";
    }
    return Response.ok(out).build();
}



    @Path("getGrupoDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getGrupoDetails(@QueryParam("id") int id) {
        String out;
        Grupo grupo;
        try {
            ControllerGrupo cs = new ControllerGrupo();
            grupo = cs.getGrupoDetails(id);
            if (grupo != null) {
                out = new Gson().toJson(grupo);
                return Response.status(Response.Status.OK).entity(out).build();
            } else {
                out = "{\"error\":\"Grupo no encontrado.\"}";
                return Response.status(Response.Status.NOT_FOUND).entity(out).build();
            }
        } catch (SQLException sQLException) {
            sQLException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener detalles del grupo.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    @Path("buscarGrupo")
@POST
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public Response buscarGrupo(@FormParam("query") String query) {
    String out;
    List<Grupo> grupos;

    try {
        ControllerGrupo controller = new ControllerGrupo();
        grupos = controller.buscarGrupo(query);
        
        out = new Gson().toJson(grupos);
        return Response.status(Response.Status.OK).entity(out).build();
    } catch (SQLException sqlException) {
        sqlException.printStackTrace();
        out = "{\"error\":\"Error de SQL al buscar grupos: " + sqlException.getMessage() + "\"}\n";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    } catch (Exception e) {
        e.printStackTrace();
        out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    }
}
    
    @GET
    @Path("getAllGrupos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGrupos() {
        String out;
        ControllerGrupos controller = new ControllerGrupos();
        try {
            List<Grupo> grupos = controller.getAll();
            out = new Gson().toJson(grupos);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener los grupos.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    @GET
    @Path("getMateriasByGrupo/{idGrupo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMateriasByGrupo(@PathParam("idGrupo") int idGrupo) {
        String out;
        ControllerGrupos controller = new ControllerGrupos();
        try {
            List<Materia> materias = controller.getMateriasByGrupo(idGrupo);
            out = new Gson().toJson(materias);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener las materias del grupo.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    @Path("getAllMaterias")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getAllMaterias() {
        String out;
        List<Materia> materias;
        try {
            ControllerGrupo cs = new ControllerGrupo();
            materias = cs.getAllMaterias();
            out = new Gson().toJson(materias);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener las materias.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    @GET
    @Path("getModalidadByGrupo/{idGrupo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModalidadByGrupo(@PathParam("idGrupo") int idGrupo) {
        String out;
        ControllerGrupos controller = new ControllerGrupos();
        try {
            String modalidad = controller.getModalidadByGrupo(idGrupo);
            if (modalidad != null) {
                out = "{\"modalidad\":\"" + modalidad + "\"}";
                return Response.status(Response.Status.OK).entity(out).build();
            } else {
                out = "{\"error\":\"Grupo no encontrado\"}";
                return Response.status(Response.Status.NOT_FOUND).entity(out).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out = "{\"error\":\"Error en el servidor\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getAll(@QueryParam("limit") int limit, @QueryParam("skip") int skip) {
        String out;
        List<Grupo> grupos;
        try {
            ControllerGrupo cs = new ControllerGrupo();
            grupos = cs.getALLS(limit, skip);
            out = new Gson().toJson(grupos);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener los grupos.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    
    
    @Path("insertGrupo")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertGrupo(@FormParam("nombreGrupo") String nombreGrupo,
                                @FormParam("capacidad") int capacidad,
                                @FormParam("modalidad") String modalidad,
                                @FormParam("idMaterias") String idMaterias) {
        String out = "";
        ControllerGrupo cs = new ControllerGrupo();

        // Imprimir los parámetros recibidos para depuración
        System.out.println("nombreGrupo: " + nombreGrupo);
        System.out.println("capacidad: " + capacidad);
        System.out.println("modalidad: " + modalidad);
        System.out.println("idMaterias: " + idMaterias);

        try {
            Grupo grupo = new Grupo();
            grupo.setNombreGrupo(nombreGrupo);
            grupo.setCapacidad(capacidad);
            grupo.setModalidad(modalidad);

            List<Materia> materias = new ArrayList<>();
            if (idMaterias != null && !idMaterias.isEmpty()) {
                for (String id : idMaterias.split(",")) {
                    Materia materia = new Materia();
                    materia.setIdMateria(Integer.parseInt(id));
                    materias.add(materia);
                }
            }
            grupo.setMaterias(materias);

            cs.insertGrupo(grupo);
            out = "{\"result\":\"Grupo insertado exitosamente\"}";
        } catch (SQLException ex) {
            ex.printStackTrace();
            out = "{\"error\":\"Error en la transacción\"}";
        }
        return Response.ok(out).build();
    }
}
