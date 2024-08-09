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
import java.util.List;
import org.utl.dsm.controller.ControllerHorarios;
import org.utl.dsm.model.Horario;


import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
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
import java.util.Set;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author betillo
 */
@Path("Horario")

public class RestHorario {
    @Path("insertHorario")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertHorario(
        @FormParam("identificador") String identificador,
        @FormParam("diaSemana") String diaSemana,
        @FormParam("horaInicio") String horaInicio,
        @FormParam("horaFin") String horaFin,
        @FormParam("curso") String curso,
        @FormParam("profesor") String profesor,
        @FormParam("profesorId") String profesorId,
        @FormParam("aula") String aula
    ) {
        String out = "";
        ControllerHorarios cs = new ControllerHorarios();
        
        try {
            Horario horario = new Horario();
            horario.setIdentificador(identificador);
            horario.setDiaSemana(diaSemana);
            horario.setHoraInicio(horaInicio);
            horario.setHoraFin(horaFin);
            horario.setCurso(curso);
            horario.setProfesor(profesor);
            horario.setAula(aula);
            
            cs.insertHorario(horario);
            out = "{\"result\":\"Objeto insertado\"}";
        } catch (Exception ex) {
            ex.printStackTrace();
            out = "{\"result\":\"Error en la transaccion\"}";
        }
        return Response.ok(out).build();
    }
    @Path("getByProfesor/{profesor}")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response getByProfesor(@PathParam("profesor") String profesor) {
    String out;
    ControllerHorarios cc = new ControllerHorarios();
    try {
        List<Horario> horarios = cc.getByProfesor(profesor);
        out = new Gson().toJson(horarios);
        return Response.status(Response.Status.OK).entity(out).build();
    } catch (SQLException sqlException) {
        sqlException.printStackTrace();
        out = "{\"error\":\"Error de SQL al obtener los horarios.\"}";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    } catch (Exception e) {
        e.printStackTrace();
        out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}";
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
    }
}

    
    @Path("getByIdenti/{identificador}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByIdenti(@PathParam("identificador") String identificador) {
        String out;
        ControllerHorarios cc = new ControllerHorarios();
        try {
            List<Horario> horarios = cc.getIdentificador(identificador);
            out = new Gson().toJson(horarios);
            return Response.status(Response.Status.OK).entity(out).build();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            out = "{\"error\":\"Error de SQL al obtener los horarios.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Ocurrió un error. Intente más tarde.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(out).build();
        }
    }

    
    @Path("getByIdentificador/{identificador}")
@GET
@Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
public Response getByIdentificador(@PathParam("identificador") String identificador) {
    ControllerHorarios cc = new ControllerHorarios();
    try {
        List<Horario> horarios = cc.getIdentificador(identificador);
        byte[] excelBytes = generateExcel(horarios);
        return Response.ok(excelBytes)
                .header("Content-Disposition", "attachment; filename=horarios_" + identificador + ".xlsx")
                .build();
    } catch (SQLException e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\":\"Error en el servidor\"}").build();
    }
}


@Path("getByProfesorIdentificador/{profesor}/{identificador}")
@GET
@Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
public Response getByProfesorIdentificador(@PathParam("profesor") String profesor, @PathParam("identificador") String identificador) {
    ControllerHorarios cc = new ControllerHorarios();
    try {
        List<Horario> horarios = cc.getHorariosByProfesorIdentificador(profesor, identificador);
        byte[] excelBytes = generateExcel(horarios);
        return Response.ok(excelBytes)
                .header("Content-Disposition", "attachment; filename=horario_" + profesor +"_"+  identificador + ".xlsx")
                .build();
    } catch (SQLException e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\":\"Error de SQL al obtener los horarios.\"}").build();
    } catch (Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\":\"Ocurrió un error. Intente más tarde.\"}").build();
    }
}

private byte[] generateExcel(List<Horario> horarios) {
    try (Workbook workbook = new XSSFWorkbook();
         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

        Sheet sheet = workbook.createSheet("Horarios");

        CellStyle centeredStyle = workbook.createCellStyle();
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);
        centeredStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        centeredStyle.setWrapText(true);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"HORA", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(centeredStyle);
        }

        String[] horas = {
            "07:00-07:50", "08:00-08:50", "09:00-09:50", "10:00-10:50", "11:00-11:50",
            "12:00-12:50", "13:00-13:50", "14:00-14:50", "15:00-15:50", "16:00-16:50",
            "17:00-17:50", "18:00-18:50", "19:00-19:50", "20:00-20:50", "21:00-21:50"
        };

        Map<String, Integer> horaRowMap = new HashMap<>();
        for (int i = 0; i < horas.length; i++) {
            Row row = sheet.createRow(i + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(horas[i]);
            cell.setCellStyle(centeredStyle);
            horaRowMap.put(horas[i], i + 1);
        }

        Map<String, Integer> dayColumnMap = new HashMap<>();
        dayColumnMap.put("Lunes", 1);
        dayColumnMap.put("Martes", 2);
        dayColumnMap.put("Miercoles", 3);
        dayColumnMap.put("Jueves", 4);
        dayColumnMap.put("Viernes", 5);

        for (Horario horario : horarios) {
            if (horario == null) continue; // Verificar si el objeto horario es null

            int startRow = -1, endRow = -1;
            for (int i = 0; i < horas.length; i++) {
                if (horario.getHoraInicio() != null && horario.getHoraInicio().equals(horas[i].split("-")[0] + ":00")) {
                    startRow = i + 1;
                }
                if (horario.getHoraFin() != null && horario.getHoraFin().equals(horas[i].split("-")[1] + ":00")) {
                    endRow = i + 1;
                }
            }

            if (startRow != -1 && endRow != -1) {
                Integer colNum = dayColumnMap.get(horario.getDiaSemana());
                if (colNum == null) continue; // Verificar si el día de la semana tiene una columna asignada

                Row startRowObj = sheet.getRow(startRow);
                if (startRowObj == null) {
                    startRowObj = sheet.createRow(startRow);
                }

                String horarioData = String.format("Curso: %s\nProf: %s\nAula: %s",
                        horario.getCurso(), horario.getProfesor(), horario.getAula());

                Cell cell = startRowObj.createCell(colNum);
                cell.setCellValue(horarioData);
                cell.setCellStyle(centeredStyle);

                if (endRow > startRow) {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, colNum, colNum));
                }

                sheet.autoSizeColumn(colNum);
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(baos);

        return baos.toByteArray();

    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

    
    @Path("getAllIdentificadorHorario")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllIdentificadorHorario() {
        ControllerHorarios controller = new ControllerHorarios();
        try {
            Set<String> identificadores = controller.getAllIdentificadores();
            String json = new Gson().toJson(identificadores);
            return Response.ok(json).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener los identificadores\"}").build();
        }
    }
    
    @Path("updateHorario")
@PUT
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public Response updateHorario(
    @FormParam("identificador") String identificador,
    @FormParam("diaSemana") String diaSemana,
    @FormParam("horaInicio") String horaInicio,
    @FormParam("horaFin") String horaFin,
    @FormParam("curso") String curso,
    @FormParam("profesor") String profesor,
    @FormParam("aula") String aula,
    @FormParam("nuevoCurso") String nuevoCurso,
    @FormParam("nuevoProfesor") String nuevoProfesor,
    @FormParam("nuevoAula") String nuevoAula) {

    Horario h = new Horario();
    h.setIdentificador(identificador);
    h.setDiaSemana(diaSemana);
    h.setHoraInicio(horaInicio);
    h.setHoraFin(horaFin);
    h.setCurso(curso);
    h.setProfesor(profesor);
    h.setAula(aula);
    h.setNuevoCurso(nuevoCurso);
    h.setNuevoProfesor(nuevoProfesor);
    h.setNuevoAula(nuevoAula);

    try {
        ControllerHorarios cc = new ControllerHorarios();
        Horario updatedHorario = cc.updateHorario(h);
        return Response.status(Response.Status.OK).entity(new Gson().toJson(updatedHorario)).build();
    } catch (Exception e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\":\"Error en el servidor\"}").build();
    }
}

}