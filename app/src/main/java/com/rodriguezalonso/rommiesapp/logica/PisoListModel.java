package com.rodriguezalonso.rommiesapp.logica;

public class PisoListModel {
    private String direccion;
    private String contacto;
    private String habitaciones;
    private String ciudad;

    public PisoListModel(){

    }
    public PisoListModel(String ciudad,String direccion, String contacto, String habitaciones) {
        this.direccion = direccion;
        this.contacto = contacto;
        this.ciudad= ciudad;
        this.habitaciones = habitaciones;
    }

    public static PisoListModel parser(String txt) {
        String[] datos = new String[3];
        datos = txt.split(";");
        String ciudad=datos[0];
        String direccion = datos[1];
        String habitaciones = " " + datos[2];
        String contacto = " " + datos[3];
        return new PisoListModel(ciudad,direccion, contacto, habitaciones);
    }
    public static String[] filtrado(String txt){
        return txt.split(";");
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContacto() {
        return contacto;
    }
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getHabitaciones() {
        return habitaciones;
    }
    public void setHabitaciones(String habitaciones) {
        this.habitaciones = habitaciones;
    }

    public String getCiudad() {
        return ciudad;
    }
}