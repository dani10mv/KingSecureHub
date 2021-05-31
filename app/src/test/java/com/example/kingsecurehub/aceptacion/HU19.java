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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

//Como usuario quiero ser capaz de ver el estado de un dispositivo
public class HU19 {

    private Casa casa;

    @Before
    public void inicializarCasa() {
        casa = new Casa();
    }

    @Test
    //Obtener el estado de un dispositivo existente
    public void verEstadoDispValido() throws DispositivoExistenteException, DispositivoNoExistenteException {
        //Given:un dispositivo existente en el sistema
        Sensor sens1 = new SensorMovimiento("sens1", "sensor cocina");
        casa.addSensor(sens1);
        //When: se intenta obtener su estado
        ((SensorMovimiento) sens1).setEstado(EstadoSMovimiento.MOTION_DETECTED);
        casa.updateSensor(sens1);
        //Then: se retorna el estado del dispositivo
        List<Sensor> sensores = casa.getSensores();
        SensorMovimiento sensorMov = ((SensorMovimiento) sensores.get(0));
        assertEquals(EstadoSMovimiento.MOTION_DETECTED,sensorMov.getEstado());
    }
    @Test
    //Obtener el estado de un dispositivo inexistente
    public void verEstadoDispInvalido(){
        //Given: un dispositivo inexistente en el sistema
        Sensor sens1 = new SensorMovimiento("sens1", "sensor inexistente");
        //When: se intenta obtener su estado
        List<Sensor> sensores = casa. getSensores();
        //Then: se muestra un mensaje indicando que no existe dicho dispositivo
        assertFalse(sensores.contains(sens1));
    }
}
