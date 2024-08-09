package org.utl.dsm.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasenia;
    private String token;
     

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombreUsuario, String contrasenia, String token) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.token = token;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usuario{");
        sb.append("idUsuario:").append(idUsuario);
        sb.append(", nombreUsuario:").append(nombreUsuario);
        sb.append(", contrasenia:").append(contrasenia);
        sb.append(", token:").append(token);
        sb.append('}');
        return sb.toString();
    }
    
    

    public void encode() {
        this.nombreUsuario = DigestUtils.md5Hex(this.nombreUsuario);
        this.contrasenia = DigestUtils.md5Hex(this.contrasenia);
    }

    public void setToken() {
        String nombreU = this.nombreUsuario;
        String p2 = "Classcraft";
        Date myDate = new Date();
        String fecha = new SimpleDateFormat("dd-MM-yyyy").format(myDate);
        String token = nombreU + "." + p2 + "." + fecha;
        this.token = DigestUtils.md5Hex(token);
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}