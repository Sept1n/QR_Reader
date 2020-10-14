package com.example.qr_reader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    public static TextView qr_reader;
    Button btn_scan;
    public static Button button;
    TextView id_text;
    TextView name_text;
    TextView surname_text;
    TextView address_text;
    TextView city_text;
    TextView age_text;
    final String url = "http://10.0.2.2:8081/Patients/index.jsp";
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qr_reader = findViewById(R.id.result_text);
        btn_scan = findViewById(R.id.btn_scan);
        button = findViewById(R.id.button2);
        id_text = findViewById(R.id.textView2);
        name_text = findViewById(R.id.textView3);
        surname_text = findViewById(R.id.textView4);
        address_text = findViewById(R.id.textView5);
        city_text = findViewById(R.id.textView6);
        age_text = findViewById(R.id.textView7);


        requestQueue = Volley.newRequestQueue(MainActivity.this);

        btn_scan.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                if(id_text.length() > 0)
                {
                    id_text.setText(" ");
                    name_text.setText(" ");
                    surname_text.setText(" ");
                    address_text.setText(" ");
                    city_text.setText(" ");
                    age_text.setText(" ");
                }
                startActivity(new Intent(getApplicationContext(),ScanCodeActivity.class));
            }

        });

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {

                if(id_text.length() > 0)
                {
                    id_text.setText(" ");
                    name_text.setText(" ");
                    surname_text.setText(" ");
                    address_text.setText(" ");
                    city_text.setText(" ");
                    age_text.setText(" ");
                }
                final String id_qr = qr_reader.getText().toString();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendId(id_qr);
                    }
                }, 3000);
            }

        });


    }

    public void sendId (final String id_qr)
    {
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {

                        for (int i = 0; i < response.length(); i++) {

                            JSONObject patient = response.getJSONObject(i);

                            //String patient = patient1.toString();

                            String id = patient.getString("id");
                            String name = patient.getString("name");
                            String surname = patient.getString("surname");
                            String address = patient.getString("address");
                            String city = patient.getString("city");
                            String age = patient.getString("age");

                            long a = Long.parseLong(id);

                            if (a == Long.parseLong(id_qr)) {
                                id_text.append("Pesel: " + id);
                                name_text.append("Imię: " + name);
                                surname_text.append("Nazwisko: " + surname);
                                address_text.append("Adres: " + address);
                                city_text.append("Miasto: " + city);
                                age_text.append("Wiek: " + age);
                            }
                        }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    Toast.makeText(MainActivity.this, "Network error" + error.toString(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "Network Error " + error.toString());
                }
                else if (error instanceof ServerError)
                {
                    Toast.makeText(MainActivity.this, "Server error" + error.toString(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "Server Error " + error.toString());
                }
                else if (error instanceof AuthFailureError)
                {
                    Toast.makeText(MainActivity.this, "AuthFailureError" + error.toString(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "AuthFailureError " + error.toString());
                }
                else if (error instanceof ParseError)
                {
                    Toast.makeText(MainActivity.this, "ParseError" + error.toString(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "ParseError " + error.toString());
                }
                else if (error instanceof NoConnectionError)
                {
                    Toast.makeText(MainActivity.this, "NoConnectionError" + error.toString(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "NoConnectionError " + error.toString());
                }
                else if (error instanceof TimeoutError)
                {
                    Toast.makeText(MainActivity.this, "TimeoutError" + error.toString(), Toast.LENGTH_LONG).show();
                    Log.e("MainActivity", "TimeoutError " + error.toString());
                }
            }
        })/*{
            @Override
            public Map <String, String> getParams() throws AuthFailureError {
                Map <String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("id", id_qr);
                return params;
            }
        }*/;

        //jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);

    }

}
