package com.rodriguezalonso.rommiesapp.logica;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String nombreApellidos, edad, ciudad, centroEstudio, correo;

    public Usuario(){

    }
    public Usuario(String nombreApellidos, String edad, String ciudad, String centroEstudio, String correo) {
        this.nombreApellidos = nombreApellidos;
        this.edad = edad;
        this.ciudad = ciudad;
        this.centroEstudio = centroEstudio;
        this.correo = correo;
    }

    public String getNombreApellidos() {
        return nombreApellidos;
    }
    public void setNombreApellidos(String nombreApellidos) {
        this.nombreApellidos = nombreApellidos;
    }

    public String getEdad() {
        return edad;
    }
    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCentroEstudio() {
        return centroEstudio;
    }
    public void setCentroEstudio(String centroEstudio) {
        this.centroEstudio = centroEstudio;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public static Usuario parser(String txt){
        try {
            String[] datos = new String[5];
            datos = txt.split(";");
            String nombreApellidos = datos[0];
            String edad = datos[1];
            String ciudad = datos[2];
            String centroEstudio = datos[3];
            String correo = datos[4];
            return new Usuario(nombreApellidos, edad, ciudad, centroEstudio, correo);
        }catch (Exception e) {
            return null;
        }
    }
}
