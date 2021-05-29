package com.example.kingsecurehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kingsecurehub.modelo.Actuador;
import com.example.kingsecurehub.modelo.Dispositivo;
import com.example.kingsecurehub.modelo.EstadoActuador;
import com.example.kingsecurehub.modelo.EstadoSApertura;
import com.example.kingsecurehub.modelo.EstadoSMovimiento;
import com.example.kingsecurehub.modelo.Sensor;
import com.example.kingsecurehub.modelo.SensorApertura;
import com.example.kingsecurehub.modelo.SensorMovimiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {

    private Spinner spEstado;
    private Button btnUpdate;
    private Button btnBack;
    private EditText etName;
    private boolean isSensor;

    private List<Sensor> sensores;
    private List<Actuador> actuadores;
    private int position;



    private Context context;
    private RequestQueue requestQueue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        btnBack=findViewById(R.id.btnBackUpdate);
        btnUpdate=findViewById(R.id.btnUpdate);
        etName=findViewById(R.id.etNameUpdate);
        spEstado = findViewById(R.id.spEstadoUpdate);



        context=getApplicationContext();
        requestQueue = Volley.newRequestQueue(context);


        sensores = (List<Sensor>) getIntent().getSerializableExtra("sensores");
        actuadores = (List<Actuador>) getIntent().getSerializableExtra("actuadores");
        position= getIntent().getIntExtra("position",-1);

        isSensor=false;


        List<String> spinnerArray = new ArrayList<>();

        Dispositivo device ;
        if(getIntent().hasExtra("sensor")){
            Sensor sensor =(Sensor) getIntent().getSerializableExtra("sensor");
            spinnerArray =  sensor.getEstados();
            System.out.println("estamos en el buen if");
            device=sensor;

            isSensor=true;

        }else//es actuador
        {
            isSensor=false;
            Actuador actuador=(Actuador) getIntent().getSerializableExtra("actuador");
            device=actuador;
            spinnerArray.add("DISCONECTED");
            spinnerArray.add("OFF");
            spinnerArray.add("ON");


        }

        System.out.println(spinnerArray);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spEstado.setAdapter(adapter);

        etName.setText(device.getNombre());



    }


    public void onClickUpdate(View view){
        if(! checkName())
            return;
        Dispositivo device = null ;
        if(isSensor){
            Sensor sensor =(Sensor) getIntent().getSerializableExtra("sensor");
            if(sensor.getTipoSensor().toUpperCase().equals("APERTURA")) {
                EstadoSApertura estado = EstadoSApertura.valueOf(spEstado.getSelectedItem().toString());
                SensorApertura newSensor = (SensorApertura) getIntent().getSerializableExtra("sensor");
                newSensor.setEstado(estado);
                newSensor.setNombre(etName.getText().toString());
                sensores.set(position,newSensor);
                sensor=newSensor;


            }else {//sensor movimiento
                EstadoSMovimiento estado =EstadoSMovimiento.valueOf(spEstado.getSelectedItem().toString());
                SensorMovimiento sensorMovimiento = (SensorMovimiento) getIntent().getSerializableExtra("sensor");
                sensorMovimiento.setEstado(estado);
                sensorMovimiento.setNombre(etName.getText().toString());
                sensores.set(position,sensorMovimiento);
                sensor=sensorMovimiento;
            }
            updateSensorRequest(sensor);

        }else{ //actuador
            Actuador actuador=(Actuador) getIntent().getSerializableExtra("actuador");
            EstadoActuador estado = EstadoActuador.valueOf(spEstado.getSelectedItem().toString());
            actuador.setEstado(estado);
            actuador.setNombre(etName.getText().toString());
            actuadores.set(position,actuador);
        }




    }

    public boolean checkName(){
        if(etName.getText().toString().equals(""))
            return false;
        return true;
    }


    public void updateSensorRequest(final Sensor sensor) {


        JsonArrayRequest updateSensorRequest = new JsonArrayRequest(Request.Method.PUT, "https://kingserve.herokuapp.com/sensor/update", null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String codigoActuador = response.getString(i);
                                Actuador actuador= null;
                                for(Actuador a : actuadores){
                                    if(a.getCodigo().equals(codigoActuador)){
                                        actuador=a;
                                        break;
                                    }
                                }

                                if(actuador.getEstado()!=EstadoActuador.DISCONNECTED){
                                    actuador.setEstado(EstadoActuador.ON);
                                    updateActuadorRequest(actuador);

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                byte[] body= null;
                try {
                    body =sensor.toJsonByte();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return body;
            }
        };
        requestQueue.add(updateSensorRequest);
    }


    public void updateActuadorRequest(final Actuador actuador) {


        JsonObjectRequest updateActuadorRequest = new JsonObjectRequest(Request.Method.PUT, "https://kingserve.herokuapp.com/actuador/update", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        int pos =0;
                        for (Actuador a : actuadores){
                            if(a.getCodigo().equals(actuador.getCodigo())) {
                                actuadores.set(pos, actuador);
                                Intent i =new Intent(UpdateActivity.this,MainActivity.class);
                                i.putExtra("sensores",(Serializable) sensores);
                                i.putExtra("actuadores",(Serializable) actuadores);
                                startActivity(i);
                                return;
                            }
                            pos++;
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                byte[] body= null;
                try {
                    body =actuador.toJsonByte();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return body;
            }
        };
        requestQueue.add(updateActuadorRequest);
    }

}
