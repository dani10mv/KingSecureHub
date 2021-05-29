package com.example.kingsecurehub.modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public abstract class Sensor extends Dispositivo {

    private final String tipoSensor;

    public Sensor( String tipoSensor){
        this.tipoSensor=tipoSensor;
    }
    public Sensor(String codigo, String nombre, String tipoSensor) {
        super(codigo,nombre);
        this.tipoSensor=tipoSensor;
    }

    public String getTipoSensor(){
        return this.tipoSensor;
    }

    public byte[] toJsonByte() throws JSONException, UnsupportedEncodingException {
        JSONObject obj = new JSONObject();
        obj.put("codigo",getCodigo());
        obj.put("nombre",getNombre());
        obj.put("estado",getEstadoInt());
        obj.put("tipoSensor",getTipoSensor());

        final String requestBody = obj.toString();
        return  requestBody.getBytes("utf-8");

    }

    public List<String> getEstados(){
        List<String> estados  = new ArrayList<>();

        if(tipoSensor.toUpperCase().equals("MOVIMIENTO")){

            for(EstadoSMovimiento e : EstadoSMovimiento.values()){

                estados.add(e.name());
            }
        }
       else if (tipoSensor.toUpperCase().equals("APERTURA")){

            for(EstadoSApertura e : EstadoSApertura.values())
                estados.add(e.name());

        }

        return estados;
    }


}
