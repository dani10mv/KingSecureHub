package com.example.kingsecurehub;

import com.example.kingsecurehub.modelo.Actuador;
import com.example.kingsecurehub.modelo.Sensor;
import com.example.kingsecurehub.modelo.SensorApertura;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DeviceService {

    @GET("/sensores/apertura")
    public Call<List<SensorApertura>> getSensoresApertura();


    @GET("sensores/movimiento")
    public Call<List<SensorApertura>> getSensoresMovimiento();


    @GET("actuadores")
    public Call<List<Actuador>> getActuadores();


}
