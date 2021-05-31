package com.example.kingsecurehub.aceptacion;

import com.example.kingsecurehub.Casa;
import com.example.kingsecurehub.exceptions.DispositivoExistenteException;
import com.example.kingsecurehub.modelo.Sensor;
import com.example.kingsecurehub.modelo.SensorApertura;
import com.example.kingsecurehub.modelo.SensorMovimiento;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

//Como usuario quiero ser capaz de a nyadir un nuevo dispositivo.
public class HU15 {

    private Casa casa;

    @Before
    public void inicializarCasa() {
        casa = new Casa();
    }

    @Test
    //No existe ningun dispositivo previamente y se quiere anyadir uno nuevo
    public void anyadirDispValido() throws DispositivoExistenteException {
        //Given: Un conjunto vacio de dispositivos
        //When: Se anyade un dispositivo nuevo
        Sensor sens1 = new SensorApertura("sens1","Sensor puerta balcon");
        casa.addSensor(sens1);
        //Then: El dispositivo se anyade correctamente
        boolean esta = false;
        for(Sensor s : casa.getSensores()){
            if (s.getCodigo().equals(sens1.getCodigo())){
                esta = true;
                break;
            }
        }
        assertTrue(esta);
    }
    @Test(expected = DispositivoExistenteException.class)
    //Existe un dispositivo y se quiere anyadir otro con el mismo nombre
    public void anyadirDispInvalido() throws DispositivoExistenteException {
        //Given: Un dispositivo previamente creado
        Sensor sens1 = new SensorApertura("sens1","Sensor puerta balcon");
        casa.addSensor(sens1);
        //When: Se intenta crear uno nuevo con el mismo codigo
        Sensor sens2 = new SensorMovimiento("sens1", "Sensor cocina");
        //Then: Se muestra el mensaje de error de que ya existe un dispositivo con dicho codigo
        casa.addSensor(sens2);
    }
}