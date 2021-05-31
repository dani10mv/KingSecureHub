package com.example.kingsecurehub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kingsecurehub.modelo.Actuador;
import com.example.kingsecurehub.modelo.Sensor;
import com.example.kingsecurehub.modelo.SensorApertura;
import com.example.kingsecurehub.modelo.SensorMovimiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SensorListAdapter  extends ArrayAdapter<Sensor> {

    private List<Sensor> lSensores;
    private Context context;
    int resouceLayout;



    public SensorListAdapter(@NonNull Context context, int resource, List<Sensor> objects) {
        super(context, resource, objects);
        this.context=context;
        this.lSensores =objects;
        this.resouceLayout=resource;



    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        View view = convertView;

        if(view==null)
            view= LayoutInflater.from(context).inflate(R.layout.simple_device,null);

        final Sensor device = lSensores.get(position);

        TextView name = view.findViewById(R.id.deviceName);
        name.setText(device.getNombre());

        TextView state = view.findViewById(R.id.state);

        ImageView ivEstado = view.findViewById(R.id.ivEstado);
        ivEstado.setImageResource(lSensores.get(position).getEstadoDrawable());

        if(device.getTipoSensor().equals("movimiento")) {
            SensorMovimiento s = (SensorMovimiento) device;
            state.setText(s.getEstado().toString());
        }else if (device.getTipoSensor().equals("Apertura")){
            SensorApertura s = (SensorApertura) device;
            state.setText(s.getEstado().toString());
        }


        Button btnDelete =  view.findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigoSensor =  device.getCodigo();

                JsonObjectRequest deleteSensorRequest = new JsonObjectRequest(Request.Method.DELETE, HostingUrl.getUrl()+"/sensor/delete/"+codigoSensor, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                lSensores.remove(position);
                                notifyDataSetChanged();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();

                            }
                        });

                requestQueue.add(deleteSensorRequest);

            }
        });



        return view;
    }


}
