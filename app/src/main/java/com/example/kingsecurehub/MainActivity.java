package com.example.kingsecurehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kingsecurehub.modelo.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private boolean clicked = false;

    private ListView lvSensores;
    private ListView lvActuadores;

    private FloatingActionButton addButton, addSensorButton, addAlertButton;

    private ArrayAdapter<Sensor> mSensorAdapter;
    private ActuadorListAdapter mActuadorAdapter;

    private RequestQueue requestQueue;
    private List<Sensor> sensores = new ArrayList<>();
    private List<Actuador> actuadores = new ArrayList<>();


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



        lvActuadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Actuador actuador =  (Actuador) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
                intent.putExtra("actuador",actuador);
                intent.putExtra("sensores", (Serializable) sensores);
                intent.putExtra("actuadores", (Serializable) actuadores);

                startActivity(intent);

            }
        });



        if (getIntent().hasExtra("sensores")) {

            sensores = (List<Sensor>) getIntent().getSerializableExtra("sensores");

            actuadores = (List<Actuador>) getIntent().getSerializableExtra("actuadores");

        } else {
            getSensoresApertura();
            getSensoresMovimiento();
            getActuadores();
        }

        System.out.println(sensores.size());

        mSensorAdapter = new SensorListAdapter(this, R.layout.simple_device, sensores);
        lvSensores.setAdapter(mSensorAdapter);

        mActuadorAdapter = new ActuadorListAdapter(this, R.layout.simple_device, actuadores);
        lvActuadores.setAdapter(mActuadorAdapter);


        mActuadorAdapter.notifyDataSetChanged();
        mSensorAdapter.notifyDataSetChanged();

        System.out.println("se actualizan las listas");

        lvSensores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Sensor sensor =  (Sensor) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
                intent.putExtra("sensor",sensor);
                intent.putExtra("sensores", (Serializable) sensores);
                intent.putExtra("actuadores", (Serializable) actuadores);
                intent.putExtra("position",i);
                startActivity(intent);

            }
        });


    }


    public void getSensoresApertura() {

        JsonArrayRequest sensoresAperturaRequest = new JsonArrayRequest(Request.Method.GET, "https://kingserve.herokuapp.com/sensores/apertura", null,
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
                                sensores.add(sensorApertura);

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

        JsonArrayRequest sensoresMovimientoRequest = new JsonArrayRequest(Request.Method.GET, "https://kingserve.herokuapp.com/sensores/movimiento", null,
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
                                sensores.add(sensorMovimiento);

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

        JsonArrayRequest actuadoresRequest = new JsonArrayRequest(Request.Method.GET, "https://kingserve.herokuapp.com/actuadores", null,
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
                                actuadores.add(actuador);

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
        i.putExtra("sensores", (Serializable) sensores);
        i.putExtra("actuadores", (Serializable) actuadores);
        i.putExtra("isSensor", true);
        startActivity(i);

    }

    public void onClickOnAddActuador(View view) {

        Intent i = new Intent(this, AddDeviceActivity.class);
        i.putExtra("sensores", (Serializable) sensores);
        i.putExtra("actuadores", (Serializable) actuadores);
        i.putExtra("isSensor", false);
        startActivity(i);

    }

}
