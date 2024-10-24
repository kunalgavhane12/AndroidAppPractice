package com.example.listview;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ListView lstview;
Spinner spinner;
AutoCompleteTextView autotxt;

ArrayList<String> arrName = new ArrayList<>();
ArrayList<String> arrIds = new ArrayList<>();
ArrayList<String> arrLanguage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        lstview = findViewById(R.id.listView);
        spinner = findViewById(R.id.spinner);
        autotxt = findViewById(R.id.autotext);

        for(int i = 0; i < 20; i++)
            arrName.add("Kunal" + i);

        arrIds.add("Adhar card");
        arrIds.add("Pan");
        arrIds.add("Votor Id");
        arrIds.add("Driving");
        arrIds.add("Passport");

        arrLanguage.add("C");
        arrLanguage.add("C++");
        arrLanguage.add("C#");
        arrLanguage.add("Java");
        arrLanguage.add("PHP");
        arrLanguage.add("Objective C");

        ArrayAdapter<String> lstAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrName);
        lstview.setAdapter(lstAdapter);

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrIds);
        spinner.setAdapter(spinAdapter);

        ArrayAdapter<String> autoComAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrLanguage);
        autotxt.setAdapter(autoComAdapter);
        autotxt.setThreshold(1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
