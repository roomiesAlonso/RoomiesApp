package com.rodriguezalonso.rommiesapp.logica;

import java.io.Serializable;

public class PisoListModel implements Serializable {
    private String calle, portal, piso, contacto, habitaciones, ciudad, metros, alquiler;

    public PisoListModel(){

    }
    public PisoListModel(String ciudad, String calle, String portal, String piso, String metros, String alquiler, String contacto, String habitaciones) {
        this.calle = calle;
        this.portal = portal;
        this.piso = piso;
        this.metros = metros;
        this.alquiler = alquiler;
        this.contacto = contacto;
        this.ciudad= ciudad;
        this.habitaciones = habitaciones;
    }

    public String getCalle() {
        return calle;
    }
    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getPortal(){
        return portal;
    }
    public void setPortal(String portal){
        this.portal = portal;
    }

    public String getPiso(){
        return piso;
    }
    public void setPiso(String piso){
        this.piso = piso;
    }

    public String getMetros(){
        return metros;
    }
    public void setMetros(String metros){
        this.metros = metros;
    }

    public String getAlquiler(){
        return alquiler;
    }
    public void setAlquiler(String alquiler){
        this.alquiler = alquiler;
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
    public void setCiudad(String ciudad){
        this.ciudad=ciudad;
    }

    public String getDireccion(){
        return getCiudad() + ", " + getCalle() + " " +getPortal() + ", " + getPiso();
    }
    public String getDatos(){
        return getHabitaciones() + " habitaciones, " + getMetros() + " m², " + getAlquiler() + "€/mes";
    }

    /**
     * Método para crear un objeto PisoListModel
     * a partir del string recibido desde Firebase
     * @param txt con el String de los datos de Firebase
     * @return devuelve un PisoListModel nuevo
     */
    public static PisoListModel parser(String txt) {
        try {
            String[] datos = new String[8];
            datos = txt.split(";");
            String ciudad = datos[0];
            String calle = datos[1];
            String portal = datos[2];
            String piso = datos[3];
            String habitaciones = " " + datos[4];
            String m2 = datos[5];
            String alquiler = datos[6];
            String contacto =  datos[7];
            return new PisoListModel(ciudad, calle, portal, piso, contacto, habitaciones, m2, alquiler);
        }catch (Exception e){
            return null;
        }
    }
}