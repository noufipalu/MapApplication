package com.example.mapapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button show, listen, current;
    TextView location;
    ListView list;

    LocationManager locationManager;
    String lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show = findViewById(R.id.btn_show);
        listen = findViewById(R.id.btnLocLisner);
        current = findViewById(R.id.btn_getCurLoc);
        location = findViewById(R.id.tv_location);
        list = findViewById(R.id.listview);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(new Criteria(), false);

        String provider = "";
        for (String prov : providers){
            provider += prov + "\n";
        }
        Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_main, providers);
        list.setAdapter(adapter);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!providerEnabled){
                    enableGPS();
                }
                else {
                 getLocation();
                }
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            String[] prmsn = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };

            ActivityCompat.requestPermissions(this, prmsn, 1);
        }else{
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (!location1.equals(null)){
                lat = location1.getLatitude()+"";
                lon = location1.getLongitude()+"";
                location.setText("Latitude:" + lat + "\n" + "Longitude:" + lon);
            }
        }
    }

    private void enableGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable Gps").setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}