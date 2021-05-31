package com.example.kingsecurehub.modelo;

import com.example.kingsecurehub.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class Dispositivo implements Serializable {
    @SerializedName("codigo")
    private String codigo;

    @SerializedName("nombre")
    private String nombre;


    public Dispositivo(){}

    public Dispositivo(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstadoInt(){
        return 0;
    }


    public int getEstadoDrawable(){
        return R.drawable.disconected;
    }

}
