package com.app.tracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    EditText txtPlaca;
    Button btnTracking;
    Button btnAbort;

    AlarmReceiver alarm;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtPlaca = findViewById(R.id.txtPlaca);
        btnTracking = findViewById(R.id.btnTracking);
        btnAbort = findViewById(R.id.btnAbort);

        settings = this.getSharedPreferences("data", Context.MODE_MULTI_PROCESS);


        // manage service
        btnTracking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initService();
            }
        });

        btnAbort.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    alarm.cancelAlarm();
                    Toast.makeText(getApplicationContext(), "RECORRIDO FINALIZADO", Toast.LENGTH_SHORT).show();
                    finish();

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initService() {
        if (saveUser(txtPlaca.getText().toString())) {
            //CALLING SERVICE
            alarm = new AlarmReceiver();
            alarm.setAlarm(this);
        }
    }

    boolean saveUser(String placa) {

        if (placa.isEmpty()) return false;

        settings.edit().clear().commit();

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("placa", placa);
        editor.commit();
        editor.apply();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}