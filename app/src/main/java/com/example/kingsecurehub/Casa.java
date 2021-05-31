package com.example.kingsecurehub;

import android.se.omapi.SEService;

import com.example.kingsecurehub.exceptions.DispositivoExistenteException;
import com.example.kingsecurehub.exceptions.DispositivoNoExistenteException;
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

    public void addSensor(Sensor sensor) throws DispositivoExistenteException {
        for(Sensor sens : this.sensores){
            if(sens.getCodigo().equals(sensor.getCodigo())) throw new DispositivoExistenteException();
        }
        sensores.add(sensor);
    }

    public void addActuador(Actuador actuador) throws DispositivoExistenteException{
        for(Actuador act : this.actuadores){
            if(act.getCodigo().equals(actuador.getCodigo())) throw new DispositivoExistenteException();
        }

        actuadores.add(actuador);
    }

    public void deleteSensor(int pos) throws DispositivoNoExistenteException {
        if(pos>=this.sensores.size()) throw new DispositivoNoExistenteException();
        sensores.remove(pos);
    }


    public void deleteActuador(int pos) throws DispositivoNoExistenteException {
        if(pos>=this.actuadores.size()) throw new DispositivoNoExistenteException();
        actuadores.remove(pos);
    }

    public void deleteSensor(Sensor sensor) throws DispositivoNoExistenteException {
        if (!this.sensores.contains(sensor)) throw  new DispositivoNoExistenteException();
        sensores.remove(sensor);
    }


    public void deleteActuador(Actuador actuador) throws DispositivoNoExistenteException {
        if (!this.actuadores.contains(actuador)) throw  new DispositivoNoExistenteException();
        actuadores.remove(actuador);
    }

    public void updateActuador(Actuador actuador) throws DispositivoNoExistenteException {

        for (int i = 0 ; i<actuadores.size();i++){
            if(actuador.getCodigo().equals(actuadores.get(i).getCodigo())){
                actuadores.set(i,actuador);
                return;
            }
        }
        throw  new DispositivoNoExistenteException();

    }

    public void updateSensor(Sensor sensor) throws DispositivoNoExistenteException {

        for (int i = 0 ; i<sensores.size();i++){
            if(sensor.getCodigo().equals(sensores.get(i).getCodigo())){
                sensores.set(i,sensor);
                return;
            }
        }
        throw  new DispositivoNoExistenteException();
    }


}
