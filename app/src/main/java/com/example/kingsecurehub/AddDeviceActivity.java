package com.example.kingsecurehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kingsecurehub.modelo.Actuador;
import com.example.kingsecurehub.modelo.EstadoActuador;
import com.example.kingsecurehub.modelo.EstadoSApertura;
import com.example.kingsecurehub.modelo.EstadoSMovimiento;
import com.example.kingsecurehub.modelo.Sensor;
import com.example.kingsecurehub.modelo.SensorMovimiento;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class AddDeviceActivity extends AppCompatActivity {
    private Context context ;

    private boolean isSensor;
   private RequestQueue requestQueue;
    private Button addButton, backButton;
    private Casa casa;
    private EditText etName, etCode;
    private Spinner ssType;
    private TextView tvType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        context=getApplicationContext();

        requestQueue = Volley.newRequestQueue(context);

        casa = (Casa) getIntent().getSerializableExtra("casa");

        isSensor = getIntent().getBooleanExtra("isSensor", false);

        addButton = findViewById(R.id.btAddFinal);
        backButton = findViewById(R.id.btBack);
        etName = findViewById(R.id.etNewName);
        etCode = findViewById(R.id.etNewCode);
        tvType = findViewById(R.id.tvSensorType);
        ssType = findViewById(R.id.spinnerSensor);


        if (!isSensor) {
            ssType.setVisibility(View.INVISIBLE);
            tvType.setVisibility(View.INVISIBLE);

        }


    }


    public void onClickAdd(View view) {

        String name = etName.getText().toString();
        String code = etCode.getText().toString();


        DeviceFactory factory = new DeviceFactory();



        if (!checkEntryValid()) {

            return;
        }

        if (isSensor) {
            Sensor sensor;
            String type = ssType.getSelectedItem().toString();


            if (type.toUpperCase().equals("MOVIMIENTO")) {

                sensor = factory.getSensorMovimiento(code, name, EstadoSMovimiento.DISCONNECTED);

            } else //(type.toUpperCase().equals("APERTURA"))
            {
                sensor = factory.getSensorApertura(code, name, EstadoSApertura.DISCONNECTED);

            }

            addNewSensorRequest(sensor);


        } else {

            Actuador actuador = factory.getActuador(code, name, EstadoActuador.DISCONNECTED);


            addNewActuadorRequest(actuador);


        }





    }

    public void onClickBack(View view){

        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("casa",(Serializable) casa);
        startActivity(i);
    }

    public boolean validCode(String code) {

        Toast t = Toast.makeText(this, "Codigo en uso", Toast.LENGTH_LONG);

        for (Sensor s : casa.getSensores()) {
            if (code.equals(s.getCodigo())) {
                t.show();
                return false;
            }

        }


        for (Actuador a : casa.getActuadores()) {
            if (code.equals(a.getCodigo())) {
                t.show();
                return false;
            }

        }
        return true;
    }


    public boolean checkEntryValid() {
        if (etCode.getText().toString().equals("") || etName.getText().toString().equals("") ) {
            Toast.makeText(this, "Campos obligatorios", Toast.LENGTH_LONG).show();
            return false;
        }
        return validCode(etCode.getText().toString());

    }


    public void addNewSensorRequest(final Sensor sensor) {


        JsonObjectRequest addSensorRequest = new JsonObjectRequest(Request.Method.POST, HostingUrl.getUrl()+"/sensor/add", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        casa.addSensor(sensor);
                        Intent i = new Intent(AddDeviceActivity.this, MainActivity.class);
                        i.putExtra("casa",(Serializable) casa);
                        startActivity(i);

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
        requestQueue.add(addSensorRequest);
    }






    public void addNewActuadorRequest(final Actuador actuador) {


        JsonObjectRequest addSensorRequest = new JsonObjectRequest(Request.Method.POST, HostingUrl.getUrl()+"/actuador/add", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        casa.addActuador(actuador);
                        Intent i = new Intent(AddDeviceActivity.this, MainActivity.class);
                        i.putExtra("casa",(Serializable) casa);
                        startActivity(i);


                        Toast.makeText(context,"añadido "+actuador.getNombre(),Toast.LENGTH_LONG).show();
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
        requestQueue.add(addSensorRequest);
    }




}
