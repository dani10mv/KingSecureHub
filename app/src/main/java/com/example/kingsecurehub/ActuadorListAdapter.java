package com.example.kingsecurehub;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kingsecurehub.modelo.Actuador;
import com.example.kingsecurehub.modelo.Dispositivo;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class ActuadorListAdapter  extends ArrayAdapter<Actuador>   {

    private List<Actuador> lActuadores;
    private Context context;
    int resouceLayout;



    public ActuadorListAdapter(@NonNull Context context, int resource, List<Actuador>objects) {
        super(context, resource, objects);
        this.context=context;
        this.lActuadores =objects;
        this.resouceLayout=resource;

    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        if(view==null)
            view= LayoutInflater.from(context).inflate(R.layout.simple_device,null);

        final Actuador device = lActuadores.get(position);

        TextView name = view.findViewById(R.id.deviceName);
        name.setText(device.getNombre());

        TextView state = view.findViewById(R.id.state);
        state.setText(device.getEstado().toString());

        System.out.println("se esta acyualizando las cosas ");


        Button btnDelete =  view.findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigoActuador=  device.getCodigo();

                JsonObjectRequest actuadoresDeleteRequest = new JsonObjectRequest(Request.Method.DELETE, HostingUrl.getUrl()+"/actuador/delete/"+codigoActuador, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                lActuadores.remove(position);
                                notifyDataSetChanged();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();

                            }
                        });

                requestQueue.add(actuadoresDeleteRequest);

            }
        });



        return view;
    }


}
