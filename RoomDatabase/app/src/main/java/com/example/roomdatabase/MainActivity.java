package com.example.roomdatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText edtName, edtAmount;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        edtName = findViewById(R.id.edtTitle);
        btnAdd = findViewById(R.id.btnAdd);

        DatabaseHelper databaseHelper = DatabaseHelper.getDB(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtName.getText().toString();
                String amount = edtAmount.getText().toString();

                // Insert expense on a background thread
                new Thread(() -> {
                    databaseHelper.expenseDao().addTx(new Expense(title, amount));

                    // Fetch all expenses on a background thread
                    List<Expense> arrExpenses = databaseHelper.expenseDao().getAllExpense();

                    // Log the data
                    for (Expense expense : arrExpenses) {
                        Log.d("Data", "Title: " + expense.getTitle() + ", Amount: " + expense.getAmount());
                    }
                }).start();
            }
        });
    }
}