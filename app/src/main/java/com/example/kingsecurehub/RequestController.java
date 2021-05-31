package com.example.kingsecurehub;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.kingsecurehub.modelo.Actuador;
import com.example.kingsecurehub.modelo.EstadoActuador;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RequestController {

    private Context context;
    private ActuadorListAdapter actuadorListAdapter;
    private SensorListAdapter sensorListAdapter;
    private RequestQueue requestQueue;
    private MainActivity activity;
    private static Queue<JsonRequest> requests;
    private int codigo;
    private static RequestController instance ;

    public  RequestController(MainActivity activity, Context context, ActuadorListAdapter actuadorListAdapter, SensorListAdapter sensorListAdapter ) {

        this.context = context;
        this.actuadorListAdapter = actuadorListAdapter;
        this.sensorListAdapter = sensorListAdapter;
        this.activity = activity;
        this.requests=new LinkedList<>();

        requestQueue = Volley.newRequestQueue(context);
        codigo = 1;
        instance=this;
    }

    public static RequestController getInstance(){
        return instance;
    }


    public void notifyServer(){
        System.out.println("size en el metodo: "+requests.size());

        for(int i =0; i<requests.size();i++){
            requestQueue.add(requests.poll());
        }
    }


    public void addRequest(JsonRequest request){
        requests.add(request);
    }


}
