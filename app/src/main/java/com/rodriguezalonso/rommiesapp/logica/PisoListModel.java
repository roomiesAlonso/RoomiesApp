package com.rodriguezalonso.rommiesapp.logica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PisoListModel implements Serializable {
    private String calle, portal, piso, contacto, ciudad;
    private int habitaciones, metros, alquiler;

    public PisoListModel(){

    }
    public PisoListModel(String ciudad, String calle, String portal, String piso,
                int metros, int alquiler, int habitaciones, String contacto) {
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

    public String getContacto() {
        return contacto;
    }
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad){
        this.ciudad=ciudad;
    }

    public int getHabitaciones(){
        return habitaciones;
    }
    public void setHabitaciones(int habitaciones){
        this.habitaciones=habitaciones;
    }

    public int getMetros(){
        return metros;
    }
    public void setMetros(int metros){
        this.metros=metros;
    }

    public int getAlquiler(){
        return alquiler;
    }
    public void setAlquiler(int alquiler){
        this.alquiler=alquiler;
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
            int habitaciones = Integer.parseInt(datos[4]);
            int metros = Integer.parseInt(datos[5]);
            int alquiler = Integer.parseInt(datos[6]);
            String contacto =  datos[7];
            return new PisoListModel(ciudad, calle, portal, piso, metros, alquiler, habitaciones, contacto);
        }catch (Exception e){
            return null;
        }
    }
}