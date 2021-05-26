package com.example.kingsecurehub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kingsecurehub.modelo.Sensor;
import com.example.kingsecurehub.modelo.*;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ListView lvDevices ;

    ArrayAdapter<Dispositivo> mAdapter ;
     RequestQueue requestQueue ;
     List<Dispositivo> dispositivos= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvDevices = findViewById(R.id.lvDevices);

        requestQueue = Volley.newRequestQueue(getApplicationContext());


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kingserve.herokuapp.com")
                .build();


        System.out.println("hola_");

        mAdapter=new ArrayAdapter<>(this,R.layout.simple_list_item_1,dispositivos);



        getSensoresApertura();
        getSensoresMovimiento();
        getActuadores();




    }


    public void getSensoresApertura(){

        JsonArrayRequest sensoresAperturaRequest = new JsonArrayRequest(Request.Method.GET, "https://kingserve.herokuapp.com/sensores/apertura", null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            lvDevices.setAdapter(mAdapter);
                            System.out.println("entramos");
                            for(int i =0 ;i<response.length();i++){
                                JSONObject device = response.getJSONObject(i);
                                String name = device.getString("nombre");

                                System.out.println(name);

                                SensorApertura sensorApertura =  SensorApertura.fromJson(device);
                                dispositivos.add(sensorApertura);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("mi putisima madre");
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
                });

        requestQueue.add(sensoresAperturaRequest);



    }


    public void getSensoresMovimiento(){

        JsonArrayRequest sensoresMovimientoRequest = new JsonArrayRequest(Request.Method.GET, "https://kingserve.herokuapp.com/sensores/movimiento", null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            lvDevices.setAdapter(mAdapter);
                            System.out.println("entramos");
                            for(int i =0 ;i<response.length();i++){
                                JSONObject device = response.getJSONObject(i);
                                String name = device.getString("nombre");

                                System.out.println(name);

                                SensorMovimiento sensorMovimiento =  SensorMovimiento.fromJson(device);
                                dispositivos.add(sensorMovimiento);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("mi putisima madre");
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
                });

        requestQueue.add(sensoresMovimientoRequest );



    }

    public void getActuadores(){

        JsonArrayRequest actuadoresRequest = new JsonArrayRequest(Request.Method.GET, "https://kingserve.herokuapp.com/actuadores", null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            lvDevices.setAdapter(mAdapter);
                            System.out.println("entramos");
                            for(int i =0 ;i<response.length();i++){
                                JSONObject device = response.getJSONObject(i);
                                String name = device.getString("nombre");

                                System.out.println(name);

                                Actuador actuadore =  Actuador.fromJson(device);
                                dispositivos.add(actuadore);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("mi putisima madre");
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
                });

        requestQueue.add(actuadoresRequest );



    }

}
