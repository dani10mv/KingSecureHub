package com.example.kingsecurehub.aceptacion;

import com.example.kingsecurehub.Casa;
import com.example.kingsecurehub.exceptions.DispositivoExistenteException;
import com.example.kingsecurehub.modelo.Sensor;
import com.example.kingsecurehub.modelo.SensorMovimiento;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


//Como usuario quiero poder listar los dispositivos existentess
public class HU17 {
    private Casa casa;

    @Before
    public void inicializarCasa() {
        casa = new Casa();
    }

    @Test
    //Listar un conjunto con dispositivos existentes
    public void listarConDisp() throws DispositivoExistenteException {
        //Given: Un conjunto no vacio de dispositivos
        Sensor sens1 = new SensorMovimiento("sens1", "sensor cocina");
        Sensor sens2 = new SensorMovimiento("sens2", "sensor salon");
        casa.addSensor(sens1);
        casa.addSensor(sens2);
        //When: se intenta listar
        List<Sensor> sensores = casa.getSensores();
        //Then: se muestra un listado con todos los dispositivos existentes
        assertEquals(2,sensores.size());
        assertTrue(sensores.contains(sens1));
        assertTrue(sensores.contains(sens2));
    }
    @Test
    //Listar un conjunto vacio de dispositivos
    public void listarSinDisp(){
        //Given: un conjunto vacio de dispositivos
        //When: se intenta listar
        List<Sensor> sensores = casa.getSensores();
        //Then: se muestra un listado vacio de dispositivos
        assertEquals(0,sensores.size());

    }
}