package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.utl.dsm.controller.ControllerAlumno;
import org.utl.dsm.model.Alumno;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;

@Path("alumno")
public class RestAlumno {
@Path("insertarAlumno")
@POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response insertarAlumno(String alumnoJSON) {
    String out = "";
    ControllerAlumno controller = new ControllerAlumno();
    Gson gson = new Gson();

    // Imprimir el JSON recibido
    System.out.println("JSON recibido: " + alumnoJSON);

    try {
        // Deserializar el objeto Alumno desde el JSON
        Alumno alumno = gson.fromJson(alumnoJSON, Alumno.class);

        // Verificar que el objeto persona no sea nulo
        if (alumno.getPersona() == null) {
            throw new IllegalArgumentException("El objeto Persona no puede ser nulo");
        }

        // Llamar al método de ControllerAlumno para insertar el alumno
        controller.insertarAlumno(alumno);

        out = "{\"result\":\"Alumno insertado correctamente\"}";
    } catch (Exception ex) {
        ex.printStackTrace();
        out = "{\"result\":\"Error en la transacción\"}";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    }

    return Response.ok(out).build();
}



@GET
    @Path("getAllAlumnos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAlumnos() {
        String out;
        List<Alumno> alumnos;

        try {
            ControllerAlumno controller = new ControllerAlumno();
            alumnos = controller.getAll();
            out = new Gson().toJson(alumnos);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener alumnos.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }






    @Path("activarAlumno")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response activarAlumno(@FormParam("idAlumno") int idAlumno) {
        ControllerAlumno controller = new ControllerAlumno();
        String out;

        try {
            controller.ActivarAlumno(idAlumno);

            out = """
            {"result":"Alumno Activado"}
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

    @Path("desactivarAlumno")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response desactivarAlumno(@FormParam("idAlumno") int idAlumno) {
        ControllerAlumno controller = new ControllerAlumno();
        String out;

        try {
            controller.desactivarAlumno(idAlumno);

            out = """
            {"result":"Alumno desactivado"}
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

    @Path("buscarAlumno")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarAlumno(
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
            @FormParam("matricula") String matricula,
            @FormParam("estatus") Integer estatus,
            @FormParam("modalidad") String modalidad,
            @FormParam("fechaIngreso") String fechaIngreso
    ) {
        String out;
        List<Alumno> alumnos;

        try {
            ControllerAlumno controller = new ControllerAlumno();
            alumnos = controller.buscarAlumno(nombre, apellidoPaterno, apellidoMaterno, genero, fechaNacimiento,
                    rfc, curp, domicilio, codigoPostal, ciudad, estado, telefono, email,
                    matricula, estatus, modalidad, fechaIngreso);
            
            out = new Gson().toJson(alumnos);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al buscar alumnos: " + sqlException.getMessage() + "\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    

    
@Path("actualizarAlumno")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarAlumno(String alumnoJSON) {
        String out;
        ControllerAlumno controller = new ControllerAlumno();
        Gson gson = new Gson();

        try {
            Alumno alumno = gson.fromJson(alumnoJSON, Alumno.class);
            controller.actualizarAlumno(alumno);
            out = "{\"result\":\"Alumno actualizado correctamente\"}";
        } catch (Exception ex) {
            ex.printStackTrace();
            out = "{\"result\":\"Error en la transacción\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }

        return Response.ok(out).build();
    }




@POST
    @Path("verificarMatricula")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verificarMatricula(@FormParam("matricula") String matricula) {
        ControllerAlumno controller = new ControllerAlumno();
        boolean existe = false;

        try {
            existe = controller.existeMatricula(matricula);
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Error de SQL\"}").build();
        }

        if (existe) {
            return Response.status(Response.Status.OK).entity("{\"existe\":true}").build();
        } else {
            return Response.status(Response.Status.OK).entity("{\"existe\":false}").build();
        }
    }





@Path("getAlumnoById")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAlumnoById(@FormParam("idAlumno") int idAlumno) {
        String out;
        ControllerAlumno controller = new ControllerAlumno();

        try {
            Alumno alumno = controller.getAlumnoById(idAlumno);
            if (alumno != null) {
                out = new Gson().toJson(alumno);
                return Response.ok(out).build();
            } else {
                out = "{\"error\":\"No se encontró ningún alumno con el ID especificado.\"}\n";
                return Response.status(Response.Status.NOT_FOUND).entity(out).build();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener el alumno por ID: " + sqlException.getMessage() + "\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
}

