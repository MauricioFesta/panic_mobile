package com.stock.panic.ui.camera;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stock.panic.data.model.CameraSql;
import com.stock.panic.utils.TokenRequest;
import com.stock.panic.utils.Uris;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SendNumberProduct extends BarcodeScanActivity{

    public RequestQueue volleyQueue;
    public String number;
    public Context context;
    public Uris uris;
    public SqLite sql;
    public CameraSql cameraSql;
    public TokenRequest tokenRequest;

    SendNumberProduct(String number, Context context){
        super();
        this.number = number;
        this.context = context;
        this.volleyQueue = Volley.newRequestQueue(context);
        this.cameraSql = new CameraSql();
        this.sql = new SqLite(context);
        this.uris = new Uris();
        this.tokenRequest = new TokenRequest();
    }

    public void send() {

        SQLiteDatabase db = sql.getWritableDatabase();

        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("codBarras", number);
            final String requestBody = jsonBody.toString();

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, uris.getApiDecreaseStockUri(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            Toast.makeText(context, "Estoque alterado com sucesso! " +  response.indexOf(0), Toast.LENGTH_LONG).show();

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if(error.networkResponse.statusCode == 401){
                        Toast.makeText(context, "Deletando token.....", Toast.LENGTH_LONG).show();
                        db.execSQL(cameraSql.getSqlTruncateTable());

                    }

                    if(error.networkResponse.statusCode == 405){
                        Toast.makeText(context, "Equipamento não encontrado!!", Toast.LENGTH_LONG).show();
                    }

                    Toast.makeText(context, "Erro " +  tokenRequest.getContaId(context), Toast.LENGTH_LONG).show();
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

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + tokenRequest.getToken(context));
                    headers.put("AuthorizationContaId", tokenRequest.getContaId(context));
                    return headers;
                }
            };

            volleyQueue.add(stringRequest);

        }catch(JSONException e){

            Toast.makeText(context, "Erro service server", Toast.LENGTH_LONG).show();

        }

    }
}
