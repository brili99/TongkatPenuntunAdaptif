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
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    TextInputEditText inputNama, inputEmail, inputPassword, inputKunci, inputTelp;
    MaterialCardView btnSignUp;
    String nama, email, password, kunci, telp;
    JSONObject jObject;
    String url = "https://pcbjogja.com/tpa/api/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputNama = (TextInputEditText) findViewById(R.id.inputNama);
        inputEmail = (TextInputEditText) findViewById(R.id.inputEmail);
        inputPassword = (TextInputEditText) findViewById(R.id.inputPassword);
        inputKunci = (TextInputEditText) findViewById(R.id.inputKunci);
        inputTelp = (TextInputEditText) findViewById(R.id.inputTelp);
        btnSignUp = (MaterialCardView) findViewById(R.id.btnSignUp);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            jObject = new JSONObject(response);
                            String res = jObject.getString("status").trim();
                            System.out.println(res);
                            if (res.contains("success")) {
                                Toast.makeText(SignUp.this, "Pendaftaran sukses", Toast.LENGTH_LONG).show();
                                finish();
                            } else if (res.contains("err")) {
                                Toast.makeText(SignUp.this, "Server error, mohon hubungi pihak developers", Toast.LENGTH_LONG).show();
                            } else if (res.contains("registered")) {
                                Toast.makeText(SignUp.this, "Kunci sudah digunakan oleh orang lain", Toast.LENGTH_LONG).show();
                            } else if (res.contains("notfound")) {
                                Toast.makeText(SignUp.this, "Kunci tidak ditemukan, mohon cek kembali", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SignUp.this, "Sepertinya server sedang bermasalah, atau sedang masa perbaikan", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUp.this, "Sepertinya ada masalah di jaringan, periksa internet anda.", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("password", password);
                params.put("kunci", kunci);
                params.put("telp", telp);
                return params;
            }
        };

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nama = inputNama.getText().toString().trim();
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                kunci = inputKunci.getText().toString().trim();
                telp = inputTelp.getText().toString().trim();

                if (nama == "" || nama == null) {
                    Toast.makeText(SignUp.this, "Mohon masukan nama", Toast.LENGTH_SHORT).show();
                } else if (email == "" || email == null) {
                    Toast.makeText(SignUp.this, "Mohon masukan email", Toast.LENGTH_SHORT).show();
                } else if (password == "" || password == null) {
                    Toast.makeText(SignUp.this, "Mohon masukan password", Toast.LENGTH_SHORT).show();
                } else if (kunci == "" || kunci == null) {
                    Toast.makeText(SignUp.this, "Mohon masukan kunci", Toast.LENGTH_SHORT).show();
                } else if (telp == "" || telp == null) {
                    Toast.makeText(SignUp.this, "Mohon masukan telphone", Toast.LENGTH_SHORT).show();
                } else {
//                    try {
//                        nama = URLEncoder.encode(nama, "utf-8");
//                        email = URLEncoder.encode(email, "utf-8");
//                        password = URLEncoder.encode(password, "utf-8");
//                        kunci = URLEncoder.encode(kunci, "utf-8");
//                        telp = URLEncoder.encode(telp, "utf-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                        Toast.makeText(SignUp.this, "Unsupported alphabet, please using classic latin alphabet.", Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    queue.add(postRequest);
                }
            }
        });
    }
}