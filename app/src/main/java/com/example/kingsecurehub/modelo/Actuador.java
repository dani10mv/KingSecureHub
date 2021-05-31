package com.example.kingsecurehub.modelo;

import androidx.annotation.NonNull;

import com.example.kingsecurehub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Actuador extends Dispositivo {

    private EstadoActuador estado;
    public Actuador(){};
    public Actuador(String codigo, String nombre) {
        super(codigo, nombre);
    }

    public EstadoActuador getEstado() {
        return estado;
    }

    public void setEstado(EstadoActuador estado) {
        this.estado = estado;
    }


    public static Actuador fromJson(JSONObject jsonObj){

        Actuador actuador = new Actuador();

        try {
            actuador.setCodigo(jsonObj.getString("codigo"));
            actuador.setNombre(jsonObj.getString("nombre"));

            int estado= jsonObj.getInt("estado");

            if(estado==0)
                actuador.setEstado(EstadoActuador.DISCONNECTED);
            else if (estado==1)
                actuador.setEstado(EstadoActuador.OFF);
            else
                actuador.setEstado(EstadoActuador.ON);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return actuador;
    }

    @Override
    public int getEstadoInt() {
        if(getEstado()==EstadoActuador.DISCONNECTED)
            return 0;
        if(getEstado()==EstadoActuador.OFF)
            return 1;
        return 2;

    }

    public byte[] toJsonByte() throws JSONException, UnsupportedEncodingException {
        JSONObject obj = new JSONObject();
        obj.put("codigo",getCodigo());
        obj.put("nombre",getNombre());
        obj.put("estado",getEstadoInt());

        final String requestBody = obj.toString();
        return  requestBody.getBytes("utf-8");

    }



    @NonNull
    @Override
    public String toString() {
        return "Acturador{" +"\n"+
                "estado=" + estado +"\n"+
                "nombre="+getNombre()+"\n"+
                "codigo="+getCodigo()+"\n"+
                '}';
    }

    @Override
    public int getEstadoDrawable() {
        if(getEstado()==EstadoActuador.OFF){
            return R.drawable.off;
        }else if(getEstado()==EstadoActuador.ON){
            return R.drawable.on;
        }
        return R.drawable.disconected;
    }
}
