package com.app.tracking;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AlarmService extends Service {

    private GpsTracker gpsTracker = new GpsTracker(this);
    RequestQueue queue;

    //    private String API_API = "http://200.31.171.50/sistema2021";
    private String API_API = "http:///192.168.1.100/tracking";
    //private String API_ENDPOINT = "/insert_position.php";
    private String API_ENDPOINT = "/index.php";

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceTraking",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        Log.d("onCreate()", "After service created");

        queue = Volley.newRequestQueue(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //HERE PASS USER ASOCIATE TO PHONE
        sendTracking();
        Toast.makeText(getApplicationContext(), "Actualizando posicion", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    private void sendTracking() {

        Location location = getPosition();

        String params = "?placa=" + getPlaca() + "&latitud=" + location.getLatitude() + "&longitud=" + location.getLongitude();
        String url = API_API + API_ENDPOINT + params;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                },
                error -> {
                }
        );
        queue.add(stringRequest);
    }

    public String getPlaca() {

        SharedPreferences settings = this.getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
        return settings.getString("placa", "");
    }

    private Location getPosition() {
        return gpsTracker.getLocation();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
