package com.example.panic.ui.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class SendNumberProduct extends CodBarsScanActivity{

    public RequestQueue volleyQueue;
    public String number;
    public Context context;

    SendNumberProduct(String number, Context context){
        super();
        this.number = number;
        this.context = context;
        this.volleyQueue = Volley.newRequestQueue(context);
    }

    public void send() {

        try {
            String url = "http://192.168.0.108:8080/product/decrease";

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("number", number);
            final String requestBody = jsonBody.toString();

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Estoque alterado com sucesso!", Toast.LENGTH_LONG).show();

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(context, "Erro " + number, Toast.LENGTH_LONG).show();
                }


            }){

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody(){
                    byte[] body;
                    body = requestBody.getBytes(StandardCharsets.UTF_8);
                    return body;
                }
            };

            volleyQueue.add(stringRequest);

        }catch(JSONException e){

            Toast.makeText(context, "Erro service server", Toast.LENGTH_LONG).show();

        }

    }
}
