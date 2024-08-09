/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.rest;

import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.dsm.controller.ControllerDia_Disponible;
import org.utl.dsm.model.DiaDisponible;

/**
 *
 * @author bryan
 */
@Path("dia_disponible")
public class RestDia_Disponible {

    @GET
    @Path("getAllDiasDisponibles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDiasDisponibles() {
        String out;
        List<DiaDisponible> diasDisponibles;

        try {
            ControllerDia_Disponible controller = new ControllerDia_Disponible();
            diasDisponibles = controller.getAll();
            out = new Gson().toJson(diasDisponibles);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener días disponibles.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
}