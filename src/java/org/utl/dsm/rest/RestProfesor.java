package org.utl.dsm.rest;

import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.controller.ControllerProfesor;
import com.google.gson.Gson;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.dsm.model.DiaDisponible;
import org.utl.dsm.model.DisponibilidadProfesor;
import org.utl.dsm.model.Materia;
import org.utl.dsm.model.Profesor;

@Path("profesor")
public class RestProfesor {
    @GET
    @Path("getAllProfesores")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProfesores() {
        String out;
        List<Profesor> profesores;

        try {
            ControllerProfesor controller = new ControllerProfesor();
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
    @Path("getDiasDisponibles/{idProfesor}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiasDisponibles(@PathParam("idProfesor") int idProfesor) {
        String out;
        ControllerProfesor controller = new ControllerProfesor();
        try {
            List<DisponibilidadProfesor> diasDisponibles = controller.getDiasDisponibles(idProfesor);
            out = new Gson().toJson(diasDisponibles);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener los días disponibles.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    @GET
    @Path("/getMaterias/{idProfesor}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMaterias(@PathParam("idProfesor") int idProfesor) {
        String out;
        List<Materia> materias;

        try {
            ControllerProfesor controller = new ControllerProfesor();
            materias = controller.getMaterias(idProfesor);
            out = new Gson().toJson(materias);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener las materias.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    
    @Path("getAllMaterias")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMaterias() {
        String out;
        try {
            ControllerProfesor cm = new ControllerProfesor();
            List<Materia> materias = cm.getAllMaterias();
            out = new Gson().toJson(materias);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error al obtener la lista de materias.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
@GET
@Path("getProfesoresByMateria/{idMateria}")
@Produces(MediaType.APPLICATION_JSON)
public Response getProfesoresByMateria(@PathParam("idMateria") int idMateria) {
    String out;
    ControllerProfesor controller = new ControllerProfesor();
    try {
        List<Profesor> profesores = controller.getProfesoresByMateria(idMateria);
        out = new Gson().toJson(profesores);
        return Response.status(Response.Status.OK).entity(out).build();
    } catch (SQLException sqlException) {
        sqlException.printStackTrace();
        out = "{\"error\":\"Error de SQL al obtener los profesores por materia.\"}\n";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    } catch (Exception e) {
        e.printStackTrace();
        out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}\n";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    }
}

@Path("getMateriaDetalles/{curso}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMateriaDetalles(@PathParam("curso") String curso) {
        String out;
        ControllerProfesor controller = new ControllerProfesor();
        try {
            Materia materia = controller.getMateriaByNombre(curso);
            if (materia != null) {
                out = new Gson().toJson(materia);
                return Response.status(Response.Status.OK).entity(out).build();
            } else {
                out = "{\"error\":\"Materia no encontrada\"}";
                return Response.status(Response.Status.NOT_FOUND).entity(out).build();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener los detalles de la materia.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }


@Path("getProfesorDetalles/{profesor}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfesorDetalles(@PathParam("profesor") String profesor) {
        String out;
        ControllerProfesor controller = new ControllerProfesor();
        try {
            Profesor prof = controller.getProfesorByNombre(profesor);
            if (prof != null) {
                out = new Gson().toJson(prof);
                return Response.status(Response.Status.OK).entity(out).build();
            } else {
                out = "{\"error\":\"Profesor no encontrado\"}";
                return Response.status(Response.Status.NOT_FOUND).entity(out).build();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener los detalles del profesor.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }
    
    


}
    
     
    

