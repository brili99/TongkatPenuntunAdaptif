package com.example.tongkatpenuntunadaptif;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView txtSignUp, forgotPassword;
    TextInputEditText inEmail, inPasswd;
    MaterialCardView btnLogin;
    JSONObject jObject;
    String kunci;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        btnLogin = (MaterialCardView) findViewById(R.id.btnLogin);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        inEmail = (TextInputEditText) findViewById(R.id.inEmail);
        inPasswd = (TextInputEditText) findViewById(R.id.inPasswd);

        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPref = getSharedPreferences("kunci_app_tpa", 0);
//        SharedPreferences.Editor mEditor = mPrefs.edit();
//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        kunci = sharedPref.getString("kunci", "kosong");
        if (!kunci.contains("kosong")){
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
        }
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
//                Toast.makeText(MainActivity.this, "Sign Up", Toast.LENGTH_SHORT).show();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "https://pcbjogja.com/tpa/api/login?" + "email=" + inEmail.getText() + "&passwd=" + inPasswd.getText();
                System.out.println(url);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                try {
                                    jObject = new JSONObject(response);
                                    String res = jObject.getString("status").trim();
                                    System.out.println(res);
                                    if (res.contains("notfound")) {
                                        Toast.makeText(MainActivity.this, "Wrong email/password", Toast.LENGTH_SHORT).show();
                                    } else if (res.contains("success")){
                                        String kunci = jObject.getString("kunci").trim();
                                        editor.putString("kunci", kunci);
                                        editor.apply();
                                        startActivity(new Intent(MainActivity.this, MapsActivity.class));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something error, check your network.", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
//                Toast.makeText(MainActivity.this, "Forgot Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}