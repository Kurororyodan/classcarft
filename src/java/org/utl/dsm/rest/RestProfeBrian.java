package org.utl.dsm.rest;

import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.controller.Controllerprofebrian;
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
import org.utl.dsm.model.Materia;
import org.utl.dsm.model.Profesor;
import org.utl.dsm.model.Profesor2;

@Path("profesor2")
public class RestProfeBrian {
    @GET
    @Path("getAllProfesores")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProfesores() {
        String out;
        List<Profesor2> profesores;

        try {
            Controllerprofebrian controller = new Controllerprofebrian();
            profesores = controller.getAll();
            out = new Gson().toJson(profesores);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener profesores.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    @GET
    @Path("getMateriasByProfesor")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMateriasByProfesor(@QueryParam("idProfesor") int idProfesor) {
        String out;
        List<Materia> materias;

        try {
            Controllerprofebrian controller = new Controllerprofebrian();
            materias = controller.getMateriasByProfesor(idProfesor);
            out = new Gson().toJson(materias);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener materias.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    
      @POST
    @Path("activarProfesor")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activarProfesor(@FormParam("idProfesor") int idProfesor) {
        Controllerprofebrian controller = new Controllerprofebrian();
        String out;

        try {
            controller.activarProfesor(idProfesor);

            out = """
            {"result":"Profesor Activado"}
            """;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = """
            {"error":"Error de SQL al activar profesor"}
            """;
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = """
            {"error":"Ocurrió un error. Intente más tarde."}
            """;
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
        return Response.ok(out).build();
    }
    
    @POST
    @Path("desactivarProfesor")
    @Produces(MediaType.APPLICATION_JSON)
    public Response DesactivarProfesor(@FormParam("idProfesor") int idProfesor) {
        Controllerprofebrian controller = new Controllerprofebrian();
        String out;

        try {
            controller.DesactivarProfesor(idProfesor);

            out = """
            {"result":"Profesor Inactivo!!"}
            """;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = """
            {"error":"Error de SQL al activar profesor"}
            """;
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = """
            {"error":"Ocurrió un error. Intente más tarde."}
            """;
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
        return Response.ok(out).build();
    }
    
 @POST
@Path("buscarProfesor")
@Produces(MediaType.APPLICATION_JSON)
public Response buscarProfesor(
        @FormParam("nombre") String nombre,
        @FormParam("apellidoPaterno") String apellidoPaterno,
        @FormParam("apellidoMaterno") String apellidoMaterno,
        @FormParam("genero") String genero,
        @FormParam("fechaNacimiento") String fechaNacimiento,
        @FormParam("rfc") String rfc,
        @FormParam("curp") String curp,
        @FormParam("domicilio") String domicilio,
        @FormParam("codigoPostal") Integer codigoPostal,
        @FormParam("ciudad") String ciudad,
        @FormParam("estado") String estado,
        @FormParam("telefono") String telefono,
        @FormParam("email") String email,
        @FormParam("codigo") String codigo,
        @FormParam("disponibilidad_horaria") Integer disponibilidad_horaria, // Cambiado a Integer
        @FormParam("estatus") Integer estatus,
        @FormParam("fechaIngreso") String fechaIngreso
) {
    String out;
    List<Profesor2> profesores;

    try {
        Controllerprofebrian controller = new Controllerprofebrian();
        profesores = controller.buscarProfesor(nombre, apellidoPaterno, apellidoMaterno, genero, fechaNacimiento,
                rfc, curp, domicilio, codigoPostal, ciudad, estado, telefono, email,
                codigo, disponibilidad_horaria, estatus, fechaIngreso);

        out = new Gson().toJson(profesores);
        return Response.ok(out).build();
    } catch (SQLException sqlException) {
        sqlException.printStackTrace();
        out = "{\"error\":\"Error de SQL al buscar profesores. Detalles: " + sqlException.getMessage() + "\"}\n";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    } catch (Exception e) {
        e.printStackTrace();
        out = "{\"error\":\"Ocurrió un error. Intente más tarde. Detalles: " + e.getMessage() + "\"}\n";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    }
}
@Path("insertarProfesor")
@POST
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public Response insertarProfesor(String profesorJSON) {
    String out = "";
    Controllerprofebrian controller = new Controllerprofebrian();
    Gson gson = new Gson();

    // Imprimir el JSON recibido
    System.out.println("JSON recibido: " + profesorJSON);

    try {
        // Deserializar el objeto Profesor desde el JSON
        Profesor2 profesor = gson.fromJson(profesorJSON, Profesor2.class);

        // Verificar que el objeto persona no sea nulo
        if (profesor.getPersona() == null) {
            throw new IllegalArgumentException("El objeto Persona no puede ser nulo");
        }

        // Verificar que las materias no sean nulas
        if (profesor.getMaterias() == null) {
            throw new IllegalArgumentException("El objeto Materias no puede ser nulo");
        }

        // Verificar que los días disponibles no sean nulos
        if (profesor.getDisponibilidad_dias() == null) {
            throw new IllegalArgumentException("El objeto Disponibilidad_dias no puede ser nulo");
        }

        // Llamar al método de ControllerProfesor para insertar el profesor
        controller.insertarProfesor(profesor);

        out = "{\"result\":\"Profesor insertado correctamente\"}";
    } catch (Exception ex) {
        ex.printStackTrace();
        out = "{\"result\":\"Error en la transacción: " + ex.getMessage() + "\"}";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    }

    return Response.ok(out).build();
}

@Path("actualizarProfesor")
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response actualizarProfesor(String profesorJSON) {
    String out;
    Controllerprofebrian controller = new Controllerprofebrian();
    Gson gson = new Gson();

    try {
        Profesor2 profesor = gson.fromJson(profesorJSON, Profesor2.class);
        controller.actualizarProfesor(profesor);
        out = "{\"result\":\"Profesor actualizado correctamente\"}";
    } catch (Exception ex) {
        ex.printStackTrace();
        out = "{\"result\":\"Error en la transacción\"}";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    }

    return Response.ok(out).build();
}


        @Path("getProfesorById")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfesorById(@FormParam("idProfesor") int idProfesor) {
        String out;
        Controllerprofebrian controller = new Controllerprofebrian();

        try {
            Profesor2 profesor = controller.getProfesorById(idProfesor);
            if (profesor != null) {
                out = new Gson().toJson(profesor);
                return Response.ok(out).build();
            } else {
                out = "{\"error\":\"No se encontró ningún profesor con el ID especificado.\"}\n";
                return Response.status(Response.Status.NOT_FOUND).entity(out).build();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener el profesor por ID: " + sqlException.getMessage() + "\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }

}