package com.example.accelerometer1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.provider.Settings;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView xValAcc, yValAcc, zValAcc, xValGyr, yValGyr, zValGyr, currentTime, currentMilliTime;
    private Sensor accelerometer, gyroscope;
    private SensorManager sensorManager1, sensorManager2;

    Switch swHertz;
    int samplingPeriodSlow = 1000000; // microseconds // 1Hz
    int samplingPeriodFast = 20000; // microseconds // 50Hz
    int samplingPeriod = samplingPeriodFast;
    int sleepyTime = 1000;

    // Timer t = new java.util.Timer();

    String[] lineForPrint;
    String lineForPrintLine;

    String gyrValueString, accValueString, timeStamp, timeStampMs;

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




        /*
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.out.println(lineForPrintLine);
                    }
                },
                100
        );
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

        timeStamp = currentDateTime.toString();
        timeStampMs = String.valueOf(detailTime);
    }


    /*
    public synchronized double[] putValuesInArray(double xVal, double yVal, double zVal) {
        return new double[]{xVal, yVal, zVal};
    }
    */

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
            //double[] stuffAcc = putValuesInArray(event.values[0], event.values[1], event.values[2]);
            //System.out.println(Arrays.toString(accValues));
            //System.out.println(Arrays.toString(stuffAcc));

            accValueString = Arrays.toString(accValues);
            //System.out.println(accValueString);

        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            xValGyr.setText(String.valueOf(event.values[0]));
            yValGyr.setText(String.valueOf(event.values[1]));
            zValGyr.setText(String.valueOf(event.values[2]));

            double[] gyrValues = {event.values[0], event.values[1], event.values[2]};
            // System.out.println(Arrays.toString(gyrValues));
            gyrValueString = Arrays.toString(gyrValues);
            //System.out.println(gyrValueString);
            //double[] stuffGyr = putValuesInArray(event.values[0], event.values[1], event.values[2]);
            //System.out.println(Arrays.toString(stuffGyr));
        }
        updateTime();

        //String[] lineForPrint = {gyrValueString, accValueString, timeStamp, timeStampMs};
        String[] lineForPrint = {gyrValueString, accValueString, timeStamp, timeStampMs};
        lineForPrintLine = Arrays.toString(lineForPrint);
        System.out.println(Arrays.toString(lineForPrint));
        // System.out.println(sleepyTime);
        // System.out.println(toString(sleepyTime));

        /*
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.out.println(lineForPrintLine);
                    }
                },
                sleepyTime
        );
        */


        /*
        boolean thisIsTrue = true;
        //System.out.println(Arrays.toString(lineForPrint));
        while(thisIsTrue == true) {
            System.out.println("Hey hey");
            System.out.println("Hey hey hey");
        }
        */

        //double[] accValues = {xVal, yVal, zVal};
        //System.out.println(Arrays.toString(accValues));
    }

    /*
    public synchronized void printStuffToConsole(String lineForPrint) {
        boolean thisIsTrue = true;
        while (thisIsTrue) {
            System.out.println("HeyHey");
            System.out.println("heyHeyHey");
        }
    }
    */

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use atm.
    }


}
