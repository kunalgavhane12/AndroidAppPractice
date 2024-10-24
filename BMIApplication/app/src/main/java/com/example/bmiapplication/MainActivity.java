package com.example.bmiapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        LinearLayout llmain;
        EditText edtWeight, edtHeightInInch, edtHeightInFt;
        Button btnCalculate;
        TextView textResult;

        llmain = findViewById(R.id.main);
        edtWeight = findViewById(R.id.edtWeight);
        edtHeightInInch = findViewById(R.id.edtHeightInInch);
        edtHeightInFt = findViewById(R.id.edtHeightInFt);
        btnCalculate = findViewById(R.id.btnCalculate);
        textResult = findViewById(R.id.textResult);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int wt = Integer.parseInt(edtWeight.getText().toString());
                int htFt = Integer.parseInt(edtHeightInFt.getText().toString());
                int htInch = Integer.parseInt(edtHeightInInch.getText().toString());

                int totalInch = htFt * 12 + htInch;

                double totalCm = totalInch * 2.53;

                double totalMeter = totalCm / 100;

                double bmi = wt / (totalMeter * totalMeter);

                if (bmi > 25){
                   textResult.setText("You're OverWeigth!");
                   llmain.setBackgroundColor(getResources().getColor(R.color.colorOW));
                } else if (bmi < 18 ) {
                    textResult.setText("You're UnderWeight!");
                    llmain.setBackgroundColor(getResources().getColor(R.color.colorUW));
                } else {
                    textResult.setText("You're Healthy!");
                    llmain.setBackgroundColor(getResources().getColor(R.color.colorH));
                }
            }
        });
    }
}