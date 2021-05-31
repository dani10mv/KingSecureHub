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
import android.widget.TextView;
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
    private TextView tvEstado;
    private boolean isSensor;


   private Casa casa;
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
        tvEstado = findViewById(R.id.tvEstadoUpdate);




        context=getApplicationContext();
        requestQueue = Volley.newRequestQueue(context);


        casa = (Casa) getIntent().getSerializableExtra("casa");

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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spEstado.setAdapter(adapter);

        }else//es actuador
        {
            isSensor=false;
            Actuador actuador=(Actuador) getIntent().getSerializableExtra("actuador");
            device=actuador;

            //no se puede cambiar manualmente el estado
            spEstado.setVisibility(View.INVISIBLE);
            tvEstado.setVisibility(View.INVISIBLE);

        }

        etName.setText(device.getNombre());



    }


    public void onClickUpdate(View view){
        if(! checkName())
            return;
        Dispositivo device = null ;
        Intent i  = new Intent(this,MainActivity.class);
        if(isSensor){
            Sensor sensor =(Sensor) getIntent().getSerializableExtra("sensor");
            if(sensor.getTipoSensor().toUpperCase().equals("APERTURA")) {
                EstadoSApertura estado = EstadoSApertura.valueOf(spEstado.getSelectedItem().toString());
                SensorApertura newSensor = (SensorApertura) getIntent().getSerializableExtra("sensor");
                newSensor.setEstado(estado);
                newSensor.setNombre(etName.getText().toString());
                casa.updateSensor(newSensor);
                sensor=newSensor;


            }else {//sensor movimiento
                EstadoSMovimiento estado =EstadoSMovimiento.valueOf(spEstado.getSelectedItem().toString());
                SensorMovimiento sensorMovimiento = (SensorMovimiento) getIntent().getSerializableExtra("sensor");
                sensorMovimiento.setEstado(estado);
                sensorMovimiento.setNombre(etName.getText().toString());
                casa.updateSensor(sensorMovimiento);
                sensor=sensorMovimiento;
            }
            i.putExtra("updateSensor",sensor);

        }else{ //actuador
            Actuador actuador=(Actuador) getIntent().getSerializableExtra("actuador");

            actuador.setNombre(etName.getText().toString());

            i.putExtra("updateActuador",(Serializable) actuador);
        }
        i.putExtra("casa",(Serializable) casa);


        startActivity(i);

    }

    public boolean checkName(){
        if(etName.getText().toString().equals(""))
            return false;
        return true;
    }


    public void onClickBack(View view){

        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("casa",(Serializable) casa);

        startActivity(i);

    }



}
