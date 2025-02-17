package org.delta.example.doorlockapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button getDoorStatusButton;
    private ImageButton buttonSend, btnUnlock;
    private TextView textAck;
    private boolean isLocked = true; // Initial state is locked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize views
        buttonSend = findViewById(R.id.buttonSend);
        btnUnlock = findViewById(R.id.btnUnlock);
        textAck = findViewById(R.id.textAck);

        // Set up window insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lock button click listener (sets lock state to true and updates status)
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocked = true;
                textAck.setText("Locked");
            }
        });

        // Unlock button click listener (sets lock state to false and updates status)
        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocked = false;
                textAck.setText("Unlocked");
            }
        });
    }
}
