package com.example.kingsecurehub;

import android.se.omapi.SEService;

import com.example.kingsecurehub.modelo.Actuador;
import com.example.kingsecurehub.modelo.Sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Casa implements Serializable {


    private List<Sensor> sensores;
    private List<Actuador> actuadores;


    public Casa(){
        sensores=new ArrayList<>();
        actuadores=new ArrayList<>();
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public List<Actuador> getActuadores() {
        return actuadores;
    }

    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    public void setActuadores(List<Actuador> actuadores) {
        this.actuadores = actuadores;
    }

    public void addSensor(Sensor sensor) {
        sensores.add(sensor);
    }

    public void addActuador(Actuador actuador) {
        actuadores.add(actuador);
    }

    public void deleteSensor(int pos){
        sensores.remove(pos);
    }


    public void deleteActuador(int pos){
        actuadores.remove(pos);
    }

    public void deleteSensor(Sensor sensor){
        sensores.remove(sensor);
    }


    public void deleteActuador(Actuador actuador){
        actuadores.remove(actuador);
    }

    public void updateActuador(Actuador actuador){

        for (int i = 0 ; i<actuadores.size();i++){
            if(actuador.getCodigo().equals(actuadores.get(i).getCodigo())){
                actuadores.set(i,actuador);
            }
        }

    }

    public void updateSensor(Sensor sensor){

        for (int i = 0 ; i<sensores.size();i++){
            if(sensor.getCodigo().equals(sensores.get(i).getCodigo())){
                sensores.set(i,sensor);
            }
        }
    }


}
