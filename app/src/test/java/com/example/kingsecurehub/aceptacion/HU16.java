package com.example.kingsecurehub.aceptacion;

import com.example.kingsecurehub.Casa;
import com.example.kingsecurehub.exceptions.DispositivoExistenteException;
import com.example.kingsecurehub.exceptions.DispositivoNoExistenteException;
import com.example.kingsecurehub.modelo.Sensor;
import com.example.kingsecurehub.modelo.SensorMovimiento;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//Como usuario quiero se capax de borrar un dispositivo
public class HU16 {
    private Casa casa;

    @Before
    public void inicializarCasa() {
        casa = new Casa();
    }

    @Test
    //Borrar un dispositivo en un conjunto no vacio de dispositivos
    public void borrarDispValido() throws DispositivoExistenteException, DispositivoNoExistenteException {
        //Given: Un conjunto que contiene dos dispositivos
        Sensor sens1 = new SensorMovimiento("sens1", "sensor cocina");
        Sensor sens2 = new SensorMovimiento("sens2", "sensor salor");
        casa.addSensor(sens1);
        casa.addSensor(sens2);
        //When: Se quiere borrar un dispositivo existente
        casa.deleteSensor(sens2);
        //Then: Se borra el dispositivo correctamente
        boolean esta = false;
        for(Sensor s : casa.getSensores()){
            if (s.getCodigo().equals(sens2.getCodigo())){
                esta = true;
                break;
            }
        }
        assertFalse(esta);
    }
    @Test(expected = DispositivoNoExistenteException.class)
    //Borrar un dispositivo inexsistente
    public void borrarDispInvalido() throws DispositivoExistenteException, DispositivoNoExistenteException {
        //Given: Un conjunto que contiene dos dispositivos
        Sensor sens1 = new SensorMovimiento("sens1", "sensor cocina");
        Sensor sens2 = new SensorMovimiento("sens2", "sensor salor");
        casa.addSensor(sens1);
        casa.addSensor(sens2);
        //When: Se intenta borrar un dispositivo inexistente
        Sensor sens3 = new SensorMovimiento("sens3", "sensor habitacion");
        casa.deleteSensor(sens3);
        //Then: Se muestra un mensaje indicando que ese dispositivo on existe
    }
}