package com.example.kingsecurehub.modelo;

import com.example.kingsecurehub.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SensorMovimiento extends Sensor {

    private EstadoSMovimiento estado;

    public SensorMovimiento(){super("movimiento");}

    public SensorMovimiento(String codigo, String nombre) {
        super(codigo, nombre, "movimiento");
    }

    public EstadoSMovimiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoSMovimiento estado) {
        this.estado = estado;
    }

    @Override
    public int getEstadoInt() {
        if(getEstado()==EstadoSMovimiento.DISCONNECTED)
            return 0;
        if(getEstado()==EstadoSMovimiento.NO_MOTION)
            return 1;

        return 2;

    }


    public static SensorMovimiento fromJson(JSONObject jsonObj){

        SensorMovimiento sensor = new SensorMovimiento();

        try {
            sensor.setCodigo(jsonObj.getString("codigo"));
            sensor.setNombre(jsonObj.getString("nombre"));

            int estado= jsonObj.getInt("estado");

            if(estado==0)
                sensor.setEstado(EstadoSMovimiento.DISCONNECTED);
            else if (estado==1)
                sensor.setEstado(EstadoSMovimiento.NO_MOTION);
            else
                sensor.setEstado(EstadoSMovimiento.MOTION_DETECTED);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return sensor;
    }
    @Override
    public String toString() {
        return "SensorMovimiento{" +"\n"+
                "estado=" + estado +"\n"+
                "nombre="+getNombre()+"\n"+
                "codigo="+getCodigo()+"\n"+
                '}';
    }


    @Override
    public int getEstadoDrawable() {
        if(getEstado()==EstadoSMovimiento.NO_MOTION){
            return R.drawable.no_motion;
        }else if (getEstado()==EstadoSMovimiento.MOTION_DETECTED){
            return R.drawable.motion_detected;
        }
        return R.drawable.disconected;
    }
}
