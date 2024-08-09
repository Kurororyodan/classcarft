package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.controller.ControllerModalidad;
import org.utl.dsm.model.Modalidad;

@Path("modalidad")
public class RestModalidad {

    @Path("getAllModalidades")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllModalidades() {
        String out = "";
        ControllerModalidad controller = new ControllerModalidad();
        List<Modalidad> modalidades;
        try {
            modalidades = controller.getAllModalidades();
            out = new Gson().toJson(modalidades);
        } catch (SQLException ex) {
            ex.printStackTrace();
            out = """
                {"result":"Error en la transacción"}
                """;
        }
        return Response.ok(out).build();
    }
}