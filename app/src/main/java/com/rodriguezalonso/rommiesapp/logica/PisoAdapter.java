package com.rodriguezalonso.rommiesapp.logica;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rodriguezalonso.rommiesapp.R;

import java.util.ArrayList;

public class PisoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PisoListModel> listaPisos;
    private LayoutInflater inflater;

    public PisoAdapter(Activity context, ArrayList<PisoListModel> listaPisos){
        this.context = context;
        this.listaPisos = listaPisos;
        inflater = LayoutInflater.from(context);
    }

    static class ViewHolder{
        TextView direccion;
        TextView contacto;
        TextView habitaciones;
    }

    /**
     * Método para crear y/o actualizar el ViewHolder del piso
     * para luego listarlo en el ListView del AppActivity
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.piso, null);
            holder = new ViewHolder();
            holder.direccion = (TextView) convertView.findViewById(R.id.direccion);
            holder.contacto = (TextView) convertView.findViewById(R.id.contacto);
            holder.habitaciones = (TextView) convertView.findViewById(R.id.habitaciones);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        PisoListModel piso = listaPisos.get(position);
        holder.direccion.setText("Dirección: "+piso.getCiudad()+" " +piso.getDireccion());
        holder.contacto.setText("Contacto: "+piso.getContacto());
        holder.habitaciones.setText("Número de habitaciones: "+piso.getHabitaciones());
        return convertView;
    }

    @Override
    public int getCount(){
        return listaPisos.size();
    }

    @Override
    public Object getItem(int posicion){
        return listaPisos.get(posicion);
    }

    @Override
    public long getItemId(int posicion){
        return posicion;
    }
}