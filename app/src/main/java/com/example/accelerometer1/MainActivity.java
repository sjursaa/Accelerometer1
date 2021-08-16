package com.example.accelerometer1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView xValAcc, yValAcc, zValAcc, xValGyr, yValGyr, zValGyr, currentTime, currentMilliTime;
    private Sensor accelerometer, gyroscope;
    private SensorManager sensorManager1, sensorManager2;

    private static final String TAG = "MyActivity";
    public static final int DEFAULT_UPDATE_INTERVAL = 20; // 50Hz
    public static final int FAST_UPDATE_INTERVAL = 10; // 100 Hz
    private static final int PERMISSIONS_FINE_LOCATION = 99;

    // Google API for location
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    LocationCallback locationCallback;

    //sss
    //public static final String ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;
    //Environment.isExternalStorageManager();



    Switch swHertz, swLocationUpdates;
    int samplingPeriodSlow = 1000000; // microseconds // 1Hz
    int samplingPeriodFast = 20000; // microseconds // 50Hz
    int samplingPeriod = samplingPeriodFast;
    int sleepyTime = 1000;

    // Timer t = new java.util.Timer();

    String[] lineForPrint;
    String lineForPrintLine;

    String gyrValueString, accValueString, timeStamp, timeStampMs;
    String longitude, latitude, latLongPrint;

    // char delay = SENSOR_DELAY_FASTEST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Date currentDateTime;// = Calendar.getInstance().getTime();

        // Create sensorManager
        sensorManager1 = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager2 = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        accelerometer = sensorManager1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope     = sensorManager2.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Register sensor listener.
        sensorManager1.registerListener(this, accelerometer, samplingPeriod);
        sensorManager2.registerListener(this, gyroscope, samplingPeriod);
        //sensorManager1.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_FASTEST);

        // Assign TextView
        xValAcc = (TextView)findViewById(R.id.xValAcc);
        yValAcc = (TextView)findViewById(R.id.yValAcc);
        zValAcc = (TextView)findViewById(R.id.zValAcc);

        xValGyr = (TextView)findViewById(R.id.xValGyr);
        yValGyr = (TextView)findViewById(R.id.yValGyr);
        zValGyr = (TextView)findViewById(R.id.zValGyr);

        currentTime = (TextView)findViewById(R.id.tvCurrentTime);
        currentMilliTime = (TextView)findViewById(R.id.tvCurrentMilliTime);
        swHertz = (Switch)findViewById(R.id.swHertz);
        swLocationUpdates = (Switch)findViewById(R.id.swLocationUpdates);

        /*
        swHertz.setOnClickListener(v -> {

            if (swHertz.isChecked()) {
                sleepyTime = 20;
            }
            else {
                sleepyTime = 100;
            }
        });
        */

        //printStuffToConsole();
        locationRequest = LocationRequest.create()
                .setInterval(DEFAULT_UPDATE_INTERVAL)
                .setFastestInterval(FAST_UPDATE_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_UPDATE_INTERVAL);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                updateLocationValues(location);
            }
        };

        swHertz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swHertz.isChecked()) {
                    sleepyTime = 20; // 50 Hz
                    //System.out.println(Arrays.toString(lineForPrint));
                }
                else {
                    sleepyTime = 1000; // 1 Hz
                }

            }
        });

        swLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swLocationUpdates.isChecked()) {
                    // Turn on location tracking
                    startLocationUpdates();
                } else {
                    // Turn off tracking
                    stopLocationUpdates();
                }
            }
        });

        updateGPS();

    }

    private void startLocationUpdates() {
        //tvUpdates.setText("Location is being tracked");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        updateGPS();
    }

    private void stopLocationUpdates() {
        //tvUpdates.setText("Location is not being tracked");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                }
                else {
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void updateGPS() {
        // getpermssions and updrse avatulse ands  tuff
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,)

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateLocationValues(location);
                }
            });
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void updateLocationValues(Location location) {
        if (location == null) {
            Log.d(TAG, "nullverdi lokasjon");
            return;
        }

        longitude = String.valueOf(location.getLongitude());
        latitude = String.valueOf(location.getLatitude());

        String[] latLong = {latitude, longitude};
        latLongPrint = Arrays.toString(latLong);
    }

    public synchronized void updateTime() {
        long detailTime = Calendar.getInstance().getTimeInMillis();
        Date currentDateTime = Calendar.getInstance().getTime();

        currentTime.setText(currentDateTime.toString());
        currentMilliTime.setText(String.valueOf(detailTime));

        timeStamp = currentDateTime.toString();
        timeStampMs = String.valueOf(detailTime);
    }

    // Accelerometer SensorEvent
    @Override
    public synchronized void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xValAcc.setText(String.valueOf(event.values[0]));
            yValAcc.setText(String.valueOf(event.values[1]));
            zValAcc.setText(String.valueOf(event.values[2]));

            //double xVal = event.values[0];
            //double yVal = event.values[1];
            //double zVal = event.values[2];
            double[] accValues = {event.values[0], event.values[1], event.values[2]};

            accValueString = Arrays.toString(accValues);

        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            xValGyr.setText(String.valueOf(event.values[0]));
            yValGyr.setText(String.valueOf(event.values[1]));
            zValGyr.setText(String.valueOf(event.values[2]));

            double[] gyrValues = {event.values[0], event.values[1], event.values[2]};

            gyrValueString = Arrays.toString(gyrValues);

            updateTime();

            String[] lineForPrint = {gyrValueString, accValueString, latLongPrint, timeStamp, timeStampMs};
            lineForPrintLine = Arrays.toString(lineForPrint);

            Log.i(TAG,lineForPrintLine);
            appendLog(lineForPrintLine);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use atm.
    }

    public void appendLog(String text){
        File logFile = new File(getFilesDir(), "log2.txt");
        /*
        if(Environment.isExternalStorageManager()){
            // logFile = new File(getFilesDir(), "log2.txt");
            /*
            try {
                //String directory = Environment.getExternalStorageState("/sdcard/log");
                logFile = new File(getFilesDir(), "log2.txt");
            }
            catch (IOException e){
                e.printStackTrace();
            }
             */
        }
        */

        if(!logFile.exists()){
            try {
                logFile.createNewFile();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        try{
           //BufferedWriter
           BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
           buf.append(text);
           buf.newLine();
           buf.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
