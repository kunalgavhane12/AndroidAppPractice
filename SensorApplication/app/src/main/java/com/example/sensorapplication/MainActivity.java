package com.example.sensorapplication;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView txtAccleroValues, txtProximityValues, txtAmbinetLightValues;
    SensorManager sensorManager;
    Sensor accleroSensor, proxiSensor, lightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize TextViews
        txtAccleroValues = findViewById(R.id.txtAccleroValues);
        txtProximityValues = findViewById(R.id.txtProximityValues);
        txtAmbinetLightValues = findViewById(R.id.txtAmbinetLightValues);

        // Get SensorManager system service
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null) {
            // Get sensors
            accleroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            proxiSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

            // Register listeners for available sensors
            if (accleroSensor != null) {
                sensorManager.registerListener(this, accleroSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "Accelerometer not available", Toast.LENGTH_SHORT).show();
            }

            if (proxiSensor != null) {
                sensorManager.registerListener(this, proxiSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "Proximity sensor not available", Toast.LENGTH_SHORT).show();
            }

            if (lightSensor != null) {
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "Ambient light sensor not available", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Sensor service not detected.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Handle Accelerometer data
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            txtAccleroValues.setText("ACCELERO\nX: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
        }
        // Handle Proximity sensor data
        else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            txtProximityValues.setText("Proxi Values: " + event.values[0]);

            if (event.values[0] > 0) {
                Toast.makeText(this, "Object is Far", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Object is Near", Toast.LENGTH_SHORT).show();
            }
        }
        // Handle Ambient light sensor data
        else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            txtAmbinetLightValues.setText("Lights Values: " + event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes (optional)
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister sensor listeners when the activity is paused
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
}
