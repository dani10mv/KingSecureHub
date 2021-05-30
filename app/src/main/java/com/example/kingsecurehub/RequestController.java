package com.example.kingsecurehub;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.kingsecurehub.modelo.Actuador;
import com.example.kingsecurehub.modelo.EstadoActuador;

import java.util.List;
import java.util.Queue;

public class RequestController {

    private Context context;
    private ActuadorListAdapter actuadorListAdapter;
    private SensorListAdapter sensorListAdapter;
    private RequestQueue requestQueue;
    private MainActivity activity;
    private Queue<JsonRequest> requests;
    private int codigo;

    public RequestController(MainActivity activity, Context context, ActuadorListAdapter actuadorListAdapter, SensorListAdapter sensorListAdapter, Queue<JsonRequest> requests) {

        this.context = context;
        this.actuadorListAdapter = actuadorListAdapter;
        this.sensorListAdapter = sensorListAdapter;
        this.activity = activity;
        this.requests=requests;

        requestQueue = Volley.newRequestQueue(context);
        codigo = 1;
    }


    public void notifyServer(){

        for(int i =0; i<requests.size();i++){
            requestQueue.add(requests.poll());
        }
    }


}
