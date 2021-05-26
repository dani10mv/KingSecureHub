package com.example.kingsecurehub.modelo;

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





}
