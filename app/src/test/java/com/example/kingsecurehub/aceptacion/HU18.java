package com.example.kingsecurehub.aceptacion;

import com.example.kingsecurehub.Casa;
import com.example.kingsecurehub.exceptions.DispositivoExistenteException;
import com.example.kingsecurehub.exceptions.DispositivoNoExistenteException;
import com.example.kingsecurehub.modelo.EstadoSMovimiento;
import com.example.kingsecurehub.modelo.Sensor;
import com.example.kingsecurehub.modelo.SensorMovimiento;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

//Como usuario quiero ser capaz de cambiar el estado de un dispositivo
public class HU18 {

    private Casa casa;

    @Before
    public void inicializarCasa() {
        casa = new Casa();
    }

    @Test
    //Cambiar el estado de un dispositivo existente
    public void cambiarEstadoDispValido() throws DispositivoExistenteException, DispositivoNoExistenteException {
        //Given: Un dispositivo existente en el sistema
        Sensor sens1 = new SensorMovimiento("sens1", "sensor cocina");
        casa.addSensor(sens1);
        //When: Se intenta cambiar su estado
        ((SensorMovimiento) sens1).setEstado(EstadoSMovimiento.MOTION_DETECTED);
        casa.updateSensor(sens1);
        //Then: Se cambia correctamente el estado del dispositivo
        List<Sensor> sensores = casa.getSensores();
        SensorMovimiento sensorMov = ((SensorMovimiento) sensores.get(0));
        assertEquals(EstadoSMovimiento.MOTION_DETECTED,sensorMov.getEstado());



    }
    @Test(expected = Exception.class)
    //Cambiar el estado de un dispositivo inexistente
    public void cambiarEstadoDispInvalido() throws DispositivoExistenteException, DispositivoNoExistenteException {
        //Given:Un dispositivo intexistente en el sistema
        Sensor sens1 = new SensorMovimiento("sens1", "sensor cocina");
        casa.addSensor(sens1);
        Sensor sens2 = new SensorMovimiento("sens2", "sensor inexistente");
        //When: se intenta cambiar su estado
        ((SensorMovimiento) sens2).setEstado(EstadoSMovimiento.MOTION_DETECTED);
        casa.updateSensor(sens2);
        //Then: se muestra un error indicando que no existe el dispositivo
    }
}