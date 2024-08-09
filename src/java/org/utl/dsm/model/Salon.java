package org.utl.dsm.model;

public class Salon {
    private int idSalon;
    private String nombre;
    private String ubicacion;
    
     public Salon() {
    }

    public Salon(int idSalon, String nombre, String ubicacion) {
        this.idSalon = idSalon;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public int getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(int idSalon) {
        this.idSalon = idSalon;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    public Salon getSalon() {
        return this;
    }
}
