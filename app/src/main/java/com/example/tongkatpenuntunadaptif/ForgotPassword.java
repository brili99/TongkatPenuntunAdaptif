package com.example.tongkatpenuntunadaptif;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ForgotPassword extends AppCompatActivity {
    TextInputEditText inputForgotEmail;
    MaterialCardView btnForgot;
    String url1 = "https://pcbjogja.com/tpa/api/forgot?email=";
    String url2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnForgot = (MaterialCardView) findViewById(R.id.btnForgot);
        inputForgotEmail = (TextInputEditText) findViewById(R.id.inputForgotEmail);


        RequestQueue queue = Volley.newRequestQueue(this);

        btnForgot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                try {
//                    url2 = url1 + URLEncoder.encode(inputForgotEmail.getText().toString().trim(), "utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        url1 + inputForgotEmail.getText(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("Response: '"+response+"'");
                                if (response.contains("success")) {
                                    Toast.makeText(ForgotPassword.this, "Sukses, Cek email anda.", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Gagal mengirimkan, ada yang salah", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });
                queue.add(stringRequest);
            }
        });
    }
}