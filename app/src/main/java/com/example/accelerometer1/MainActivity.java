package com.example.accelerometer1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView xValAcc, yValAcc, zValAcc, xValGyr, yValGyr, zValGyr, currentTime, currentMilliTime;
    private Sensor accelerometer, gyroscope;
    private SensorManager sensorManager1, sensorManager2;

    Switch swHertz;
    int samplingPeriodSlow = 1000000; // microseconds // 1Hz
    int samplingPeriodFast = 20000; // microseconds // 50Hz
    int samplingPeriod = samplingPeriodSlow;

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

        /*
        swHertz.setOnClickListener(v -> {

            if (swHertz.isChecked()) {
                samplingPeriod = samplingPeriodFast;
            }
            else {
                samplingPeriod = samplingPeriodSlow;
            }
        });
        */
        /*
        swHertz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swHertz.isChecked()) {
                    samplingPeriod = samplingPeriodFast;
                    setupSensorManager(samplingPeriod);
                }
                if(swHertz.isChecked() == false) {
                    samplingPeriod = samplingPeriodSlow;
                    setupSensorManager(samplingPeriod);
                }
            }
        });
        */
        //currentDateTime = Calendar.getInstance().getTime();
        //currentTime.setText(currentDateTime.toString());
    }
    /*
    public void setupSensorManager(int samplingPeriod) {
        sensorManager1 = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager2 = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        accelerometer = sensorManager1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager2.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager1.unregisterListener(this);
        sensorManager2.unregisterListener(this);

        // Register sensor listener.
        sensorManager1.registerListener(this, accelerometer, samplingPeriod);
        sensorManager2.registerListener(this, gyroscope, samplingPeriod);
    }
    */
    public synchronized void updateTime() {
        long detailTime = Calendar.getInstance().getTimeInMillis();
        Date currentDateTime = Calendar.getInstance().getTime();

        currentTime.setText(currentDateTime.toString());
        currentMilliTime.setText(String.valueOf(detailTime));
    }

    // Accelerometer SensorEvent
    @Override
    public synchronized void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xValAcc.setText(String.valueOf(event.values[0]));
            yValAcc.setText(String.valueOf(event.values[1]));
            zValAcc.setText(String.valueOf(event.values[2]));
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            xValGyr.setText(String.valueOf(event.values[0]));
            yValGyr.setText(String.valueOf(event.values[1]));
            zValGyr.setText(String.valueOf(event.values[2]));
        }
        updateTime();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use atm.
    }


}

