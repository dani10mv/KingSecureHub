package com.example.kingsecurehub;

import com.example.kingsecurehub.modelo.Actuador;
import com.example.kingsecurehub.modelo.EstadoActuador;
import com.example.kingsecurehub.modelo.EstadoSApertura;
import com.example.kingsecurehub.modelo.EstadoSMovimiento;
import com.example.kingsecurehub.modelo.SensorApertura;
import com.example.kingsecurehub.modelo.SensorMovimiento;

public class DeviceFactory {


    public SensorMovimiento getSensorMovimiento(String codigo, String nombre, EstadoSMovimiento estado){
        SensorMovimiento s =new SensorMovimiento(codigo,nombre);
        s.setEstado(estado);

        return s;

    }


    public SensorApertura getSensorApertura(String codigo, String nombre, EstadoSApertura estado){
        SensorApertura s =new SensorApertura(codigo,nombre);
        s.setEstado(estado);

        return s;
    }


    public Actuador getActuador(String codigo, String nombre, EstadoActuador estado){

        Actuador a = new Actuador(codigo,nombre);
        a.setEstado(estado);
        return a;
    }

}
