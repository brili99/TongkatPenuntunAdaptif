package com.example.tongkatpenuntunadaptif;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.tongkatpenuntunadaptif.databinding.ActivityMapsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private double lat, lon;
    private String kunci = "";
    Marker marker;
    JSONObject jObject;
    Button btnNama, btnLogout, btnSend1, btnSend2, btnSend3;
    MarkerOptions markerOptions = new MarkerOptions();
    LatLng pasien
            = new LatLng(-7.7434024, 110.3904714);
    String nama = "";
    long tmp_last_notif, last_notif;
    TextView txtMapUpdate, txtspo2, txtJantung, txtTimeRespon, txtMsgRespon;
    NotificationCompat.Builder builder;
    Uri sound;
    int code_cloud;
    public static final String NOTIFICATION_CHANNEL_ID = "tpa_ch1";
    private final static String default_notification_channel_id = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnNama = (Button) findViewById(R.id.btnNama);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnSend1 = (Button) findViewById(R.id.btnSend1);
        btnSend2 = (Button) findViewById(R.id.btnSend2);
        btnSend3 = (Button) findViewById(R.id.btnSend3);
        txtMapUpdate = (TextView) findViewById(R.id.txtMapUpdate);
        txtspo2 = (TextView) findViewById(R.id.txtspo2);
        txtJantung = (TextView) findViewById(R.id.txtJantung);
        txtTimeRespon = (TextView) findViewById(R.id.txtTimeRespon);
        txtMsgRespon = (TextView) findViewById(R.id.txtMsgRespon);

        final MediaPlayer mp_1 = MediaPlayer.create(this, R.raw.panik_panik);
        final MediaPlayer mp_2 = MediaPlayer.create(this, R.raw.telfon_saya);
        final MediaPlayer mp_3 = MediaPlayer.create(this, R.raw.saya_baik_baik_saja);
        final MediaPlayer mp_4 = MediaPlayer.create(this, R.raw.iya);
        final MediaPlayer mp_5 = MediaPlayer.create(this, R.raw.tidak);

        SharedPreferences sharedPref = getSharedPreferences("kunci_app_tpa", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        kunci = sharedPref.getString("kunci", "kosong");
        String url = "https://pcbjogja.com/tpa/api/status?kunci=" + kunci;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jObject = new JSONObject(response);
//                            String res = jObject.getString("status").trim();
                            nama = jObject.getString("nama").trim();
                            btnNama.setText(nama);
                            txtMapUpdate.setText("Last update: " + jObject.getString("last_update").trim());
                            txtspo2.setText("Oksigen: " + jObject.getString("spo2").trim());
                            txtJantung.setText("Jantung: " + jObject.getString("bpm").trim());
                            tmp_last_notif = jObject.getLong("last_notif");
                            if (last_notif != tmp_last_notif) {
                                last_notif = tmp_last_notif;
                                code_cloud = jObject.getInt("code_cloud");
                                txtTimeRespon.setText(jObject.getString("last_update").trim());
//                                if (code_cloud == 0) {
//                                    txtMsgRespon.setText("standby");
//                                }
                                String msg = "";
                                if (code_cloud == 1) {
                                    txtMsgRespon.setText("Kode Panik");
                                    msg = "Pengguna tongkat panik";
                                    mp_1.start();
//                                    sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/panik_panik.mp3");
                                } else if (code_cloud == 2) {
                                    txtMsgRespon.setText("Telpon saya");
                                    msg = "Telpon saya";
                                    mp_2.start();
//                                    sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/telfon_saya.mp3");
                                } else if (code_cloud == 3) {
                                    txtMsgRespon.setText("Saya baik baik saja");
                                    msg = "Saya baik baik saja";
                                    mp_3.start();
//                                    sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/saya_baik_baik_saja.mp3");
                                } else if (code_cloud == 4) {
                                    txtMsgRespon.setText("Iya");
                                    msg = "Iya";
                                    mp_4.start();
//                                    sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/iya.mp3");
                                } else if (code_cloud == 5) {
                                    txtMsgRespon.setText("Tidak");
                                    msg = "Tidak";
                                    mp_5.start();
//                                    sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/tidak.mp3");
                                }
                                if (!msg.equals("")) {
                                    builder = new NotificationCompat.Builder(MapsActivity.this,
                                            default_notification_channel_id)
                                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                                            .setContentTitle("Pesan dari pengguna tongkat")
//                                            .setSound(sound)
                                            .setContentText(msg);

                                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                                                .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                                                .setUsage(AudioAttributes. USAGE_ALARM )
                                                .build() ;
                                        int importance = NotificationManager.IMPORTANCE_HIGH;
                                        NotificationChannel notificationChannel = new
                                                NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                                        notificationChannel.enableLights(true);
                                        notificationChannel.setLightColor(Color.RED);
                                        notificationChannel.enableVibration(true);
                                        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                                        notificationChannel.setSound(sound , audioAttributes) ;
                                        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
                                        assert mNotificationManager != null;
                                        mNotificationManager.createNotificationChannel(notificationChannel);
                                    }
                                    assert mNotificationManager != null;
                                    mNotificationManager.notify((int) System.currentTimeMillis(), builder.build());
                                }
                            }
                            lat = jObject.getDouble("latitude");
                            lon = jObject.getDouble("longitude");
                            mapFragment.onPause();
                            pasien = new LatLng(lat, lon);

                            markerOptions.position(pasien);
//                            markerOptions.title(nama);
                            marker.setPosition(pasien);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(pasien));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        Timer timer = new Timer();
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                queue.add(stringRequest);
                if (code_cloud == 1) {
                    mp_1.start();
                }
            }
        };
        timer.scheduleAtFixedRate(t, 0, 5000);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                t.cancel();
                editor.putString("kunci", "kosong");
                editor.apply();
//                startActivity(new Intent(MapsActivity.this, MainActivity.class));
                finish();
            }
        });

        btnSend1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendDataRes(1);
            }
        });

        btnSend2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendDataRes(2);
            }
        });

        btnSend3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendDataRes(3);
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);

        markerOptions.position(pasien);
//        markerOptions.title(nama);
        marker = mMap.addMarker(markerOptions);
//        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pasien, 15));
    }

    public void sendDataRes(int mode) {
        RequestQueue queue2 = Volley.newRequestQueue(this);
        String url = "https://pcbjogja.com/tpa/api/com?kunci=" + kunci;
        url += "&code_device=";
        url += mode;
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        System.out.println("Sending");
                        Toast.makeText(MapsActivity.this, "Pengiriman pesan sukses", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Toast.makeText(MapsActivity.this, "Gagal mengirimkan pesan", Toast.LENGTH_SHORT).show();
            }
        });
        queue2.add(stringRequest2);
    }

//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "notif_tpa";
//            String description = "channel app tpa untuk notifikasi pengguna tongkat";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
}