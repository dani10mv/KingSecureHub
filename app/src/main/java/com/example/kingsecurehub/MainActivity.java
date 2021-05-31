package com.example.kingsecurehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.kingsecurehub.modelo.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//https://rapid-mini-screwdriver.glitch.me/
public class MainActivity extends AppCompatActivity {

    private Context context;

    private boolean clicked = false;

    private ListView lvSensores;
    private ListView lvActuadores;

    private FloatingActionButton addButton, addSensorButton, addAlertButton;

    private SensorListAdapter mSensorAdapter;
    private ActuadorListAdapter mActuadorAdapter;

    private  RequestController requestController = RequestController.getInstance();


    private RequestQueue requestQueue;
    private Casa casa= new Casa();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvSensores = findViewById(R.id.lvSensores);
        lvActuadores = findViewById(R.id.lvActuadores);
        addButton = findViewById(R.id.addButton);
        addAlertButton = findViewById(R.id.addAlertButton);
        addSensorButton = findViewById(R.id.addSensorButton);

        context = getApplicationContext();

        requestQueue = Volley.newRequestQueue(getApplicationContext());


        if (getIntent().hasExtra("casa")) {

            casa = (Casa) getIntent().getSerializableExtra("casa");

        } else {
            getSensoresApertura();
            getSensoresMovimiento();
            getActuadores();
        }



        if (getIntent().hasExtra("updateSensor")) {

            Sensor updateSensor = (Sensor) getIntent().getSerializableExtra("updateSensor");
            JsonRequest req = getupdateSensorRequest(updateSensor);
            requestController.addRequest(req);
        } else if (getIntent().hasExtra("updateActuador")) {
            Actuador updateActuador = (Actuador) getIntent().getSerializableExtra("updateActuador");
            updateActuadorRequest(updateActuador);
        }



        mSensorAdapter = new SensorListAdapter(this, R.layout.simple_device, casa.getSensores());
        lvSensores.setAdapter(mSensorAdapter);

        mActuadorAdapter = new ActuadorListAdapter(this, R.layout.simple_device, casa.getActuadores());
        lvActuadores.setAdapter(mActuadorAdapter);


        mActuadorAdapter.notifyDataSetChanged();
        mSensorAdapter.notifyDataSetChanged();

        System.out.println("se actualizan las listas");

        lvSensores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Sensor sensor = (Sensor) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                intent.putExtra("sensor", sensor);
                intent.putExtra("casa", (Serializable) casa);
                intent.putExtra("position", i);
                startActivity(intent);

            }
        });

        lvActuadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Actuador actuador = (Actuador) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                intent.putExtra("actuador", actuador);
                intent.putExtra("casa",(Serializable) casa);

                startActivity(intent);

            }
        });



        if (getIntent().getExtras()== null) {
            //First run only
            requestController = new RequestController(this, context, mActuadorAdapter, mSensorAdapter);
            Thread t = new RequestThread(requestController);
            t.start();
        }


    }


    public void getSensoresApertura() {

        JsonArrayRequest sensoresAperturaRequest = new JsonArrayRequest(Request.Method.GET, HostingUrl.getUrl() + "/sensores/apertura", null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            System.out.println("entramos");
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject device = response.getJSONObject(i);
                                String name = device.getString("nombre");

                                System.out.println(name);

                                SensorApertura sensorApertura = SensorApertura.fromJson(device);
                                casa.addSensor(sensorApertura);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mSensorAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(sensoresAperturaRequest);


    }


    public void getSensoresMovimiento() {

        JsonArrayRequest sensoresMovimientoRequest = new JsonArrayRequest(Request.Method.GET, HostingUrl.getUrl() + "/sensores/movimiento", null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            System.out.println("entramos");
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject device = response.getJSONObject(i);
                                String name = device.getString("nombre");

                                System.out.println(name);

                                SensorMovimiento sensorMovimiento = SensorMovimiento.fromJson(device);
                                casa.addSensor(sensorMovimiento);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("mi putisima madre");
                        }

                        mSensorAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(sensoresMovimientoRequest);


    }


    public void getActuadores() {

        JsonArrayRequest actuadoresRequest = new JsonArrayRequest(Request.Method.GET, HostingUrl.getUrl() + "/actuadores", null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            System.out.println("entramos");
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject device = response.getJSONObject(i);
                                String name = device.getString("nombre");

                                System.out.println(name);

                                Actuador actuador = Actuador.fromJson(device);
                                casa.addActuador(actuador);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("mi putisima madre");
                        }
                        mActuadorAdapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(actuadoresRequest);


    }

    public void onClickAdd(View view) {
        clicked = !clicked;

        if (clicked) {
            Animation animationRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_open_add);
            addButton.startAnimation(animationRotate);


            Animation showButton = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_bottom);
            addAlertButton.startAnimation(showButton);
            addSensorButton.startAnimation(showButton);


        } else {
            Animation animationRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_close_add);
            addButton.startAnimation(animationRotate);

            Animation hideButton = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_bottom);
            addAlertButton.startAnimation(hideButton);
            addSensorButton.startAnimation(hideButton);


        }


    }


    public void onClickOnAddSensor(View view) {

        Intent i = new Intent(this, AddDeviceActivity.class);
        i.putExtra("casa", (Serializable) casa);

        i.putExtra("isSensor", true);
        startActivity(i);

    }

    public void onClickOnAddActuador(View view) {

        Intent i = new Intent(this, AddDeviceActivity.class);
        i.putExtra("casa", (Serializable) casa);
        i.putExtra("isSensor", false);
        startActivity(i);

    }


    public JsonArrayRequest getupdateSensorRequest(final Sensor sensor) {


        JsonArrayRequest updateSensorRequest = new JsonArrayRequest(Request.Method.PUT, HostingUrl.getUrl() + "/sensor/update", null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String codigoActuador = response.getString(i);
                                Actuador actuador = null;
                                for (Actuador a : casa.getActuadores()) {
                                    if (a.getCodigo().equals(codigoActuador)) {
                                        actuador = a;
                                        break;
                                    }
                                }

                                if (actuador.getEstado() != EstadoActuador.DISCONNECTED) {
                                    actuador.setEstado(EstadoActuador.ON);
                                    updateActuadorRequest(actuador);

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mSensorAdapter.notifyDataSetChanged();

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
                byte[] body = null;
                try {
                    body = sensor.toJsonByte();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return body;
            }
        };
        return updateSensorRequest;
    }


    public void updateActuadorRequest(final Actuador actuador) {


        JsonObjectRequest updateActuadorRequest = new JsonObjectRequest(Request.Method.PUT, HostingUrl.getUrl() + "/actuador/update", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        int pos = 0;
                        for (Actuador a : casa.getActuadores()) {
                            if (a.getCodigo().equals(actuador.getCodigo())) {
                                casa.updateActuador(actuador);
                                mActuadorAdapter.notifyDataSetChanged();
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
                byte[] body = null;
                try {
                    body = actuador.toJsonByte();
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
