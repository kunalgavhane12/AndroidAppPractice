package com.example.intentscreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent fromAct = getIntent();

        String name = fromAct.getStringExtra("Name");
        int rollNo = fromAct.getIntExtra("Roll No", 0);


        TextView txtStudentInfo = findViewById(R.id.textData);

        txtStudentInfo.setText("Roll No: "+ rollNo + "\nName: " + name);

    }
}