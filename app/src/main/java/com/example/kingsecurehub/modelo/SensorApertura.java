package com.example.kingsecurehub.modelo;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SensorApertura extends Sensor{



    private EstadoSApertura estado;

    public SensorApertura(){
        super("Apertura");
    };
    public SensorApertura(String codigo, String nombre) {
        super(codigo, nombre, "Apertura");

    }

    public EstadoSApertura getEstado() {

        return estado;
    }

    public void setEstado(EstadoSApertura estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "SensorApertura{" +"\n"+
                "estado=" + estado +"\n"+
                "nombre="+getNombre()+"\n"+
                "codigo="+getCodigo()+"\n"+
                '}';
    }

    @Override
    public int getEstadoInt() {
        if(getEstado()==EstadoSApertura.DISCONNECTED)
            return 0;
        if(getEstado()==EstadoSApertura.CLOSE)
            return 1;

            return 2;

    }


    public static SensorApertura fromJson(JSONObject jsonObj){

        SensorApertura sensor = new SensorApertura();

        try {
            sensor.setCodigo(jsonObj.getString("codigo"));
            sensor.setNombre(jsonObj.getString("nombre"));

            int estado= jsonObj.getInt("estado");

            if(estado==0)
                sensor.setEstado(EstadoSApertura.DISCONNECTED);
            else if (estado==1)
                sensor.setEstado(EstadoSApertura.CLOSE);
            else
                sensor.setEstado(EstadoSApertura.OPEN);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return sensor;
    }
}
