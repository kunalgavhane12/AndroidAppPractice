package com.example.alarammanagerapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    static final int ALARM_REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnSet = findViewById(R.id.btnSet);
        EditText editTime = findViewById(R.id.edtTime);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input from the user
                String timeString = editTime.getText().toString();

                // Validate if the input is a valid number
                if (timeString.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a time", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int time = Integer.parseInt(timeString);
                    if (time <= 0) {
                        Toast.makeText(MainActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Calculate the trigger time
                    long triggerTime = System.currentTimeMillis() + (time * 1000);

                    // Create an Intent to trigger the BroadcastReceiver
                    Intent iBroadCast = new Intent(MainActivity.this, MyReceiver.class);

                    // Create a PendingIntent to be triggered by the AlarmManager
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            MainActivity.this, ALARM_REQ_CODE, iBroadCast,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );

                    // Set the alarm
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);

                    // Inform the user the alarm is set
                    Toast.makeText(MainActivity.this, "Alarm set for " + time + " seconds", Toast.LENGTH_SHORT).show();

                } catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid number
                    Toast.makeText(MainActivity.this, "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
